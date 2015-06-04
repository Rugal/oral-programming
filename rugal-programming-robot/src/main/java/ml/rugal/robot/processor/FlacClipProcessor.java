package ml.rugal.robot.processor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.sound.sampled.AudioInputStream;
import ml.rugal.googlespeech.request.APIRequest;
import ml.rugal.recorder.flac.FlacStreamConverter;
import ml.rugal.recorder.processor.ClipProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rugal Bernstein
 */
public class FlacClipProcessor extends ClipProcessor
{

    private static final Logger LOG = LoggerFactory.getLogger(FlacClipProcessor.class.getName());

    @Override
    public void process(AudioInputStream ais)
    {
        ByteArrayOutputStream flacOS = FlacStreamConverter.convert(ais);
        byte[] data = flacOS.toByteArray();
        APIRequest request = new APIRequest("AIzaSyBqC4CJz7HcalA_2aP5bd_Ll8iyLbgxtJs");
        try
        {
            String response = request.execute(data, ais.getFormat());
            LOG.info(response);
        }
        catch (URISyntaxException ex)
        {
            LOG.error("Unable to construct URI");
        }
        catch (IOException ex)
        {
            LOG.error("Unable to send request");
        }

    }

}
