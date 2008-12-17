package javabot.admin;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class Home extends /*Authenticated*/WebPage {
    public Home(PageParameters parameters) {
        add(new FeedbackPanel("feedback"));
        add(new ChannelConfigPanel("channels"));
        add(new AdminConfigPanel("admins"));
        add(new BotConfigPanel("bot"));
        add(new JavadocPanel("javadoc"));
    }
}
