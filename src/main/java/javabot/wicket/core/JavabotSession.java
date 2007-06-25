package javabot.wicket.core;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;

public final class JavabotSession extends WebSession {
    protected JavabotSession(final WebApplication application, final Request request) {
        super(application, request);
    }
}