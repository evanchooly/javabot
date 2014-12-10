package javabot.operations.locator;

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
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.HashMap;
import java.util.Map;

public class JCPJSRLocator {
    public Map<String, String> locate(final Map<String, String> inputs) {
        final Map<String, String> retVal = new HashMap<>();
        final String urlString = "http://www.jcp.org/en/jsr/detail?id=" + inputs.get("jsr");
        retVal.put("url", urlString);
        try {
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 5 * 1000);
            HttpConnectionParams.setSoTimeout(params, 5 * 1000);
            HttpClient httpclient = new DefaultHttpClient(params);
            HttpGet httpget = new HttpGet(urlString);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try {
                    String data = EntityUtils.toString(entity);
                    Document doc = Jsoup.parse(data);
                    Element title = doc.select("div.header1").first();
                    StringBuilder titleText = new StringBuilder();
                    String separator = "";
                    // we build the content,
                    // because of that stupid <sup> thing the JCP uses
                    for (TextNode element : title.textNodes()) {
                        titleText.append(separator)
                                .append(element.text().trim());
                        separator = " ";
                    }
                    retVal.put("title", titleText.toString());
                } finally {
                    EntityUtils.consume(entity);
                }
            }

        } catch (Exception e) {
            retVal.put("title", null);
        }
        return retVal;
    }

    public String findInformation(final int jsr) {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("jsr", Integer.toString(jsr));
        final Map<String, String> outputs = locate(inputs);
        if (outputs.get("title") == null) {
            return "";
        }
        return "'" + outputs.get("title") + "' can be found at " + outputs.get("url");
    }
}
