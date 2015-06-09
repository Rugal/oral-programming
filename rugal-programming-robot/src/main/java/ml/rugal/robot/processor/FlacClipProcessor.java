package ml.rugal.robot.processor;

import com.google.gson.Gson;
import java.awt.AWTException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.sound.sampled.AudioInputStream;
import ml.rugal.googlespeech.gson.SpeechResponseData;
import ml.rugal.googlespeech.request.APIRequest;
import ml.rugal.googlespeech.request.SpeechApiKey;
import ml.rugal.operator.CommandExecutor;
import ml.rugal.operator.commandSpec.Command;
import ml.rugal.operator.commandSpec.CommandFactory;
import ml.rugal.recorder.flac.FlacStreamConverter;
import ml.rugal.recorder.processor.ClipProcessor;
import ml.rugal.robot.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rugal Bernstein
 */
public class FlacClipProcessor extends ClipProcessor
{

    private final APIRequest request = new APIRequest(SpeechApiKey.key);

    private static final Gson gson = new Gson();

    private final CommandExecutor executor = new CommandExecutor();

    private static final Logger LOG = LoggerFactory.getLogger(FlacClipProcessor.class.getName());

    private final MainFrame frame;

    public FlacClipProcessor(MainFrame Frame)
    {
        this.frame = Frame;
    }

    public FlacClipProcessor()
    {
        this(null);
    }

    @Override
    public void process(AudioInputStream ais)
    {
        ByteArrayOutputStream flacOS = FlacStreamConverter.convert(ais);
        byte[] data = flacOS.toByteArray();
        try
        {
            String response = request.execute(data, ais.getFormat());
            LOG.debug(response);
            String json = response.split("\n")[1];
            SpeechResponseData ob = gson.fromJson(json, SpeechResponseData.class);
            LOG.info(ob.result[0].alternative[0].transcript);
            if (frame != null)
            {
                frame.getTextArea().append(ob.result[0].alternative[0].transcript);
                frame.getTextArea().append("\n");
            }
            Command command = CommandFactory.constructCommand(ob.result[0].alternative[0].transcript.split(" "));
            executor.execute(command);

        }
        catch (URISyntaxException ex)
        {
            LOG.error("Unable to construct URI");
        }
        catch (IOException ex)
        {
            LOG.error("Unable to send request");
        }
        catch (AWTException ex)
        {
            LOG.error("Fatal error: unable to create keyboard for your machine.");
            LOG.error("System shutting down.");
            System.exit(1);
        }
        catch (Exception e)
        {
            LOG.error("Exception happened");
        }
    }

}
