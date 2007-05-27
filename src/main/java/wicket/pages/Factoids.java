package wicket.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import wicket.core.JavabotPage;
import wicket.panels.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import laszlo.panels.ExamplePanel;

// User: joed
// Date: May 17, 2007
// Time: 2:37:26 PM

public class Factoids extends JavabotPage {

    public Factoids(final PageParameters parameters) {
        add(new StyleSheetReference("stylesheet", getClass(), "css/style.css"));


        ChannelBox channelBox = new ChannelBox("ChannelBox");
        add(channelBox);

        FactoidsPanel factoids = new FactoidsPanel("factoids");
        add(factoids);

        JavabotInfo javabotInfo = new JavabotInfo("info");
        add(javabotInfo);

        Credits credits = new Credits("credits");
        add(credits);

        ExamplePanel laszlo = new ExamplePanel("laszlo");
        add(laszlo);
        
    }

}