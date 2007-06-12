package admininterface.admin;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.resources.StyleSheetReference;

// Author: joed
// Date  : Jun 11, 2007

public class Home extends AuthenticatedWebPage {
    /**
     * Constructor
     *
     * @param parameters Page parameters (ignored since this is the home page)
     */
    public Home(final PageParameters parameters) {

        add(new StyleSheetReference("stylesheet", getClass(), "css/style.css"));

    }
}
