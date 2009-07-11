package javabot.wicket.pages;

import javabot.wicket.core.JavabotPage;
import javabot.wicket.panels.KarmaPanel;
import org.apache.wicket.PageParameters;

public class Karma extends JavabotPage {

    public Karma(final PageParameters parameters) {
        super(parameters);
        add(new KarmaPanel("karmalist"));
    }
}