package javabot.wicket.pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javabot.wicket.core.JavabotPage;
import javabot.wicket.panels.ChannelLog;
import javabot.wicket.panels.NavigationPanel;
import javabot.wicket.panels.WelcomePanel;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.resources.StyleSheetReference;

// User: joed
// Date: May 17, 2007
// Time: 2:37:26 PM

public class Index extends JavabotPage {

    public Index(final PageParameters parameters) {
        super(parameters);
        
//        add(new StyleSheetReference("stylesheet", getClass(), "css/style.css"));

        String year = parameters.getString("0");
        String month = parameters.getString("1");
        String day = parameters.getString("2");
        String channel = parameters.getString("3");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        if (channel != null && year != null) {
            try {
                date = sdf.parse(year + "-" + month + "-" + day);
            } catch (ParseException e) {
                //log.error("Failed parsing date");
            }
        }
        NavigationPanel navigation = new NavigationPanel("navigation", date, channel);
        navigation.setVisible(date != null);
        add(navigation);
        ChannelLog channelLog = new ChannelLog("ChannelLog", channel, date);
        channelLog.setVisible(date != null);
        add(channelLog);
        WelcomePanel welcome = new WelcomePanel("Welcome");
        welcome.setVisible(date == null);
        add(welcome);
    }
}