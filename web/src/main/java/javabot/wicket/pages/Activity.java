package javabot.wicket.pages;

import javabot.wicket.core.JavabotPage;
import javabot.wicket.panels.ActivityPanel;
import org.apache.wicket.PageParameters;
// User: joed
// Date: May 17, 2007
// Time: 2:37:26 PM

public class Activity extends JavabotPage {

    public Activity(PageParameters parameters) {
        super(parameters);
        add(new ActivityPanel("stats"));


    }

}