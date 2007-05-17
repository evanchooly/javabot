package wicket.core;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;

//
// User: joed
// Date: May 17, 2007
// Time: 3:22:13 PM

// 
public final class JavabotSession extends WebSession {
    // TODO Add any session properties here

    /**
     * Constructor
     *
     * @param application The application
     * @param request     The request
     */
    protected JavabotSession(final WebApplication application, final Request request) {
        super(application, request);
    }
}