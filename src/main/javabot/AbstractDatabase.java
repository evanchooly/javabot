package javabot;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

/**
 * Created Jun 26, 2005
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
public abstract class AbstractDatabase implements Database {
    private static Log log = LogFactory.getLog(AbstractDatabase.class);
    private String _htmlFile;

    public AbstractDatabase(Element root) {
        this(root.getChild("factoids").getAttributeValue("htmlfilename"));
    }

    public AbstractDatabase(String htmlFile) {
        _htmlFile = htmlFile;
    }

    public AbstractDatabase() {
    }

    protected void dumpHTML() {
        try {
            List<Factoid> factoids = getFactoids();
            PrintWriter writer = new PrintWriter(new FileWriter(_htmlFile));
            writer.println("<html><body><table border=\"1\" width=\"100%\"><tr>"
                + "<th width=\"5%\">id</th><th width=\"10%\">factoid</th>"
                + "<th width=\"75%\">value</th><th>user</th></tr>\n");
            for(Factoid factoid : factoids) {

                StringBuilder html = new StringBuilder("<tr><td>");
                html.append(factoid.getID());
                html.append("</td><td>");
                html.append(factoid.getName());
                html.append("</td><td>");
                html.append(htmlize(factoid.getValue()));
                html.append("</td><td>");
                html.append(factoid.getUser());
                html.append("</td></tr>\n");
                writer.println(html);
            }
            writer.println("</table></body></html>");
            writer.flush();
            writer.close();
        } catch(IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    protected String htmlize(final String value) {
        String newValue = value.replaceAll("<", "&lt;");
        newValue = newValue.replaceAll(">", "&gt;");
        int startHttp = newValue.indexOf("http://");
        while(startHttp != -1) {
            StringBuilder builder = new StringBuilder(newValue.substring(0, startHttp));
            int endHttp = newValue.indexOf(" ", startHttp);
            if(endHttp == -1) {
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