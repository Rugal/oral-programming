package ml.rugal.googlespeech.request;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
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
     * Must be a FLAC file, not responsible for but recognition if otherwise.
     *
     * @param file a valid FLAC file
     *
     * @return return a JSON which google directly return for us.
     *
     * @throws URISyntaxException Throw if URI concatenate error.
     * @throws IOException        Throw if file unable to open file.
     */
    public String execute(File file) throws URISyntaxException, IOException
    {

        URI uri = new URIBuilder()
                .setScheme("https")
                .setHost("www.google.com")
                .setPath("/speech-api/v2/recognize")
                .setParameter("output", "json")
                .setParameter("lang", this.lang)
                .setParameter("key", this.apikey).build();

        //Multipart file
        MultipartEntity entity = new MultipartEntity();
        entity.addPart("file", new FileBody(file));

        //http request
        HttpPost request = new HttpPost(uri);
        //content type = audio-flac
        request.setHeader("Content-Type", "audio/x-flac; rate=44100");
        request.setEntity(entity);
        //use String handler
        ResponseHandler<String> handler = new BasicResponseHandler();

        LOG.debug("Executing request:\n" + request.getRequestLine());

        String value = httpclient.execute(request, handler);
        LOG.debug("Received response body:\n" + value);
        return value;
    }

}
