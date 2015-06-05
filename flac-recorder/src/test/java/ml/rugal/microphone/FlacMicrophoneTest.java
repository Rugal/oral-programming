package ml.rugal.microphone;

import java.io.IOException;
import java.util.Map;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import ml.rugal.recorder.microphone.Microphone;
import ml.rugal.recorder.processor.ClipProcessor;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Rugal Bernstein
 */
public class FlacMicrophoneTest
{

    public FlacMicrophoneTest()
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
    public void testGetFlacFile() throws LineUnavailableException, IOException, InterruptedException
    {
        System.out.println("getFlacFile");
        Microphone microphone = new Microphone();
        //Just a test
        microphone.addAudioListener(new ClipProcessor()
        {

            @Override
            public void process(AudioInputStream ais)
            {
                System.out.println("Here");
            }
        });
        microphone.start();
        Thread.sleep(10000);
        microphone.close();
    }

    @Test
//    @Ignore
    public void testChannel() throws LineUnavailableException
    {
        Mixer mixer = AudioSystem.getMixer(null); // default mixer
        mixer.open();

        System.out.printf("Supported SourceDataLines of default mixer (%s):\n\n", mixer.getMixerInfo().getName());
        for (Line.Info info : mixer.getSourceLineInfo())
        {
            if (SourceDataLine.class.isAssignableFrom(info.getLineClass()))
            {
                SourceDataLine.Info info2 = (SourceDataLine.Info) info;
                System.out.println(info2);
                System.out.printf("  max buffer size: \t%d\n", info2.getMaxBufferSize());
                System.out.printf("  min buffer size: \t%d\n", info2.getMinBufferSize());
                AudioFormat[] formats = info2.getFormats();
                System.out.println("  Supported Audio formats: ");
                for (AudioFormat format : formats)
                {
                    System.out.println("    " + format);
                    Map<String, Object> prop = format.properties();
                    if (!prop.isEmpty())
                    {
                        System.out.println("      Properties: ");
                        prop.entrySet().stream().forEach((entry) ->
                        {
                            System.out.printf("      %s: \t%s\n", entry.getKey(), entry.getValue());
                        });
                    }
                }
                System.out.println();
            }
            else
            {
                System.out.println(info.toString());
            }
            System.out.println();
        }

    }

}
