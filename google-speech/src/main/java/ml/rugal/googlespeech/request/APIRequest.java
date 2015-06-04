package ml.rugal.googlespeech.request;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.sound.sampled.AudioFormat;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class send FLAC audio file to google speech API and fetch back result.
 * The API key must be activated by yourself. If you have problem with respect
 * to the API key, please refer to
 * http://www.chromium.org/developers/how-tos/api-keys
 * Up to 0.1 we only support FLAC type of audio, but I think this type is the
 * best for the already weak enough speech recognition.
 *
 * @author Rugal Bernstein
 * @since 0.1
 */
public class APIRequest
{

    private static final Logger LOG = LoggerFactory.getLogger(APIRequest.class.getName());

    private static final URIBuilder URITemplate = new URIBuilder().setScheme("https").setHost("www.google.com").setPath("/speech-api/v2/recognize");

    private final CloseableHttpClient httpclient = HttpClients.createDefault();

    private final String apikey;

    /**
     * Must initialize with valid google developer API key.
     *
     * @param apikey Must filled with valid API key.
     */
    public APIRequest(String apikey)
    {
        this.apikey = apikey;
    }

    private String lang = Lang.Canada.getLang();

    /**
     * You can set the language spec from {@code Lang}, those are supported by
     * google speech.
     * For instance {@code Lang.Canada.getLang()}
     *
     * @param lang The language code you want.
     */
    public void setLang(String lang)
    {
        this.lang = lang;
    }

    /**
     * Must be a FLAC file, not responsible for correct recognition if
     * otherwise.
     *
     * @param file        a valid FLAC file
     * @param audioFormat
     *
     *
     * @return return a JSON which google directly return for us.
     *
     * @throws URISyntaxException Throw if URI concatenate error.
     * @throws IOException        Throw if file unable to open file.
     */
    public String execute(File file, AudioFormat audioFormat) throws URISyntaxException, IOException
    {
        HttpEntityEnclosingRequestBase request = constructRequest(audioFormat);
        //Multipart file
        MultipartEntity entity = new MultipartEntity();
        //http request
        entity.addPart("file", new FileBody(file));
        request.setEntity(entity);

        return doRequest(request);
    }

    /**
     * Another version of execute, use byte array instead of file.
     * Beware the ContentType is audio/x-flac.
     * <p>
     * @param data
     *                    <p>
     * @param audioFormat
     *                    <p>
     * @return
     *         <p>
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     */
    public String execute(byte[] data, AudioFormat audioFormat) throws URISyntaxException, IOException
    {
        //construct a basic request
        HttpEntityEnclosingRequestBase request = constructRequest(audioFormat);
        //add audio data into HTTP request
        MultipartEntity entity = new MultipartEntity();
        //Data is flac by default, but I don't know if the ContentType here is useful or not
        ContentBody cd = new InputStreamBody(new ByteArrayInputStream(data), ContentType.create("audio/x-flac"));
        entity.addPart("data", cd);
        request.setEntity(entity);
        //send request and get response String back
        return doRequest(request);
    }

    /**
     * This method just for nicer code style. Wrap the sending procedure in
     * here. That's it!
     * <p>
     * @param request
     *                <p>
     * @return
     *         <p>
     * @throws IOException
     */
    private String doRequest(HttpEntityEnclosingRequestBase request) throws IOException
    {
        //use String handler
        LOG.debug("Executing request:\n" + request.getRequestLine());
        String value = httpclient.execute(request, new BasicResponseHandler());
        LOG.debug("Received response body:\n" + value);
        return value;
    }

    /**
     * TODO what if I need get request?
     * <p>
     * @param audioFormat
     *                    <p>
     * @return
     *         <p>
     * @throws URISyntaxException
     */
    private HttpEntityEnclosingRequestBase constructRequest(AudioFormat audioFormat) throws URISyntaxException
    {

        URI uri = URITemplate
            .setParameter("output", "json")
            .setParameter("lang", this.lang)
            .setParameter("key", this.apikey).build();
        //Multipart file
        //http request
        HttpPost request = new HttpPost(uri);
        //content type = audio-flac with sample rate 16000
        request.setHeader("Content-Type", "audio/x-flac; rate=" + (int) audioFormat.getSampleRate() + ";");
        return request;
    }
}
