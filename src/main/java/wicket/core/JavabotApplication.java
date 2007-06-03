package wicket.core;


import org.apache.wicket.Request;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.AjaxServerAndClientTimeFilter;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import wicket.pages.Index;

// User: joed
// Date: May 17, 2007
// Time: 2:35:38 PM

// 
public class JavabotApplication extends WebApplication {

    public void JavabotApplication() {

    }

    protected void init() {
        addComponentInstantiationListener(new SpringComponentInjector(this));

        getResourceSettings().setThrowExceptionOnMissingResource(false);
        getRequestCycleSettings().addResponseFilter(new AjaxServerAndClientTimeFilter());
        getDebugSettings().setAjaxDebugModeEnabled(true);
        // getRequestCycleSettings().setBufferResponse(false);


        mountBookmarkablePage("/home", Index.class);
    
    }

    public Class getHomePage() {
        return Index.class;
    }

    public Session newSession(Request request, Response response) {
        return new JavabotSession(JavabotApplication.this, request);
    }


}
