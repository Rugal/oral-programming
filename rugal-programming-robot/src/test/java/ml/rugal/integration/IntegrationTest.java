package ml.rugal.integration;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import ml.rugal.googlespeech.gson.SpeechResponseData;
import ml.rugal.googlespeech.request.APIRequest;
import ml.rugal.googlespeech.request.SpeechApiKey;
import ml.rugal.recorder.microphone.Microphone;
import ml.rugal.robot.processor.FlacClipProcessor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Rugal Bernstein
 */
public class IntegrationTest
{

    private final APIRequest request = new APIRequest(SpeechApiKey.key);

    private static final String TRANSCRIPT = "good morning Google how are you feeling today";

    public IntegrationTest()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown() throws InterruptedException
    {
        while (true)
        {
            Thread.sleep(2000);
        }
    }

    @Test
    @Ignore
    public void testExecute() throws InterruptedException
    {
        System.out.println("Test Api with direct stream");
        Microphone microphone = new Microphone();
        microphone.addAudioListener(new FlacClipProcessor());
        try
        {
            microphone.start();
        }
        catch (LineUnavailableException ex)
        {
        }
//        microphone.close();
    }

    @Test
    @Ignore
    public void testReadAudioFile() throws UnsupportedAudioFileException, IOException, URISyntaxException
    {
        System.out.println("Read byte stream from sample file and Test API");
        File file = new File("../google-speech/good-morning-google.flac");

        AudioFormat af = new AudioFormat(44100f, 16, 1, true, false);

        String prejson = request.execute(file, af);
        String json = prejson.split("\n")[1];

        SpeechResponseData ob = new Gson().fromJson(json, SpeechResponseData.class);
        Assert.assertEquals(TRANSCRIPT, ob.result[0].alternative[0].transcript);

    }
}
