package ml.rugal.recorder.flac;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import net.sourceforge.javaFlacEncoder.FLACEncoder;
import net.sourceforge.javaFlacEncoder.FLACFileOutputStream;
import net.sourceforge.javaFlacEncoder.FLACOutputStream;
import net.sourceforge.javaFlacEncoder.FLACStreamOutputStream;
import net.sourceforge.javaFlacEncoder.StreamConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class only provide basic function for converting WAV stream to FLAC
 * output stream.
 * <p>
 * Class that contains methods to encode Wave files to FLAC files
 * THIS IS THANKS TO THE javaFlacEncoder Project created here:
 * http://sourceforge.net/projects/javaflacencoder/
 * <p>
 * Thanks to gearcode https://github.com/gearcode/speech-to-text, his work is
 * great! I realized not only file but stream could be "converted" in FLAC
 * encoder.
 * <p>
 * @author gearcode
 */
public class FlacStreamConverter
{

    private static final Logger LOG = LoggerFactory.getLogger(FlacStreamConverter.class.getName());

    /**
     * Constructor
     */
    private FlacStreamConverter()
    {

    }

    /**
     * Same as stream method but wrapped by file.
     * <p>
     * @param inputFile  Input wave file
     * @param outputFile Output FLAC file
     */
    public static void convertWaveToFlac(File inputFile, File outputFile)
    {
        try
        {
            convertWaveToFlac(AudioSystem.getAudioInputStream(inputFile), new FLACFileOutputStream(outputFile));
        }
        catch (UnsupportedAudioFileException | IOException e)
        {
        }
    }

    /**
     * Same as file method but just simply use file name string.
     * <p>
     * @param inputFile  Input wave file
     * @param outputFile Output FLAC file
     */
    public static void convertWaveToFlac(String inputFile, String outputFile)
    {
        convertWaveToFlac(new File(inputFile), new File(outputFile));
    }

    /**
     * Convert WAV stream data to FLAC stream data.
     * <p>
     * @param audioInputStream
     * @param flacOutputStream
     */
    public static void convertWaveToFlac(AudioInputStream audioInputStream, FLACOutputStream flacOutputStream)
    {
        AudioFormat format = audioInputStream.getFormat();

        StreamConfiguration streamConfiguration = new StreamConfiguration();
        streamConfiguration.setSampleRate((int) format.getSampleRate());
        streamConfiguration.setBitsPerSample(format.getSampleSizeInBits());
        streamConfiguration.setChannelCount(format.getChannels());
        try
        {
            int frameSize = format.getFrameSize();
            FLACEncoder flacEncoder = new FLACEncoder();
            flacEncoder.setStreamConfiguration(streamConfiguration);
            flacEncoder.setOutputStream(flacOutputStream);
            flacEncoder.openFLACStream();
            int[] sampleData = new int[(int) audioInputStream.getFrameLength()];
            byte[] samplesIn = new byte[frameSize];

            int i = 0;

            while (audioInputStream.read(samplesIn, 0, frameSize) != -1)
            {
                if (frameSize != 1)
                {
                    ByteBuffer bb = ByteBuffer.wrap(samplesIn);
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                    short shortVal = bb.getShort();
                    sampleData[i] = shortVal;
                }
                else
                {
                    sampleData[i] = samplesIn[0];
                }

                i++;
            }
            flacEncoder.addSamples(sampleData, i);
            flacEncoder.encodeSamples(i, false);
            flacEncoder.encodeSamples(flacEncoder.samplesAvailableToEncode(), true);
            audioInputStream.close();
        }
        catch (IOException ex)
        {
            LOG.error("Unable to convert WAV to FLAC", ex);
        }
    }

    /**
     * Easier method to convert waveStream to flacStream.
     * Just wrap the second parameter into the method body.
     * <p>
     * @param ais The input audio stream
     * <p>
     * @return
     */
    public static ByteArrayOutputStream convert(AudioInputStream ais)
    {
        ByteArrayOutputStream flacOS = new ByteArrayOutputStream();
        FLACOutputStream fos;
        try
        {
            fos = new FLACStreamOutputStream(flacOS);
            convertWaveToFlac(ais, fos);
        }
        catch (IOException ex)
        {
            LOG.error("Unable to obtain FLAC stream", ex);
        }
        return flacOS;
    }
}
