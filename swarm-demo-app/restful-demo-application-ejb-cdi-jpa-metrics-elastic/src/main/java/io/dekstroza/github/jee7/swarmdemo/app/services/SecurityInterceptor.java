package io.dekstroza.github.jee7.swarmdemo.app.services;

import static io.dekstroza.github.jee7.swarmdemo.app.endpoints.ApplicationConstants.SUPER_SECRET_KEY;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.interception.PostMatchContainerRequestContext;

import io.jsonwebtoken.Jwts;

@Provider
@ServerInterceptor
public class SecurityInterceptor implements ContainerRequestFilter {

    public void filter(ContainerRequestContext requestContext) throws IOException {

        final PostMatchContainerRequestContext pmContext = (PostMatchContainerRequestContext) requestContext;
        final Method method = pmContext.getResourceMethod().getMethod();

        //Access allowed for all
        if (method.isAnnotationPresent(PermitAll.class)) {
            return;
        }
        //Access denied for all
        if (method.isAnnotationPresent(DenyAll.class)) {
            requestContext.abortWith(Response.status(FORBIDDEN).build());
        }
        final List<String> authHeaders = requestContext.getHeaders().get("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            if (authHeaders.get(0) != null && authHeaders.get(0).startsWith("Bearer")) {
                String jwtToken = authHeaders.get(0).substring(7);
                try {

                    Jwts.parser().setSigningKey(SUPER_SECRET_KEY).parseClaimsJws(jwtToken);
                    //OK, we can trust this JWT
                } catch (Exception e) {
                    //don't trust the JWT!
                    requestContext.abortWith(Response.status(FORBIDDEN).build());
                }
            } else {
                requestContext.abortWith(Response.status(FORBIDDEN).build());
            }

        } else {
            requestContext.abortWith(Response.status(FORBIDDEN).build());
        }

    }
}
