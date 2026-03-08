package javabot.web

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status.FORBIDDEN
import jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR
import jakarta.ws.rs.core.Response.Status.NOT_FOUND
import jakarta.ws.rs.core.Response.Status.TEMPORARY_REDIRECT
import jakarta.ws.rs.core.Response.Status.UNAUTHORIZED
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import java.net.URI
import java.net.URISyntaxException
import javabot.web.views.TemplateService
import org.slf4j.LoggerFactory

@Provider
@ApplicationScoped
class RuntimeExceptionMapper @Inject constructor(private val templateService: TemplateService) :
    ExceptionMapper<RuntimeException> {

    companion object {
        private val LOG = LoggerFactory.getLogger(RuntimeExceptionMapper::class.java)
    }

    override fun toResponse(runtime: RuntimeException): Response {

        if (runtime is WebApplicationException) {
            return handleWebApplicationException(runtime)
        } else {
            LOG.error(runtime.message, runtime)
            return Response.status(INTERNAL_SERVER_ERROR)
                .entity(templateService.createError500View())
                .build()
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
                return Response.status(INTERNAL_SERVER_ERROR)
                    .entity(templateService.createError500View())
                    .build()
            }
        } else if (status == FORBIDDEN.statusCode) {
            return Response.status(INTERNAL_SERVER_ERROR)
                .entity(templateService.createError403View())
                .build()
        } else if (status == NOT_FOUND.statusCode) {
            return Response.status(INTERNAL_SERVER_ERROR)
                .entity(templateService.createError404View())
                .build()
        } else {
            LOG.error(exception.message, exception)
            return Response.status(INTERNAL_SERVER_ERROR)
                .entity(templateService.createError500View())
                .build()
        }
    }
}
