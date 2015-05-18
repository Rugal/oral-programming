package ml.rugal.microphone;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
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

    private final String apikey = "AIzaSyBqC4CJz7HcalA_2aP5bd_Ll8iyLbgxtJs";

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
        try (FlacMicrophone microphone = new FlacMicrophone())
        {
            File file = new File("test.wav");

            System.out.println("Start");
            microphone.startRecord(file);
            Thread.sleep(6000);
            microphone.close();
            System.out.println("Stop");
            File out = microphone.getFlacFile();
            file.deleteOnExit();
            out.deleteOnExit();
        }
    }

}
