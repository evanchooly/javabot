package wicket.panels;

import javabot.dao.FactoidDao;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

// Author: joed

// Date  : May 18, 2007
public class JavabotInfo extends Panel {

    @SpringBean
    FactoidDao factoid;

    public JavabotInfo(String s) {
        super(s);
        add(new ExternalLink("homepage", "http://sourceforge.net/projects/fn-javabot", "HomePage"));
        add(new Label("factoid_count", factoid.getNumberOfFactoids().toString()));
    }
}
