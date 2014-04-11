package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import static java.lang.String.format;

public class Encodings {
    public static String encodeForm(String url, Map<String, String> data) throws UnsupportedEncodingException {
      StringBuilder builder = new StringBuilder(url);
      for (Entry<String, String> pair : data.entrySet()) {
        builder.append(format("&%s=%s", encode(pair.getKey()), encode(pair.getValue())));
      }
      return builder.toString();
    }

  private static String encode(final String value) throws UnsupportedEncodingException {
    return URLEncoder.encode(value, "UTF-8");
  }
}
