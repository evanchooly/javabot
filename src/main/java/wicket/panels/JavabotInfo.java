package wicket.panels;

import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.time.Duration;

// Author: joed

// Date  : May 18, 2007
public class JavabotInfo extends Panel {

    public JavabotInfo(String s) {
        super(s);
        add(new ExternalLink("homepage", "http://sourceforge.net/projects/fn-javabot", "HomePage"));

        FactoidCount factoidCount = new FactoidCount("factoid_count");

        add(factoidCount);
        factoidCount.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(30)));
    }
}
