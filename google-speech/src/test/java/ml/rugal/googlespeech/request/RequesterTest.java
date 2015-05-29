package ml.rugal.googlespeech.request;

import com.google.gson.Gson;
import java.io.File;
import ml.rugal.googlespeech.gson.SpeechResponseData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Rugal Bernstein
 */
public class RequesterTest
{

    private final String apikey = "AIzaSyBqC4CJz7HcalA_2aP5bd_Ll8iyLbgxtJs";

    private final APIRequest requester = new APIRequest(apikey);

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
    public void testExecute() throws Exception
    {
        System.out.println("execute");
        File file = new File("E:\\Downloads\\good-morning-google.flac");
        String prejson = requester.execute(file);
        String json = prejson.substring(prejson.indexOf("\n") + 1);
        SpeechResponseData ob = new Gson().fromJson(json, SpeechResponseData.class);
        System.out.println(ob.result[0].alternative[0].transcript);
    }

}
