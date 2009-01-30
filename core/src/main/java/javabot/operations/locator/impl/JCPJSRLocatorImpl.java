package javabot.operations.locator.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javabot.operations.locator.JCPJSRLocator;
import org.cyberneko.html.parsers.DOMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class JCPJSRLocatorImpl implements JCPJSRLocator {
    @Override
    public Map<String, String> locate(final Map<String, String> inputs) {
        final Map<String, String> retVal = new HashMap<String, String>();
        final String urlString = "http://www.jcp.org/en/jsr/detail?id=" + inputs.get("jsr");
        retVal.put("url", urlString);
        final URL url;
        try {
            BufferedReader reader = null;
            try {
                url = new URL(urlString);
                final URLConnection connection = url.openConnection();
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);
                reader = new BufferedReader(new InputStreamReader((InputStream) url.getContent()));
                final StringBuilder builder = new StringBuilder();
                while (!builder.toString().trim().endsWith("</html>")) {
                    final String line = reader.readLine();
                    if (line != null) {
                        builder.append(line);
                    }
                }
                final DOMParser docBuilder = new DOMParser();
                docBuilder.parse(new InputSource(new StringReader(builder.toString().trim())));
                final Document dom = docBuilder.getDocument();
                final NodeList nodes = dom.getElementsByTagName("div");
                for (int i = 0; i < nodes.getLength(); i++) {
                    final Node div = nodes.item(i);
                    final NamedNodeMap nnm = div.getAttributes();
                    if (nnm.getNamedItem("class") != null) {
                        if ("header1".equals(nnm.getNamedItem("class").getNodeValue())) {
                            retVal.put("title", getNodeValue(div));
                            break;
                        }
                    }
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }

        } catch (Exception e) {
            retVal.put("title", "Could not determine JSR information");
            e.printStackTrace();
        }
        return retVal;
    }

    private String getNodeValue(final Node node) {
        final StringBuilder value = new StringBuilder();
        final int length = node.getChildNodes().getLength();
        for (int index = 0; index < length; index++) {
            final Node child = node.getChildNodes().item(index);
            if (child.getChildNodes().getLength() != 0) {
                value.append(getNodeValue(child));
            } else {
                value.append(child.getNodeValue());
            }
        }
        return value.toString();
    }

    @Override
    public String findInformation(final int jsr) {
        final Map<String, String> inputs = new HashMap<String, String>();
        inputs.put("jsr", Integer.toString(jsr));
        final Map<String, String> outputs = locate(inputs);
        return "'" + outputs.get("title") + "' can be found at " + outputs.get("url");
    }
}