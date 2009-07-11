package javabot.wicket.pages;

import javabot.wicket.core.JavabotPage;
import javabot.wicket.panels.FactoidsPanel;
import org.apache.wicket.PageParameters;

public class Factoids extends JavabotPage {
    public Factoids(PageParameters parameters) {
        super(parameters);
        add(new FactoidsPanel("factoids"));
    }
}