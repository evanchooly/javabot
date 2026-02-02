package javabot.web

import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Singleton
import freemarker.cache.ClassTemplateLoader
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.io.File
import java.nio.file.Files
import javabot.Javabot
import javabot.JavabotConfig
import javabot.JavabotModule
import javabot.dao.ApiDao
import javabot.web.resources.AdminResource
import javabot.web.resources.BotResource
import javabot.web.resources.PublicOAuthResource
import org.slf4j.LoggerFactory

@Singleton
class JavabotApplication @Inject constructor(var injector: Injector) {
    var running = false
    lateinit var server: EmbeddedServer<*, *>

    companion object {
        private val LOG = LoggerFactory.getLogger(JavabotApplication::class.java)

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val application =
                Guice.createInjector(JavabotModule()).getInstance(JavabotApplication::class.java)
            application.run()
        }
    }

    fun run() {
        val configuration = JavabotConfiguration()

        val bot = injector.getInstance(Javabot::class.java)
        bot.setUpThreads()

        server =
            embeddedServer(Netty, port = 8080, host = "0.0.0.0") { configureServer(configuration) }

        running = true
        server.start(wait = true)
    }

    private fun Application.configureServer(configuration: JavabotConfiguration) {
        // Install FreeMarker for templating
        install(FreeMarker) {
            templateLoader = ClassTemplateLoader(this::class.java.classLoader, "/")
        }

        // Install sessions
        install(Sessions) {
            cookie<UserSession>(JavabotConfiguration.SESSION_TOKEN_NAME) {
                cookie.path = "/"
                cookie.maxAgeInSeconds = 86400 * 30
            }
        }

        // Install status pages for error handling
        install(StatusPages) {
            exception<Throwable> { call, cause ->
                LOG.error("Request failed", cause)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Internal Server Error: ${cause.message}",
                )
            }

            status(HttpStatusCode.NotFound) { call, status ->
                call.respondText("404: Page Not Found", status = status)
            }
        }

        // Configure routing
        routing {
            // Static assets using static() which serves files from resources
            static("/assets") {
                resources("assets")
            }
            static("/webjars") {
                resources("META-INF/resources/webjars")
            }

            // Javadoc filter
            get("/javadoc/{...}") {
                val apiDao = injector.getInstance(ApiDao::class.java)
                val config = injector.getInstance(JavabotConfig::class.java)

                val pathAfterJavadoc = call.request.uri.substringAfter("/javadoc/")
                val filePath = "/$pathAfterJavadoc"
                val path = File("javadoc$filePath").toPath()

                if (Files.exists(path)) {
                    call.respondFile(path.toFile())
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            // Health check
            get("/health") { call.respondText("OK", contentType = ContentType.Text.Plain) }

            // Register OAuth routes
            val oauth = injector.getInstance(PublicOAuthResource::class.java)
            oauth.configuration = configuration
            oauth.configureRoutes(this)

            // Register Bot routes
            val botResource = injector.getInstance(BotResource::class.java)
            botResource.configureRoutes(this)

            // Register Admin routes
            val adminResource = injector.getInstance(AdminResource::class.java)
            adminResource.configureRoutes(this)
        }
    }
}

data class UserSession(val sessionToken: String)
