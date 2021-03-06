package ml.rugal.recorder.processor;

import javax.sound.sampled.AudioInputStream;

/**
 * The processor class used in microphone data process.
 * <p>
 * @author Rugal Bernstein
 */
public abstract class ClipProcessor
{

    /**
     * Method to invoke when process audio data in real.
     * <p>
     * @param ais just audio input stream for a valid segment
     */
    public abstract void process(AudioInputStream ais);
}
