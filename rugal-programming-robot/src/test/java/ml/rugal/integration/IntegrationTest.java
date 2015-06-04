package ml.rugal.integration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import ml.rugal.googlespeech.request.APIRequest;
import ml.rugal.recorder.flac.FlacStreamConverter;
import ml.rugal.recorder.microphone.Microphone;
import ml.rugal.robot.processor.FlacClipProcessor;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Rugal Bernstein
 */
public class IntegrationTest
{

    private final String apikey = "AIzaSyBqC4CJz7HcalA_2aP5bd_Ll8iyLbgxtJs";

    private final APIRequest request = new APIRequest(apikey);

    public IntegrationTest()
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
//    @Ignore
    public void testExecute() throws InterruptedException
    {
        Microphone microphone = new Microphone();
        microphone.addAudioListener(new FlacClipProcessor());
        try
        {
            microphone.start();
            Thread.sleep(10000);
        }
        catch (LineUnavailableException ex)
        {
        }
        microphone.close();
    }

    @Test
    @Ignore
    public void testReadAudioFile() throws UnsupportedAudioFileException, IOException, URISyntaxException
    {
        File file = new File("E:\\Downloads\\google.wav");
//        File file = new File("E:\\Downloads\\good-morning-google.flac");

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        ByteArrayOutputStream baos = FlacStreamConverter.convert(audioInputStream);

        System.out.println(request.execute(baos.toByteArray(), audioInputStream.getFormat()));

    }
}
