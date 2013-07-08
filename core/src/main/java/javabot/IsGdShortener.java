package javabot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IsGdShortener {
  private static final Logger log = LoggerFactory.getLogger(IsGdShortener.class);

  private static final String API_URL = "http://is.gd/api.php?longurl=";

  private final String url;

  public IsGdShortener(final String url) {
    this.url = url;
  }

  public String invoke() {
    StringBuilder buff = null;
    try {
      final HttpURLConnection connection =
          (HttpURLConnection) new URL(API_URL + URLEncoder.encode(url, "UTF-8")).openConnection();
      connection.setRequestMethod("GET");
      connection.setInstanceFollowRedirects(true);
      connection.setConnectTimeout(7500);
      try (InputStreamReader isr = new InputStreamReader(connection.getInputStream());
           BufferedReader reader = new BufferedReader(isr)) {
        connection.connect();
        final int code = connection.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
          throw new IOException("Result code: " + code);
        }
        String ret;
        buff = new StringBuilder();
        while ((ret = reader.readLine()) != null) {
          buff.append(ret);
        }
      } finally {
        connection.disconnect();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return buff == null ? null : buff.toString();
  }
}
