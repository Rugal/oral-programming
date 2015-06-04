/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.rugal.recorder.processor;

import java.io.ByteArrayOutputStream;
import javax.sound.sampled.AudioInputStream;
import ml.rugal.recorder.flac.FlacStreamConverter;

/**
 *
 * @author Rugal Bernstein
 */
public class FlacClipProcessor extends ClipProcessor
{

    @Override
    public void process(AudioInputStream ais)
    {
        ByteArrayOutputStream flacOS = FlacStreamConverter.convert(ais);

        //TODO then our work is to use the converted flac stream to do some work
    }

}
