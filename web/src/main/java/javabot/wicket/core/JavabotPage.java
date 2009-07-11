package javabot.wicket.core;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.PageParameters;
import javabot.wicket.panels.JavabotInfo;
import javabot.wicket.panels.Credits;
import javabot.wicket.panels.ChannelBox;
//
// User: joed
// Date: May 17, 2007
// Time: 3:25:10 PM

// 
public class JavabotPage extends WebPage {
    public JavabotPage() {
    }

    public JavabotPage(final PageParameters pageParameters) {
        super(pageParameters);
        add(new StyleSheetReference("stylesheet", getClass(), "css/style.css"));
        add(new JavabotInfo("info"));
        add(new Credits("credits"));
        add(new ChannelBox("ChannelBox"));
    }

    /**
         * Get downcast session object for easy access by subclasses
         *
         * @return The session
         */
        public JavabotSession getQuickStartSession()
        {
                return (JavabotSession)getSession();
        }

}
