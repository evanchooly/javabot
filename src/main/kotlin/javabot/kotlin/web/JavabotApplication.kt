package javabot.kotlin.web

import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Singleton
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.views.ViewBundle
import javabot.JavabotModule
import javabot.kotlin.web.auth.RestrictedProvider
import javabot.kotlin.web.resources.AdminResource
import javabot.kotlin.web.resources.BotResource
import javabot.kotlin.web.resources.PublicOAuthResource
import org.eclipse.jetty.server.session.SessionHandler
import java.io.IOException
import java.util.EnumSet
import javax.servlet.DispatcherType
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Singleton
class JavabotApplication : Application<JavabotConfiguration>() {

    companion object {

        @Throws(Exception::class)
        @JvmStatic public fun main(args: Array<String>) {
            Guice.createInjector(JavabotModule()).getInstance(JavabotApplication::class.java).run(arrayOf("server", "javabot.yml"))
        }
    }

    @Inject
    private lateinit val injector: Injector

    override fun initialize(bootstrap: Bootstrap<JavabotConfiguration>) {
        bootstrap.addBundle(ViewBundle())
        bootstrap.addBundle(AssetsBundle("/assets", "/assets", null, "assets"))
        bootstrap.addBundle(AssetsBundle("/META-INF/resources/webjars", "/webjars", null, "webjars"))
    }

    @Throws(Exception::class)
    override fun run(configuration: JavabotConfiguration, environment: Environment) {
        environment.applicationContext.isSessionsEnabled = true
        environment.applicationContext.sessionHandler = SessionHandler()

        val oauth = injector.getInstance(PublicOAuthResource::class.java)
        oauth.configuration = configuration
        environment.jersey().register(oauth)

        environment.jersey().register(injector.getInstance(BotResource::class.java))
        environment.jersey().register(injector.getInstance(AdminResource::class.java))
        environment.jersey().register(RuntimeExceptionMapper(configuration))
        environment.jersey().register(RestrictedProvider())

        environment.servlets().addFilter("html", HtmlToResourceFilter()).addMappingForUrlPatterns(EnumSet.allOf(DispatcherType::class.java),
              false, "*.html")

        environment.healthChecks().register("javabot", JavabotHealthCheck())
    }
    private class HtmlToResourceFilter : Filter {

        @Throws(ServletException::class)
        override fun init(filterConfig: FilterConfig) {

        }

        @Throws(IOException::class, ServletException::class)
        override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
            val replace = (request as HttpServletRequest).requestURI.replace(".html", "")
            request.getRequestDispatcher(replace).forward(request, response)
        }
        override fun destroy() {

        }

    }
}
