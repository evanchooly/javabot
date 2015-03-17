package javabot.operations;

import javabot.Message;
import javabot.operations.urlcontent.URLContentAnalyzer;
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
import java.util.Arrays;
import java.util.stream.Stream;

public class URLTitleOperation extends BotOperation {
    URLContentAnalyzer analyzer = new URLContentAnalyzer();

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
            httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0");
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            try {
                if (!(response.getStatusLine().getStatusCode() == 404 ||
                        response.getStatusLine().getStatusCode() == 403)) {
                    if (entity != null) {
                        Document doc = Jsoup.parse(EntityUtils.toString(entity));
                        String title=clean(doc.title());
                        if (analyzer.check(url, title)) {
                            getBot().postMessage(event.getChannel(),
                                    event.getUser(),
                                    String.format("%s'%s title: %s",
                                            event.getUser().getNick(),
                                            event.getUser().getNick().endsWith("s") ? "" : "s",
                                            title),
                                    event.isTell());
                        }
                    }
                }
            } finally {
                EntityUtils.consume(entity);
            }
        } catch (IOException ioe) {
            if (loop && !url.substring(0, 10).contains("//www.")) {
                String tUrl = url.replace("//", "//www.");
                findTitle(event, tUrl, originalUrl, false);
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    private String clean(String title) {
        StringBuilder sb=new StringBuilder();
        title.chars().filter(i -> i<((2<<7)-1)).forEach(i->sb.append((char)i));
        return sb.toString();
    }
}
