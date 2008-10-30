package javabot.admin;

import javabot.Javabot;
import javabot.dao.AdminDao;
import org.apache.wicket.Component;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequestCycleProcessor;
import org.apache.wicket.protocol.http.request.CryptedUrlWebRequestCodingStrategy;
import org.apache.wicket.protocol.http.request.WebRequestCodingStrategy;
import org.apache.wicket.request.IRequestCodingStrategy;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminApplication extends WebApplication {
    private static final Logger log = LoggerFactory.getLogger(AdminApplication.class);
    private Javabot bot;

    @SpringBean
    private AdminDao dao;

    public AdminApplication() {
    }

    @Override
    public Class getHomePage() {
        return Home.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new AdminSession(this, request);
    }

    @Override
    protected void init() {
        super.init();
        addComponentInstantiationListener(new SpringComponentInjector(this));
        getSecuritySettings().setAuthorizationStrategy(new AdminAuthorizationStrategy());
        InjectorHolder.getInjector().inject(this);
        try {
            if(dao.findAll().isEmpty()) {
                dao.create("cheeser", "cheeser");
            }
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
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

    private static class AdminAuthorizationStrategy implements IAuthorizationStrategy {
        public boolean isActionAuthorized(Component component, Action action) {
            return true;
        }

        public boolean isInstantiationAuthorized(Class componentClass) {
            if(AuthenticatedWebPage.class.isAssignableFrom(componentClass)) {
                if(((AdminSession)Session.get()).isSignedIn()) {
                    return true;
                }
                throw new RestartResponseAtInterceptPageException(SignIn.class);
            }
            return true;
        }
    }
}