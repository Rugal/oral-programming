package ml.rugal.googlespeech.request;

import com.google.gson.Gson;
import java.io.File;
import javax.sound.sampled.AudioFormat;
import ml.rugal.googlespeech.SpeechApiKey;
import ml.rugal.googlespeech.gson.SpeechResponseData;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Rugal Bernstein
 */
public class APIRequesterTest
{

    private final APIRequest requester = new APIRequest(SpeechApiKey.key);

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
    @Ignore
    public void testExecute() throws Exception
    {
        System.out.println("execute");
        File file = new File("E:\\Downloads\\good-morning-google.flac");

        AudioFormat af = new AudioFormat(44100f, 16, 1, true, false);

        String prejson = requester.execute(file, af);
        String json = prejson.substring(prejson.indexOf("\n") + 1);
        SpeechResponseData ob = new Gson().fromJson(json, SpeechResponseData.class);
        System.out.println(ob.result[0].alternative[0].transcript);
    }

}
