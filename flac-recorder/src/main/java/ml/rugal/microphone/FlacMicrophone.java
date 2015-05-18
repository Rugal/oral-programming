package ml.rugal.microphone;

import java.io.File;
import javax.sound.sampled.AudioFileFormat;
import net.sourceforge.javaFlacEncoder.FLAC_FileEncoder;

/**
 *
 * @author Rugal Bernstein
 */
public class FlacMicrophone extends Microphone
{

    private final FLAC_FileEncoder encoder = new FLAC_FileEncoder();

    public FlacMicrophone()
    {
        super(AudioFileFormat.Type.WAVE);
    }

    public File getFlacFile()
    {
        if (this.state != CaptureState.CLOSED)
        {
            return null;
        }
        File flacFile = new File(this.audioFile.getAbsolutePath().replace(".wav", ".flac"));
        encoder.encode(audioFile, flacFile);
        return flacFile;
    }

}
