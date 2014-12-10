package javabot.operations;

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

import java.io.IOException;
import java.net.URL;

public class URLTitleOperation extends BotOperation {
    @Override
    public boolean handleChannelMessage(final Message event) {
        final String message = event.getValue();
        if (message.contains("http://") || message.contains("https://")) {
            for (String token : message.split(" ")) {
                if (token.startsWith("http")) {
                    // let's try to build a url from it!
                    try {
                        new URL(token);
                        findTitle(event, token, token, true);
                        return true;
                    } catch (IOException ignored) {
                        ignored.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    private void findTitle(Message event, String url, String originalUrl, boolean loop) throws IOException {
        try {
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 5 * 1000);
            HttpConnectionParams.setSoTimeout(params, 5 * 1000);
            HttpClient httpclient = new DefaultHttpClient(params);
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try {
                    Document doc = Jsoup.parse(EntityUtils.toString(entity));
                    getBot().postMessage(event.getChannel(), event.getUser(), originalUrl + ": " + doc.title(), event.isTell());
                } finally {
                    EntityUtils.consume(entity);
                }
            }
        } catch (IOException ioe) {
            if (loop && !url.substring(0, 10).contains("//www.")) {
                String tUrl = url.replace("//", "//www.");
                findTitle(event, tUrl, originalUrl, false);
            }
        }
    }
}
