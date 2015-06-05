package ml.rugal.recorder.microphone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import ml.rugal.recorder.processor.ClipProcessor;
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
 * @author Luke Kuza, Aaron Gokaslan, 李腾, Rugal Bernstein
 */
public class Microphone extends AbstractMicrophone
{

    private static final Logger LOG = LoggerFactory.getLogger(Microphone.class.getName());

    private final List<ClipProcessor> processor = new ArrayList<>();

    /**
     *
     */
    public Microphone()
    {
        this.audioFormat = defaultAudioFormat();
    }

    /**
     * Use this method because instantiate a AudioFormat class is a little bit
     * of troublesome.
     * The default audio format using specified parameters.
     * Using sample rate 16000, 16 bits, mono, signed, little endian.
     *
     * @return give the default audio format.
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
     * Add a audio listener that could process the clip produced by microphone
     * <p>
     * @param p the processor listen to clip
     */
    public void addAudioListener(ClipProcessor p)
    {
        processor.add(p);
    }

    /**
     * Clear all audio processor
     */
    public void clearAudioListener()
    {
        processor.clear();
    }

    /**
     * {@inheritDoc }
     */
    @Override
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
     *
     * {@inheritDoc }
     */
    @Override
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

    /**
     * The most important thread for recording.
     */
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
                            processor.stream().forEach((cp) ->
                            {
                                cp.process(audioInputStream);
                            });
                        }
                    }
                }
            }
        }
    }
}
