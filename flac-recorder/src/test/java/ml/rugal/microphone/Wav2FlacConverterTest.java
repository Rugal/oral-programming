package ml.rugal.microphone;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.LineUnavailableException;
import net.sourceforge.javaFlacEncoder.FLAC_FileEncoder;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class Wav2FlacConverterTest
{

    public Wav2FlacConverterTest()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test
    @Ignore
    public void testExecute() throws IOException, LineUnavailableException, InterruptedException, URISyntaxException
    {
        System.out.println("execute");
        try (Microphone microphone = new Microphone(AudioFileFormat.Type.WAVE))
        {
            File file = new File("test.wav");
            if (!file.exists())
            {
                file.createNewFile();
            }

            System.out.println("Start");
            microphone.startRecord(file);
            Thread.sleep(6000);
            microphone.close();
            System.out.println("Stop");
            File out = new File("test.flac");
            new FLAC_FileEncoder().encode(file, out);
            file.deleteOnExit();
            out.deleteOnExit();
        }
    }

}
