package javabot.web

import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.File
import java.nio.file.Files
import javabot.Javabot
import javabot.JavabotConfig
import javabot.dao.ApiDao
import kotlin.jvm.java
import org.slf4j.LoggerFactory

@Singleton
class JavabotApplication @Inject constructor(/*var injector: Injector*/ ) {
    var running = false

    companion object {
        private val LOG = LoggerFactory.getLogger(JavabotApplication::class.java)

        @Throws(Exception::class) @JvmStatic fun main(args: Array<String>) {}
    }

    fun run(configuration: JavabotConfiguration) {
        val bot: Javabot? = null // = injector.getInstance(Javabot::class.java)
        bot?.setUpThreads()

        //        environment
        //            .servlets()
        //            .addFilter("javadoc", injector.getInstance(JavadocFilter::class.java))
        //            .addMappingForUrlPatterns(
        //                EnumSet.allOf(DispatcherType::class.java),
        //                false,
        //                "/javadoc/*"
        //            )

        running = false
    }

    class JavadocFilter @Inject constructor(var apiDao: ApiDao, var config: JavabotConfig) :
        Filter {
        override fun destroy() {}

        override fun doFilter(
            request: ServletRequest,
            response: ServletResponse,
            chain: FilterChain
        ) {
            request as HttpServletRequest
            var filePath = request.requestURI.split("/").drop(2).joinToString("/")
            if (!filePath.startsWith("/")) {
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

        override fun init(filterConfig: FilterConfig?) {}
    }
}
