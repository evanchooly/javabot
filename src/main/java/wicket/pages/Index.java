package wicket.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.Model;
import wicket.core.JavabotPage;

// User: joed
// Date: May 17, 2007
// Time: 2:37:26 PM

// 
public class Index extends JavabotPage {

    public Index(final PageParameters parameters) {

         add(new StyleSheetReference("stylesheet", getClass(), "css/style.css"));

    }
}
