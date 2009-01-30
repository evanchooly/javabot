package javabot.wicket.core;

import javabot.wicket.pages.Index;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.AjaxServerAndClientTimeFilter;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

public class JavabotApplication extends WebApplication {

    @Override
    protected void init() {
        addComponentInstantiationListener(new SpringComponentInjector(this));
        getResourceSettings().setThrowExceptionOnMissingResource(false);
        getRequestCycleSettings().addResponseFilter(new AjaxServerAndClientTimeFilter());
        getDebugSettings().setAjaxDebugModeEnabled(false);
        // getRequestCycleSettings().setBufferResponse(false);
        // getMarkupSettings().setStripWicketTags(true);
        mountBookmarkablePage("/home", Index.class);
    }

    @Override
    public Class getHomePage() {
        return Index.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new JavabotSession(this, request);
    }
}