package wicket.core;


import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.AjaxServerAndClientTimeFilter;
import org.apache.wicket.protocol.http.WebApplication;
import wicket.pages.Index;

//
// User: joed
// Date: May 17, 2007
// Time: 2:35:38 PM

// 
public class JavabotApplication extends WebApplication {

    public void JavabotApplication() {

    }

    protected void init() {
        getResourceSettings().setThrowExceptionOnMissingResource(false);
        getRequestCycleSettings().addResponseFilter(new AjaxServerAndClientTimeFilter());
        getDebugSettings().setAjaxDebugModeEnabled(true);
    }

    public Class getHomePage() {
        return Index.class;
    }

    public Session newSession(Request request, Response response) {
        return new JavabotSession(JavabotApplication.this, request);
    }


}
