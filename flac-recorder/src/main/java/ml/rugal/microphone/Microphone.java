package ml.rugal.microphone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Microphone class that contains methods to capture audio from microphone.
 * Credit to Original author Luke Kuza, Aaron
 * Gokaslan(https://github.com/lkuza2/java-speech-api).
 * <p>
 * Thanks to gearcode https://github.com/gearcode/speech-to-text, his work is
 * great!
 *
 * @author Luke Kuza, Aaron Gokaslan, gearcode, Rugal Bernstein
 */
public class Microphone
{

    private static final Logger LOG = LoggerFactory.getLogger(Microphone.class.getName());

    /**
     * Some value for computing volume
     */
    public static final float MAX_8_BITS_SIGNED = Byte.MAX_VALUE;

    public static final float MAX_8_BITS_UNSIGNED = 0xff;

    public static final float MAX_16_BITS_SIGNED = Short.MAX_VALUE;

    public static final float MAX_16_BITS_UNSIGNED = 0xffff;

    /**
     * Interval between valid audio data(ms)
     */
    public final long WORD_GAPS_DURATION = 2000;

    /**
     * Minimum volume, ignore if lower than this value
     */
    public final int AUDIO_LEVEL_MIN = 10;

    /**
     * TargetDataLine variable to store audio data from microphone.
     */
    private TargetDataLine targetDataLine = null;

    /**
     * Enumerator to represent the state for Microphone.
     */
    protected CaptureState state = CaptureState.CLOSED;

    private AudioFormat audioFormat = getAudioFormat();

    public Microphone()
    {
    }

    /**
     * The audio format to save in
     *
     * @return Returns AudioFormat to be used later when capturing audio from
     *         microphone
     */
    public AudioFormat getAudioFormat()
    {
        if (audioFormat == null)
        {
            LOG.debug("Using default audio format");
            audioFormat = defaultAudioFormat();
        }
        return audioFormat;
    }

    public void setAudioFormat(AudioFormat audioFormat)
    {
        this.audioFormat = audioFormat;
    }

    /**
     * The default audio format using specified parameters.
     * Using sample rate 16000, 16 bits, mono, signed, little endian.
     *
     * @return
     */
    private AudioFormat defaultAudioFormat()
    {
        float sampleRate = 16000;
        //8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        //8,16
        int channels = 1;
        //1,2
        boolean signed = true;
        //true,false
        boolean bigEndian = false;
        //true,false
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    /**
     * Gets the current state of Microphone
     *
     * @return RECORDING is returned when the Thread is recording Audio
     *         and/or saving it to a file<br>
     * PREPARING is returned if the Thread is initialize itself to
     * prepare for work<br>
     * CLOSED is returned if the Thread is not doing anything/not capturing
     * audio.
     */
    public CaptureState getState()
    {
        return state;
    }

    /**
     * Captures audio from the microphone.
     *
     *
     * @throws LineUnavailableException This happens if unable to initialize
     *                                  target data line, usually because
     *                                  microphone or audio driver problem.
     */
    public void start() throws LineUnavailableException
    {
        if (this.state == CaptureState.CLOSED)
        {
            LOG.debug("Prepare to record");
            state = CaptureState.PREPARING;
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, this.audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            LOG.debug("Ready to record");
            //Start recording
            LOG.debug("Start recording");
            state = CaptureState.RECORDING;
            new Thread(new CaptureThread()).start();
            LOG.debug("Recording started");
        }
        else
        {
            LOG.warn("Microphone is not yet closed, please check capture status");
        }
    }

    /**
     * Close the microphone capture, saving all processed audio to the specified
     * file.<br>
     * If already closed, this does nothing
     */
    public void close()
    {
        if (this.state != CaptureState.CLOSED)
        {
            LOG.debug("Stop recording");
            targetDataLine.stop();
            targetDataLine.close();
            this.state = CaptureState.CLOSED;
            LOG.debug("Recording Stopped");
        }
        else
        {
            LOG.debug("Microphone not start yet");
        }
    }

    private class CaptureThread implements Runnable
    {

        //start point of valid audio data
        private long startPoint = -1;

        //start point of invalid audio data
        private long gapPoint = -1;

        @Override
        public void run()
        {
            int frameSizeInBytes = audioFormat.getFrameSize();
            int bufferLengthInFrames = targetDataLine.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] bytes = new byte[bufferLengthInBytes];

            int numBytesRead;

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try
            {
                //apply for audio resource from OS
                targetDataLine.open(audioFormat);
                //start read buffer from I/O
                targetDataLine.start();
            }
            catch (LineUnavailableException e)
            {
                LOG.error("Unable to obtain required resources and start data line.");
                LOG.warn("Force to stop recording");
                close();
            }
            while (state == CaptureState.RECORDING)
            {
                //read data from data line
                numBytesRead = targetDataLine.read(bytes, 0, bytes.length);
                if (numBytesRead == -1)
                {
                    //if unable to read any byte from data line, something goes wrong
                    //stop work
                    close();
                    break;
                }
                /**
                 * Compute volume in this audio byte data
                 */
                int level = (int) (Microphone.calculateLevel(bytes, 0, 0, audioFormat) * 100);
                LOG.info("" + level);
                long currentTime = System.currentTimeMillis();
                //ignore unhearable audio
                if (level > AUDIO_LEVEL_MIN)
                {
                    //So this loop guarantee to have audio data
                    gapPoint = -1;
                    if (startPoint == -1)
                    {
                        startPoint = currentTime;
                        out = new ByteArrayOutputStream();
                    }
                    try
                    {
                        out.write(bytes);
                        LOG.debug("Write " + numBytesRead + " byte into buffer stream");
                    }
                    catch (IOException e)
                    {
                        LOG.error("Unable to write data into out stream");
                    }
                }
                else
                {
                    //if audio output stream contain valid audio data
                    //this is the time to send it out
                    if (startPoint != -1)
                    {
                        //if the right last loop contain valid audio data
                        if (gapPoint == -1)
                        {
                            gapPoint = currentTime;
                        }
                        if (currentTime - gapPoint > WORD_GAPS_DURATION)
                        {//send to google API iff silent time lasts for a predefined GAP long
                            LOG.debug("Clip length：" + (currentTime - startPoint));
                            startPoint = -1;
                            gapPoint = -1;
                            byte[] byteArray = out.toByteArray();
                            AudioInputStream audioInputStream
                                = new AudioInputStream(new ByteArrayInputStream(byteArray),
                                                       audioFormat,
                                                       byteArray.length / audioFormat.getFrameSize());
                            //TODO Send file to google API
                        }
                    }
                }
            }

        }
    }

    /**
     * Compute volume.
     * Different audio format have separate ways of volume computing.
     * Treat 16 and 8 bits differently.
     * Also consider big endian and little endian
     * <p>
     * @param buffer
     * @param readPoint
     * @param leftOver
     *                  <p>
     * @param format
     *                  <p>
     * @return
     */
    public static float calculateLevel(byte[] buffer, int readPoint, int leftOver, AudioFormat format)
    {
        int max = 0;
        float level;
        boolean use16Bit = (format.getSampleSizeInBits() == 16);
        boolean signed = (format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED);
        boolean bigEndian = (format.isBigEndian());

        if (use16Bit)
        {
            for (int i = readPoint; i < buffer.length - leftOver; i += 2)
            {
                int value;
                // deal with endianness
                int hiByte = (bigEndian ? buffer[i] : buffer[i + 1]);
                int loByte = (bigEndian ? buffer[i + 1] : buffer[i]);
                if (signed)
                {
                    short shortVal = (short) hiByte;
                    shortVal = (short) ((shortVal << 8) | (byte) loByte);
                    value = shortVal;
                }
                else
                {
                    value = (hiByte << 8) | loByte;
                }
                max = Math.max(max, value);
            } // for
        }
        else
        {
            // 8 bit - no endianness issues, just sign
            for (int i = readPoint; i < buffer.length - leftOver; i++)
            {
                int value;
                if (signed)
                {
                    value = buffer[i];
                }
                else
                {
                    short shortVal = 0;
                    shortVal = (short) (shortVal | buffer[i]);
                    value = shortVal;
                }
                max = Math.max(max, value);
            }
        }
        // express max as float of 0.0 to 1.0 of max value
        // of 8 or 16 bits (signed or unsigned)
        if (signed)
        {
            if (use16Bit)
            {
                level = (float) max / MAX_16_BITS_SIGNED;
            }
            else
            {
                level = (float) max / MAX_8_BITS_SIGNED;
            }
        }
        else
        {
            if (use16Bit)
            {
                level = (float) max / MAX_16_BITS_UNSIGNED;
            }
            else
            {
                level = (float) max / MAX_8_BITS_UNSIGNED;
            }
        }
        return level;
    }
}
