package javabot.admin;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

// Author: joed
// Date  : Jun 11, 2007

public class Home extends AuthenticatedWebPage {
    public Home(PageParameters parameters) {
        add(new FeedbackPanel("feedback"));
        add(new ChannelConfigPanel("channels"));
        add(new BotConfigPanel("bot"));
    }
}
