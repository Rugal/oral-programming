package ml.rugal.googlespeech.request;

import com.google.gson.Gson;
import java.io.File;
import javax.sound.sampled.AudioFormat;
import ml.rugal.googlespeech.gson.SpeechResponseData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Rugal Bernstein
 */
public class APIRequesterTest
{

    private final APIRequest requester = new APIRequest(SpeechApiKey.key);

    private static final String TRANSCRIPT = "good morning Google how are you feeling today";

    public APIRequesterTest()
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
    public void testExecute() throws Exception
    {
        System.out.println("Test Google API with sample audio file:");
        File file = new File("good-morning-google.flac");
        AudioFormat af = new AudioFormat(44100f, 16, 1, true, false);

        String prejson = requester.execute(file, af);
        String json = prejson.split("\n")[1];

        SpeechResponseData ob = new Gson().fromJson(json, SpeechResponseData.class);
        Assert.assertEquals(TRANSCRIPT, ob.result[0].alternative[0].transcript);
    }

}
