package wicket.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import wicket.core.JavabotPage;
import wicket.panels.ChannelBox;
import wicket.panels.ChannelLog;
import wicket.panels.JavabotInfo;

// User: joed
// Date: May 17, 2007
// Time: 2:37:26 PM

public class Index extends JavabotPage {

    public Index(final PageParameters parameters) {
        add(new StyleSheetReference("stylesheet", getClass(), "css/style.css"));

        ChannelBox channelBox = new ChannelBox("ChannelBox");
        add(channelBox);

        if (!"".equals(parameters.getString("channel"))) {
            ChannelLog channelLog = new ChannelLog("ChannelLog", parameters.getString("channel"));
            add(channelLog);
        } else {
            ChannelLog channelLog = new ChannelLog("ChannelLog", "");
            add(channelLog);
        }
        JavabotInfo javabotInfo = new JavabotInfo("info");
        add(javabotInfo);
    }
}
