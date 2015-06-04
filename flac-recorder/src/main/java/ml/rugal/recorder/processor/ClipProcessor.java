package ml.rugal.recorder.processor;

import javax.sound.sampled.AudioInputStream;

/**
 *
 * @author Rugal Bernstein
 */
public abstract class ClipProcessor
{

    public abstract void process(AudioInputStream ais);
}
