package javabot.admin;

import javabot.Javabot;
import org.apache.wicket.Component;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequestCycleProcessor;
import org.apache.wicket.protocol.http.request.CryptedUrlWebRequestCodingStrategy;
import org.apache.wicket.protocol.http.request.WebRequestCodingStrategy;
import org.apache.wicket.request.IRequestCodingStrategy;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

public class AdminApplication extends WebApplication {
    private Javabot bot;

    public AdminApplication() {
    }

    @Override
    public Class getHomePage() {
        return Home.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new AdminSession(AdminApplication.this, request);
    }

    @Override
    protected void init() {
        super.init();
        addComponentInstantiationListener(new SpringComponentInjector(this));
        getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy() {
            public boolean isActionAuthorized(Component component, Action action) {
                return true;
            }

            public boolean isInstantiationAuthorized(Class componentClass) {
                if(AuthenticatedWebPage.class.isAssignableFrom(componentClass)) {
                    // Is user signed in?
                    if(((AdminSession)Session.get()).isSignedIn()) {
                        // okay to proceed
                        return true;
                    }

                    // Force sign in
                    throw new RestartResponseAtInterceptPageException(SignIn.class);
                }
                return true;
            }
        });
    }

    @Override
    protected IRequestCycleProcessor newRequestCycleProcessor() {
        return new WebRequestCycleProcessor() {
            @Override
            protected IRequestCodingStrategy newRequestCodingStrategy() {
                return new CryptedUrlWebRequestCodingStrategy(new WebRequestCodingStrategy());
            }
        };
    }

    public void bounceBot() {
        if(bot != null) {
            bot.dispose();
        }
        bot = new Javabot();
    }
}