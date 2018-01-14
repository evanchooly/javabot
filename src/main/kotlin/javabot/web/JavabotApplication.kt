package javabot.web

import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Singleton
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.views.ViewBundle
import javabot.IrcAdapter
import javabot.Javabot
import javabot.JavabotConfig
import javabot.JavabotModule
import javabot.OfflineAdapter
import javabot.dao.ApiDao
import javabot.web.auth.RestrictedProvider
import javabot.web.resources.AdminResource
import javabot.web.resources.BotResource
import javabot.web.resources.PublicOAuthResource
import org.eclipse.jetty.server.session.SessionHandler
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.util.EnumSet
import javax.servlet.DispatcherType
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Singleton
class JavabotApplication @Inject constructor(var injector: Injector): Application<JavabotConfiguration>() {
    var running = false

    companion object {
        private val LOG = LoggerFactory.getLogger(JavabotApplication::class.java)

        @Throws(Exception::class)
        @JvmStatic fun main(args: Array<String>) {
            Guice.createInjector(JavabotModule())
                    .getInstance(JavabotApplication::class.java)
                    .run(arrayOf("server", "javabot.yml"))
        }
    }

    override fun initialize(bootstrap: Bootstrap<JavabotConfiguration>) {
        bootstrap.addBundle(ViewBundle())
        bootstrap.addBundle(AssetsBundle("/assets", "/assets", null, "assets"))
        bootstrap.addBundle(AssetsBundle("/META-INF/resources/webjars", "/webjars", null, "webjars"))
    }

    override fun run(configuration: JavabotConfiguration, environment: Environment) {
        environment.applicationContext.isSessionsEnabled = true
        environment.applicationContext.sessionHandler = SessionHandler()

        val bot = injector.getInstance(Javabot::class.java)
        bot.setUpThreads()

        val oauth = injector.getInstance(PublicOAuthResource::class.java)
        oauth.configuration = configuration
        environment.jersey().register(oauth)

        environment.jersey().register(injector.getInstance(BotResource::class.java))
        environment.jersey().register(injector.getInstance(AdminResource::class.java))
        environment.jersey().register(RuntimeExceptionMapper(configuration))
        environment.jersey().register(RestrictedProvider())

        environment.servlets()
                .addFilter("javadoc", injector.getInstance(JavadocFilter::class.java))
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType::class.java), false, "/javadoc/*")

        environment.healthChecks().register("javabot", JavabotHealthCheck())

        running = true
    }

    class JavadocFilter @Inject constructor(var apiDao: ApiDao, var config: JavabotConfig) : Filter {
        override fun destroy() {
        }

        override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
            request as HttpServletRequest
            var filePath = request.requestURI.split("/").drop(2).joinToString("/")
            if(!filePath.startsWith("/")) {
                filePath = "/" + filePath
            }
            val path = File("javadoc$filePath").toPath()

            if (Files.exists(path)) {
                response.outputStream.use { stream ->
                    Files.copy(path, stream)
                    stream.flush()
                }
            } else {
                (response as HttpServletResponse).sendError(404)
            }
        }

        override fun init(filterConfig: FilterConfig?) {
        }
    }
}