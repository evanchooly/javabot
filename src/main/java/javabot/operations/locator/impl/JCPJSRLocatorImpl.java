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
    private static final Logger log = LoggerFactory.getLogger(JCPJSRLocatorImpl.class);

    @Override
    public Map<String, String> locate(Map<String, String> inputs) {
        Map<String, String> retVal = new HashMap<String, String>();
        String urlString = "http://www.jcp.org/en/jsr/detail?id=" + inputs.get("jsr");
        retVal.put("url", urlString);
        URL url;
        try {
            BufferedReader reader = null;
            try {
                url = new URL(urlString);
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);
                reader = new BufferedReader(new InputStreamReader((InputStream) url.getContent()));
                StringBuilder builder = new StringBuilder();
                while (!builder.toString().trim().endsWith("</html>")) {
                    String line = reader.readLine();
                    if (line != null) {
                        builder.append(line);
                    }
                }
                DOMParser docBuilder = new DOMParser();
                docBuilder.parse(new InputSource(new StringReader(builder.toString().trim())));
                Document dom = docBuilder.getDocument();
                NodeList nodes = dom.getElementsByTagName("div");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node div = nodes.item(i);
                    NamedNodeMap nnm = div.getAttributes();
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

    private String getNodeValue(Node node) {
        StringBuilder value = new StringBuilder();
        int length = node.getChildNodes().getLength();
        for (int index = 0; index < length; index++) {
            Node child = node.getChildNodes().item(index);
            if (child.getChildNodes().getLength() != 0) {
                value.append(getNodeValue(child));
            } else {
                value.append(child.getNodeValue());
            }
        }
        return value.toString();
    }

    @Override
    public String findInformation(int jsr) {
        Map<String, String> inputs = new HashMap<String, String>();
        inputs.put("jsr", Integer.toString(jsr));
        Map<String, String> outputs = locate(inputs);
        return "'" + outputs.get("title") + "' can be found at " + outputs.get("url");
    }
}