package javabot.operations;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
@SPI(BotOperation.class)
public class URLTitleOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(URLTitleOperation.class);

    @Override
    public List<Message> handleChannelMessage(final IrcEvent event) {
        List<Message> responses = new ArrayList<>();
        // First, see if we have "http://" or "https://" in the message.
        // If not, well, we're done, ain't we?
        final String message = event.getMessage();
        // no "http"? then no "http://" or "https://" either, eh
        if (message.contains("http")) {
            if (message.contains("http://") || message.contains("https://")) {
                for (String token : message.split(" ")) {
                    if (token.startsWith("http")) {
                        // let's try to build a url from it!
                        try {
                            URL url = new URL(token);
                            findTitle(event, responses, token, token, true);
                        } catch (MalformedURLException ignored) {
                            ignored.printStackTrace();
                        } catch (IOException ignored) {
                            ignored.printStackTrace();
                        }
                    }
                }
            }
        }
        return responses;
    }

    private void findTitle(IrcEvent event, List<Message> responses, String url,
                           String originalUrl, boolean loop) throws IOException {
        try {
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 5 * 1000);
            HttpConnectionParams.setSoTimeout(params, 5 * 1000);
            HttpClient httpclient = new DefaultHttpClient(params);
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try {
                        String data = EntityUtils.toString(entity);
                        Document doc = Jsoup.parse(data);
                        responses.add(new Message(event.getChannel(), event,
                                originalUrl + ": " + doc.title()));
                    } finally {
                        EntityUtils.consume(entity);
                    }
                }
            } finally {
            }
        } catch (IOException ioe) {
            if (loop) {
                // prepend "www" just in case...
                if (!url.substring(0, 10).contains("//www.")) {
                    String tUrl = url.replace("//", "//www.");
                    findTitle(event, responses, tUrl, originalUrl, false);
                }
            }

        }
    }
}
