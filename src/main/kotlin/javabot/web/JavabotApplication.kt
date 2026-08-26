package javabot.web

import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.ext.Provider
import java.io.File
import java.nio.file.Files
import javabot.Javabot
import javabot.JavabotConfig
import javabot.JavabotModule
import javabot.dao.ApiDao
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.LoggerFactory

@ApplicationScoped
class JavabotApplication @Inject constructor(var injector: Injector) {
    var running = false

    @ConfigProperty(name = "javabot.web.enabled", defaultValue = "false")
    var webEnabled: Boolean = false

    companion object {
        private val LOG = LoggerFactory.getLogger(JavabotApplication::class.java)

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val injector = Guice.createInjector(JavabotModule())
            // Quarkus will handle the lifecycle, just initialize the bot
            val bot = injector.getInstance(Javabot::class.java)
            bot.setUpThreads()
        }
    }

    fun onStart(@Observes ev: StartupEvent) {
        if (webEnabled) {
            LOG.info("Starting Javabot web application")
            val bot = injector.getInstance(Javabot::class.java)
            bot.setUpThreads()
            running = true
        } else {
            LOG.info("Javabot web application is disabled")
        }
    }

    @Provider
    @ApplicationScoped
    class JavadocFilter @Inject constructor(var apiDao: ApiDao, var config: JavabotConfig) :
        ContainerRequestFilter {

        @Context private lateinit var httpRequest: HttpServletRequest

        @Context private lateinit var httpResponse: HttpServletResponse

        override fun filter(requestContext: ContainerRequestContext) {
            val path = requestContext.uriInfo.path
            if (path.startsWith("/javadoc/")) {
                var filePath = path.split("/").drop(2).joinToString("/")
                if (!filePath.startsWith("/")) {
                    filePath = "/" + filePath
                }
                val javadocPath = File("javadoc$filePath").toPath()

                if (Files.exists(javadocPath)) {
                    try {
                        httpResponse.outputStream.use { stream ->
                            Files.copy(javadocPath, stream)
                            stream.flush()
                        }
                        requestContext.abortWith(jakarta.ws.rs.core.Response.ok().build())
                    } catch (e: Exception) {
                        requestContext.abortWith(jakarta.ws.rs.core.Response.status(500).build())
                    }
                } else {
                    requestContext.abortWith(jakarta.ws.rs.core.Response.status(404).build())
                }
            }
        }
    }
}
