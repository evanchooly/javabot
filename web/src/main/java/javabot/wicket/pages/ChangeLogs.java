package javabot.wicket.pages;

import javabot.wicket.core.JavabotPage;
import javabot.wicket.panels.ChangesPanel;
import org.apache.wicket.PageParameters;

public class ChangeLogs extends JavabotPage {
    public ChangeLogs(final PageParameters parameters) {
        super(parameters);
        add(new ChangesPanel("changes"));
    }
}