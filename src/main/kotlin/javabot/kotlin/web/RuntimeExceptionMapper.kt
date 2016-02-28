package javabot.kotlin.web

import com.sun.jersey.api.core.HttpContext
import javabot.kotlin.web.resources.PublicErrorResource
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.URISyntaxException
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider class RuntimeExceptionMapper(configuration: JavabotConfiguration) : ExceptionMapper<RuntimeException> {

    @Context
    private val httpContext: HttpContext? = null

    private val configuration: JavabotConfiguration

    init {
        this.configuration = configuration
    }

    override fun toResponse(runtime: RuntimeException): Response {

        if (runtime is WebApplicationException) {
            return handleWebApplicationException(runtime)
        } else {
            log.error(runtime.message, runtime)
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(PublicErrorResource().view500()).build()
        }
    }

    private fun handleWebApplicationException(exception: RuntimeException): Response {
        val webAppException = exception as WebApplicationException

        // No logging
        val status = webAppException.response.status
        if (status == Status.UNAUTHORIZED.statusCode) {
            try {
                return Response.status(Status.TEMPORARY_REDIRECT).location(URI("/auth/login")).build()
            } catch (e: URISyntaxException) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(PublicErrorResource().view500()).build()
            }

        } else if (status == Status.FORBIDDEN.statusCode) {
            return Response.status(Status.FORBIDDEN).build()
            //                       .entity(new PublicErrorResource().view403())
        } else if (status == Status.NOT_FOUND.statusCode) {
            return Response.status(Status.NOT_FOUND).build()

            //                       .entity(new PublicErrorResource().view404())
        } else {
            log.error(exception.message, exception)
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(PublicErrorResource().view500()).build()
        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(RuntimeExceptionMapper::class.java)
    }

}
