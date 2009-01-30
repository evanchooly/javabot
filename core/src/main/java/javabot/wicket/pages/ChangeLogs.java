package javabot.wicket.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import javabot.wicket.core.JavabotPage;
import javabot.wicket.panels.ChangesPanel;
import javabot.wicket.panels.ChannelBox;
import javabot.wicket.panels.Credits;
import javabot.wicket.panels.JavabotInfo;

// User: joed
// Date: May 17, 2007
// Time: 2:37:26 PM

public class ChangeLogs extends JavabotPage {

    public ChangeLogs(final PageParameters parameters) {
        add(new StyleSheetReference("stylesheet", getClass(), "css/style.css"));


        ChannelBox channelBox = new ChannelBox("ChannelBox");
        add(channelBox);


        ChangesPanel changes = new ChangesPanel("changes");
        add(changes);

        JavabotInfo javabotInfo = new JavabotInfo("info");
        add(javabotInfo);

        Credits credits = new Credits("credits");
        add(credits);


    }

}