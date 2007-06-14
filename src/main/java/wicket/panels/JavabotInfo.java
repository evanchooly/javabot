package wicket.panels;

import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.time.Duration;
import wicket.pages.ChangeLogs;
import wicket.pages.Factoids;
import wicket.pages.Karma;

// Author: joed

// Date  : May 18, 2007
public class JavabotInfo extends Panel {

    public JavabotInfo(String id) {
        super(id);
        add(new ExternalLink("homepage", "http://sourceforge.net/projects/fn-javabot", "HomePage"));

        Link factoid = new BookmarkablePageLink("factoid_link", Factoids.class);
        factoid.add(new Label("factoid", "Factoids: "));
        add(factoid);

        Link karma = new BookmarkablePageLink("karma_link", Karma.class);
        karma.add(new Label("karma", "Karma ranking"));
        add(karma);

        Link changes = new BookmarkablePageLink("changes_link", ChangeLogs.class);
        changes.add(new Label("changes", "Changelog"));
        add(changes);

        FactoidCount factoidCount = new FactoidCount("factoid_count");
        add(factoidCount);
        factoidCount.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(15)));
    }
}
