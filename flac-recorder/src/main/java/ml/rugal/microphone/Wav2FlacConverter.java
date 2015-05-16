package ml.rugal.microphone;

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
import net.sourceforge.javaFlacEncoder.StreamConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that contains methods to encode Wave files to FLAC files
 * THIS IS THANKS TO THE javaFlacEncoder Project created here:
 * http://sourceforge.net/projects/javaflacencoder/.
 *
 * Credit to Original author Luke Kuza, Aaron
 * Gokaslan(https://github.com/lkuza2/java-speech-api).
 *
 * @author Luke Kuza, Aaron Gokaslan, Rugal Bernstein
 * @deprecated
 */
public class Wav2FlacConverter
{

    private static final Logger LOG = LoggerFactory.getLogger(Wav2FlacConverter.class.getName());

    private AudioFormat audioFormat = null;

    public Wav2FlacConverter(AudioFormat audioFormat)
    {
        this.audioFormat = audioFormat;
    }

    private StreamConfiguration configStream()
    {
        StreamConfiguration streamConfiguration = new StreamConfiguration();
        streamConfiguration.setSampleRate((int) audioFormat.getSampleRate());
        streamConfiguration.setBitsPerSample(audioFormat.getSampleSizeInBits());
        streamConfiguration.setChannelCount(audioFormat.getChannels());
        return streamConfiguration;

    }

    /**
     * Converts a wave file to a FLAC file(in order to POST the data to Google
     * and retrieve a response) <br>
     * Sample Rate is 8000 by default
     *
     * @param inputFile  Input wave file
     * @param outputFile Output FLAC file
     */
    public void convertWaveToFlac(File inputFile, File outputFile)
    {
        AudioInputStream audioInputStream;
        StreamConfiguration streamConfiguration = configStream();
        FLACEncoder flacEncoder = new FLACEncoder();
        try (FLACFileOutputStream flacOutputStream = new FLACFileOutputStream(outputFile))
        {

            //Set input audio stream
            audioInputStream = AudioSystem.getAudioInputStream(inputFile);

            //set output audio stream
            flacEncoder.setStreamConfiguration(streamConfiguration);
            flacEncoder.setOutputStream(flacOutputStream);
            flacEncoder.openFLACStream();

            int frameSize = audioInputStream.getFormat().getFrameSize();
            int frameLength = (int) audioInputStream.getFrameLength();
            if (frameLength <= AudioSystem.NOT_SPECIFIED)
            {
                frameLength = 16384;//Arbitrary file size
            }
            int[] sampleData = new int[frameLength];
            byte[] samplesIn = new byte[frameSize];
            int i = 0;
            // read from instream
            for (; audioInputStream.read(samplesIn, 0, frameSize) != -1; i++)
            {
                if (frameSize != 1)
                {
                    ByteBuffer bb = ByteBuffer.wrap(samplesIn);
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                    short shortVal = bb.getShort();
                    //Set to outstream
                    sampleData[i] = shortVal;
                }
                else
                {
                    sampleData[i] = samplesIn[0];
                }

            }
            audioInputStream.close();
            flacOutputStream.close();

            sampleData = truncateNullData(sampleData, i);
            flacEncoder.addSamples(sampleData, i);
//            flacEncoder.encodeSamples(i, false);
            flacEncoder.encodeSamples(flacEncoder.samplesAvailableToEncode(), true);

        }
        catch (UnsupportedAudioFileException ua)
        {
            LOG.error("This audio file is not supported.", ua);
        }
        catch (IOException ex)
        {
            LOG.error("IO Error occur while processing.", ex);
        }
    }

    /**
     * Used for when the frame length is unknown to shorten the array to prevent
     * huge blank end space
     *
     * @param sampleData The int[] array you want to shorten
     * @param index      The index you want to shorten it to
     *
     * @return The shortened array
     */
    private int[] truncateNullData(int[] sampleData, int index)
    {
        if (index == sampleData.length)
        {
            return sampleData;
        }
        int[] out = new int[index];
        System.arraycopy(sampleData, 0, out, 0, index);
        return out;
    }

}
