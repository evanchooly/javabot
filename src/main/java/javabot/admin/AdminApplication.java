package javabot.admin;

import org.apache.wicket.*;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequestCycleProcessor;
import org.apache.wicket.protocol.http.request.CryptedUrlWebRequestCodingStrategy;
import org.apache.wicket.protocol.http.request.WebRequestCodingStrategy;
import org.apache.wicket.request.IRequestCodingStrategy;
import org.apache.wicket.request.IRequestCycleProcessor;
//

// Author: joed

// Date  : Jun 11, 2007
public class AdminApplication extends WebApplication {

    /**
     * Constructor.
     */
    public AdminApplication() {
    }


    @Override
    public Class getHomePage() {
        return Home.class;
    }

    /**
     * @see WebApplication#newSession(Request, Response)
     */
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
                if (AuthenticatedWebPage.class.isAssignableFrom(componentClass)) {
                    // Is user signed in?
                    if (((AdminSession) Session.get()).isSignedIn()) {
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

    /**
     * @see WebApplication#newRequestCycleProcessor()
     */
    @Override
    protected IRequestCycleProcessor newRequestCycleProcessor() {
        return new WebRequestCycleProcessor() {
            @Override
            protected IRequestCodingStrategy newRequestCodingStrategy() {
                return new CryptedUrlWebRequestCodingStrategy(new WebRequestCodingStrategy());
            }
        };
    }


}


