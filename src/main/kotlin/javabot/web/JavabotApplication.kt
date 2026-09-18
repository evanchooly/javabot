package javabot.web

import com.google.inject.Injector
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.ext.Provider
import java.io.File
import java.nio.file.Files
import javabot.Javabot
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.LoggerFactory

@ApplicationScoped
class JavabotApplication @Inject constructor(var injector: Injector) {
    var running = false

    @ConfigProperty(name = "javabot.web.enabled", defaultValue = "false")
    var webEnabled: Boolean = false

    companion object {
        private val LOG = LoggerFactory.getLogger(JavabotApplication::class.java)
    }

    fun onStart(@Observes ev: StartupEvent) {
        if (webEnabled) {
            LOG.info("Starting Javabot web application")
            injector.getInstance(Javabot::class.java).start()
            running = true
        } else {
            LOG.info("Javabot web application is disabled")
        }
    }

    @Provider
    @ApplicationScoped
    class JavadocFilter : ContainerRequestFilter {

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
                        requestContext.abortWith(
                            jakarta.ws.rs.core.Response.ok(javadocPath.toFile()).build()
                        )
                    } catch (_: Exception) {
                        requestContext.abortWith(jakarta.ws.rs.core.Response.status(500).build())
                    }
                } else {
                    requestContext.abortWith(jakarta.ws.rs.core.Response.status(404).build())
                }
            }
        }
    }
}
