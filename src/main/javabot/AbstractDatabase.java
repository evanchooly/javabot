package javabot;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

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

    protected void dumpHTML() {
        try {
            Iterator<String> iterator = keys().iterator();
            PrintWriter writer = new PrintWriter(new FileWriter(_htmlFile));
            writer.println
                ("<html><body><table border=\"1\"><tr><th>" +
                    "factoid</th><th>value</th></tr>");
            while(iterator.hasNext()) {
                String factoid = (String)iterator.next();
                String value = getFactoid(factoid);
                value = value.replaceAll("<", "&lt;");
                value = value.replaceAll(">", "&gt;");
                StringBuilder builder = new StringBuilder(value);
                int startHttp = 0;
                do {
                    startHttp = value.indexOf("http://", startHttp);
                    if(startHttp != -1) {
                        int endHttp = value.indexOf(" ", startHttp);
                        if(endHttp == -1) {
                            endHttp = value.length();
                        }
                        builder.append(value.substring(0, startHttp));
                        builder.append("<a href=\"");
                        builder.append(value.substring(startHttp, endHttp));
                        builder.append("\">");
                        builder.append(value.substring(startHttp, endHttp));
                        builder.append("</a>");
                        builder.append(value.substring(endHttp));
                        startHttp = builder.indexOf("</a>", startHttp);
                    }
                } while(startHttp != -1);
                builder.insert(0,"<tr><td>");
                builder.insert(0,factoid);
                builder.insert(0,"</td><td>");
                builder.insert(0,"</td></tr>");
                writer.println(builder.toString());
            }
            writer.println("</table></body></html>");
            writer.flush();
            writer.close();
        } catch(IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    protected abstract Set<String> keys();
}
