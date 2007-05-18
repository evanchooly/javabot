package wicket.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import wicket.core.JavabotPage;

import java.util.List;
import java.util.ArrayList;

// User: joed
// Date: May 17, 2007
// Time: 2:37:26 PM

// 
public class Index extends JavabotPage {

    public Index(final PageParameters parameters) {
        add(new StyleSheetReference("stylesheet", getClass(), "css/style.css"));


}
}
