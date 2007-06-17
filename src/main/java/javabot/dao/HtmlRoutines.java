package javabot.dao;

import javabot.dao.model.Factoid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

//
// Author: joed

// Date  : Apr 12, 2007
class HtmlRoutines {

    private static final Log log = LogFactory.getLog(HtmlRoutines.class);

    public void dumpHTML(List<Factoid> factoids, String htmlFile) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(htmlFile));
            writer.println("<html><body><table border=\"1\"><tr><th>id</th><th>factoid</th><th>value</th><th>user</th></tr>\n");
            for (Factoid factoid : factoids) {
                StringBuilder html = new StringBuilder("<tr>");
                appendCell(html, Long.toString(factoid.getId()));
                appendCell(html, factoid.getName());
                appendCell(html, htmlize(factoid.getValue()));
                appendCell(html, factoid.getUserName());
                html.append("</tr>\n");
                writer.println(html);
            }
            writer.println("</table></body></html>");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private void appendCell(StringBuilder html, String value) {
        html.append("<td>");
        html.append(value);
        html.append("</td>");
    }

    private String htmlize(String value) {
        String newValue = value.replaceAll("<", "&lt;");
        newValue = newValue.replaceAll(">", "&gt;");
        int startHttp = newValue.indexOf("http://");
        while (startHttp != -1) {
            StringBuilder builder = new StringBuilder(newValue.substring(0, startHttp));
            int endHttp = newValue.indexOf(" ", startHttp);
            if (endHttp == -1) {
                endHttp = newValue.length();
            }
            String link = newValue.substring(startHttp, endHttp);
            builder.append("<a href=\"");
            builder.append(link);
            builder.append("\" target=\"_blank\">");
            builder.append(link);
            builder.append("</a>");
            builder.append(newValue.substring(endHttp));
            newValue = builder.toString();
            startHttp = newValue.indexOf("http://", link.length() + startHttp + 30);
        }
        return newValue;
    }

}
