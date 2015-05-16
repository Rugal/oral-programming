package ml.rugal.microphone;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
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
 *
 * @author Luke Kuza, Aaron Gokaslan, Rugal Bernstein
 */
public class Microphone implements Closeable
{

    private static final Logger LOG = LoggerFactory.getLogger(Microphone.class.getName());

    /**
     * TargetDataLine variable to store audio data from microphone.
     */
    private TargetDataLine targetDataLine = null;

    /**
     * Enumerator to represent the state for Microphone.
     */
    private CaptureState state = CaptureState.CLOSED;

    /**
     * Variable for the audio saved file type
     */
    private AudioFileFormat.Type fileType;

    /**
     * Variable that holds the saved audio file
     */
    private File audioFile = null;

    private AudioFormat audioFormat = null;

    /**
     *
     * @param fileType File type to save the audio in<br>
     * Example, to save as WAVE use AudioFileFormat.Type.WAVE
     */
    public Microphone(AudioFileFormat.Type fileType)
    {
        this.fileType = fileType;
    }

    /**
     * Initializes the target data line.
     */
    private void initTargetDataLine()
    {
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
        try
        {
            this.targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        }
        catch (LineUnavailableException e)
        {
            LOG.error("Fail to get audio line", e);
        }
    }

    /**
     * Captures audio from the microphone and saves it a file
     *
     * @param audioFile The File to save the audio to
     *
     * @throws LineUnavailableException
     */
    public void startRecord(File audioFile) throws LineUnavailableException
    {
        this.state = CaptureState.PREPARING;
        this.audioFile = audioFile;

        if (this.targetDataLine == null)
        {
            initTargetDataLine();
        }

        //Start recorder thread
        new Thread(new CaptureThread()).start();

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
            audioFormat = defaultAudioFormat();
        }
        return audioFormat;
    }

    public void setAudioFormat(AudioFormat audioFormat)
    {
        this.audioFormat = audioFormat;
    }

    private AudioFormat defaultAudioFormat()
    {
        float sampleRate = 44100.0F;
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
     * Opens the microphone for real. This method will record audio data into
     * the targetDataLine object.
     * If it's already open, it does nothing.
     */
    private void open()
    {
        if (targetDataLine == null)
        {
            initTargetDataLine();
        }
        if (!targetDataLine.isOpen() && !targetDataLine.isRunning() && !targetDataLine.isActive())
        {
            try
            {
                this.state = CaptureState.RECORDING;
                this.targetDataLine.open(this.audioFormat);
                this.targetDataLine.start();
            }
            catch (LineUnavailableException e)
            {
                LOG.error("Unable to open microphone", e);
            }
        }

    }

    /**
     * Close the microphone capture, saving all processed audio to the specified
     * file.<br>
     * If already closed, this does nothing
     */
    @Override
    public void close()
    {
        if (this.state != CaptureState.CLOSED)
        {
            targetDataLine.stop();
            targetDataLine.close();
            this.state = CaptureState.CLOSED;
        }
    }

    /**
     * Thread to capture the audio from the microphone and save it to a file
     */
    private class CaptureThread implements Runnable
    {

        /**
         * Run method for thread
         */
        @Override
        public void run()
        {
            open();
            try
            {
                AudioSystem.write(new AudioInputStream(targetDataLine), fileType, audioFile);
            }
            catch (IOException ex)
            {
                LOG.error("Fail to write audio data", ex);
            }

        }
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
     * Sets the current state of Microphone
     *
     * @param state State from enumerator
     */
    private void setState(CaptureState state)
    {
        this.state = state;
    }

    public File getAudioFile()
    {
        return audioFile;
    }

    public void setAudioFile(File audioFile)
    {
        this.audioFile = audioFile;
    }

    public AudioFileFormat.Type getFileType()
    {
        return fileType;
    }

    public void setFileType(AudioFileFormat.Type fileType)
    {
        this.fileType = fileType;
    }

}
