package javabot.wicket.panels;

import javabot.wicket.pages.Activity;
import javabot.wicket.pages.ChangeLogs;
import javabot.wicket.pages.Factoids;
import javabot.wicket.pages.Karma;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

// Author: joed

// Date  : May 18, 2007
public class JavabotInfo extends Panel {

    public JavabotInfo(String id) {
        super(id);
        add(new ExternalLink("homepage", "http://kenai.com/projects/javabot", "HomePage"));
                            
        Link factoid = new BookmarkablePageLink("factoid_link", Factoids.class);
        factoid.add(new Label("factoid", "Factoids: "));
        add(factoid);

        Link stats = new BookmarkablePageLink("activity_link", Activity.class);
        stats.add(new Label("stats", "Statistics"));
        add(stats);

        Link karma = new BookmarkablePageLink("karma_link", Karma.class);
        karma.add(new Label("karma", "Karma ranking"));
        add(karma);

        Link changes = new BookmarkablePageLink("changes_link", ChangeLogs.class);
        changes.add(new Label("changes", "Changelog"));
        add(changes);
        add(new FactoidCount("factoid_count"));
    }
}
