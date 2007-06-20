package javabot.admin;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.resources.StyleSheetReference;

public class SignOut extends AuthenticatedWebPage {
    /**
     * Constructor
     *
     * @param parameters Page parameters (ignored since this is the home page)
     */
    public SignOut(PageParameters parameters) {
        getSession().invalidate();
        add(new StyleSheetReference("stylesheet", getClass(), "style.css"));
    }
}
