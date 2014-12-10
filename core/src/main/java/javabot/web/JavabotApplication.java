package javabot.web;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import javabot.JavabotModule;
import javabot.web.auth.RestrictedProvider;
import javabot.web.resources.AdminResource;
import javabot.web.resources.BotResource;
import javabot.web.resources.PublicOAuthResource;
import org.eclipse.jetty.server.session.SessionHandler;
import org.mongodb.morphia.Datastore;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.EnumSet;

@Singleton
public class JavabotApplication extends Application<JavabotConfiguration> {
    @Inject
    private Datastore ds;

    @Inject
    private Injector injector;

    @Override
    public void initialize(final Bootstrap<JavabotConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle());
        bootstrap.addBundle(new AssetsBundle("/assets", "/assets", null, "assets"));
        bootstrap.addBundle(new AssetsBundle("/META-INF/resources/webjars", "/webjars", null, "webjars"));
    }

    @Override
    public void run(final JavabotConfiguration configuration, final Environment environment) throws Exception {
        environment.getApplicationContext().setSessionsEnabled(true);
        environment.getApplicationContext().setSessionHandler(new SessionHandler());

        PublicOAuthResource oauth = injector.getInstance(PublicOAuthResource.class);
        oauth.setConfiguration(configuration);
        environment.jersey().register(oauth);

        environment.jersey().register(injector.getInstance(BotResource.class));
        environment.jersey().register(injector.getInstance(AdminResource.class));
        environment.jersey().register(new RuntimeExceptionMapper(injector, configuration));
        environment.jersey().register(new RestrictedProvider());

        environment.servlets().addFilter("html", new HtmlToResourceFilter())
                   .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "*.html");

        environment.healthChecks().register("javabot", new JavabotHealthCheck());
    }

    public static void main(String[] args) throws Exception {
        Guice.createInjector(new JavabotModule())
             .getInstance(JavabotApplication.class)
             .run(new String[]{"server", "javabot.yml"});
    }

    private static class HtmlToResourceFilter implements Filter {
        @Override
        public void init(final FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
            String replace = ((HttpServletRequest) request).getRequestURI().replace(".html", "");
            request.getRequestDispatcher(replace).forward(request, response);
        }

        @Override
        public void destroy() {

        }
    }
}
