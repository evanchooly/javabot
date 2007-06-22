package javabot.admin;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.resources.StyleSheetReference;

// Author: joed
// Date  : Jun 11, 2007

public class Home extends AuthenticatedWebPage {
    /**
     * Constructor
     *
     * @param parameters Page parameters (ignored since this is the home page)
     */
    public Home(PageParameters parameters) {
        add(new StyleSheetReference("stylesheet", getClass(), "style.css"));
        add(new ChannelConfigPanel("channels"));
        add(new BotConfigPanel("bot"));
    }
}
