package ml.rugal.recorder.microphone;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author Rugal Bernstein
 */
public abstract class AbstractMicrophone
{

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
    protected TargetDataLine targetDataLine = null;

    /**
     * Enumerator to represent the state for Microphone.
     */
    protected CaptureState state = CaptureState.CLOSED;

    protected AudioFormat audioFormat;

    public TargetDataLine getTargetDataLine()
    {
        return targetDataLine;
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
     * The audio format to save in
     *
     * @return Returns AudioFormat to be used later when capturing audio from
     *         microphone
     */
    public AudioFormat getAudioFormat()
    {
        return audioFormat;
    }

    public void setAudioFormat(AudioFormat audioFormat)
    {
        this.audioFormat = audioFormat;
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

    /**
     * Captures audio from the microphone.
     *
     *
     * @throws LineUnavailableException This happens if unable to initialize
     *                                  target data line, usually because
     *                                  microphone or audio driver problem.
     */
    public abstract void start() throws LineUnavailableException;

    /**
     * Close the microphone capture, saving all processed audio to the specified
     * file.<br>
     * If already closed, this does nothing
     */
    public abstract void close();
}
