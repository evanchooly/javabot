package javabot.web;

import com.google.inject.Injector;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import javabot.web.resources.PublicErrorResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.net.URI;
import java.net.URISyntaxException;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Logger log = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

    @Context
    private HttpContext httpContext;
    private Injector injector;

    private JavabotConfiguration configuration;

    public RuntimeExceptionMapper(final Injector injector, final JavabotConfiguration configuration) {
        this.injector = injector;
        this.configuration = configuration;
    }

    @Override
    public Response toResponse(RuntimeException runtime) {

        if (runtime instanceof WebApplicationException) {
            return handleWebApplicationException(runtime);
        } else {
            log.error(runtime.getMessage(), runtime);
            HttpRequestContext request = httpContext.getRequest();
            return Response
                       .status(Status.INTERNAL_SERVER_ERROR)
                       .entity(new PublicErrorResource().view500())
                       .build();

        }
    }

    private Response handleWebApplicationException(RuntimeException exception) {
        WebApplicationException webAppException = (WebApplicationException) exception;

        // No logging
        int status = webAppException.getResponse().getStatus();
        if (status == Response.Status.UNAUTHORIZED.getStatusCode()) {
            try {
                return Response
                           .status(Status.TEMPORARY_REDIRECT)
                           .location(new URI("/auth/login"))
                           .build();
            } catch (URISyntaxException e) {
                return Response
                           .status(Status.INTERNAL_SERVER_ERROR)
                           .entity(new PublicErrorResource().view500())
                           .build();
            }
        } else if (status == Status.FORBIDDEN.getStatusCode()) {
            return Response
                       .status(Response.Status.FORBIDDEN)
                            //                       .entity(new PublicErrorResource().view403())
                       .build();
        } else if (status == Status.NOT_FOUND.getStatusCode()) {
            return Response
                       .status(Response.Status.NOT_FOUND)
                            //                       .entity(new PublicErrorResource().view404())
                       .build();
        } else {
            log.error(exception.getMessage(), exception);
            return Response
                       .status(Status.INTERNAL_SERVER_ERROR)
                       .entity(new PublicErrorResource().view500())
                       .build();
        }
    }

}
