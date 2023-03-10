package javabot.web

import javabot.web.resources.PublicErrorResource
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.URISyntaxException
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status
import javax.ws.rs.core.Response.Status.FORBIDDEN
import javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR
import javax.ws.rs.core.Response.Status.NOT_FOUND
import javax.ws.rs.core.Response.Status.TEMPORARY_REDIRECT
import javax.ws.rs.core.Response.Status.UNAUTHORIZED
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider
import org.apache.http.protocol.HttpContext

@Provider
class RuntimeExceptionMapper(val configuration: JavabotConfiguration) : ExceptionMapper<RuntimeException> {

    companion object {
        private val LOG = LoggerFactory.getLogger(RuntimeExceptionMapper::class.java)
    }

    @Context
    private var httpContext: HttpContext? = null

    override fun toResponse(runtime: RuntimeException): Response {

        if (runtime is WebApplicationException) {
            return handleWebApplicationException(runtime)
        } else {
            LOG.error(runtime.message, runtime)
            return Response.status(INTERNAL_SERVER_ERROR).entity(PublicErrorResource.view500()).build()
        }
    }

    private fun handleWebApplicationException(exception: RuntimeException): Response {
        val webAppException = exception as WebApplicationException

        // No logging
        val status = webAppException.response.status
        if (status == UNAUTHORIZED.statusCode) {
            try {
                return Response.status(TEMPORARY_REDIRECT).location(URI("/auth/login")).build()
            } catch (e: URISyntaxException) {
                return Response.status(INTERNAL_SERVER_ERROR).entity(PublicErrorResource.view500()).build()
            }

        } else if (status == FORBIDDEN.statusCode) {
            return Response.status(INTERNAL_SERVER_ERROR).entity(PublicErrorResource.view403()).build()
        } else if (status == NOT_FOUND.statusCode) {
            return Response.status(INTERNAL_SERVER_ERROR).entity(PublicErrorResource.view404()).build()
        } else {
            LOG.error(exception.message, exception)
            return Response.status(INTERNAL_SERVER_ERROR).entity(PublicErrorResource.view500()).build()
        }
    }

}
