package ml.rugal.integration;

import com.google.gson.Gson;
import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.sound.sampled.LineUnavailableException;
import ml.rugal.googlespeech.gson.SpeechResponseData;
import ml.rugal.googlespeech.request.APIRequest;
import ml.rugal.microphone.FlacMicrophone;
import ml.rugal.operator.CommandExecutor;
import ml.rugal.operator.commandSpec.Command;
import ml.rugal.operator.commandSpec.CommandFactory;
import ml.rugal.operator.exception.CommandInvalidException;
import ml.rugal.operator.exception.CommandNotFoundException;
import org.junit.After;
import org.junit.Before;
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
    public void testExecute() throws IOException, LineUnavailableException, InterruptedException, URISyntaxException, CommandNotFoundException, CommandInvalidException, AWTException
    {
        System.out.println("execute");
        CommandExecutor executor = new CommandExecutor();
        try (FlacMicrophone microphone = new FlacMicrophone())
        {
            File file = new File("test.wav");
//            File out = new File("E:\\Downloads\\move.flac");
            System.out.println("Start");
            microphone.startRecord(file);
            Thread.sleep(4000);
            microphone.close();
            System.out.println("Stop");
            File out = microphone.getFlacFile();
            String prejson = request.execute(out);
            String json = prejson.substring(prejson.indexOf("\n") + 1);
            SpeechResponseData ob = new Gson().fromJson(json, SpeechResponseData.class);
            System.out.println(ob.result[0].alternative[0].transcript);
            Command cmd = CommandFactory.constructCommand(ob.result[0].alternative[0].transcript.split(" "));
            executor.execute(cmd);
            file.deleteOnExit();
            out.deleteOnExit();
        }
    }

}
