package ga.rugal.integration;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.sound.sampled.LineUnavailableException;
import ml.rugal.googlespeech.gson.SpeechResponseData;
import ml.rugal.googlespeech.request.APIRequest;
import ml.rugal.microphone.FlacMicrophone;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class RequesterTest
{

    private final String apikey = "AIzaSyBqC4CJz7HcalA_2aP5bd_Ll8iyLbgxtJs";

    private final APIRequest request = new APIRequest(apikey);

    public RequesterTest()
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

        try (FlacMicrophone microphone = new FlacMicrophone())
        {
            File file = new File("test.wav");

            System.out.println("Start");
            microphone.startRecord(file);
            Thread.sleep(6000);
            microphone.close();
            System.out.println("Stop");
            File out = microphone.getFlacFile();
            String prejson = request.execute(out);
            String json = prejson.substring(prejson.indexOf("\n") + 1);
            SpeechResponseData ob = new Gson().fromJson(json, SpeechResponseData.class);
            System.out.println(ob.result[0].alternative[0].transcript);
            file.deleteOnExit();
            out.deleteOnExit();
        }
    }

}
