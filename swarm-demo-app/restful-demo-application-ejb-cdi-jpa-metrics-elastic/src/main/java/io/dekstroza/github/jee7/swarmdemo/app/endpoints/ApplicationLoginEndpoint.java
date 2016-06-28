package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationConstants.*;
import static javax.ws.rs.core.Response.Status.*;
import static javax.ws.rs.core.Response.status;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import io.dekstroza.github.jee7.swarmdemo.app.ProfilingInterceptor;
import io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationUser;
import io.dekstroza.github.jee7.swarmdemo.app.api.Credentials;
import io.dekstroza.github.jee7.swarmdemo.app.api.InvalidCredentialsException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Stateless
@Path("v1.0.0")
public class ApplicationLoginEndpoint {

    @PersistenceContext
    private EntityManager em;

    @PermitAll
    @Path("login")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Interceptors(ProfilingInterceptor.class)
    @Asynchronous
    public void applicationLogin(@QueryParam(USERNAME) final String username, @QueryParam(PASSWORD) final String password,
                                 final @Suspended AsyncResponse response) {
        final Credentials credentials = new Credentials(username, password);
        try {
            final String JWToken = authenticateUser(credentials);
            response.resume(status(OK).header(AUTHORIZATION, JWToken).build());
        } catch (final InvalidCredentialsException ie) {
            response.resume(status(BAD_REQUEST).entity(ie.getMessage()).build());
        } catch (final Exception e) {
            response.resume(status(INTERNAL_SERVER_ERROR).build());
        }

    }

    public String authenticateUser(Credentials credentials) throws InvalidCredentialsException {
        try {
            final ApplicationUser applicationUser = findApplicationUserByCredentials(credentials);
            return createLoginToken(credentials);
        } catch (final NoResultException nre) {
            throw new InvalidCredentialsException("Invalid username or password");
        } catch (final Exception e) {
            throw new InvalidCredentialsException(e.getMessage());
        }

    }

    String createLoginToken(final Credentials credentials) {
        final Date now = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, 1);

        final String jwtToken = Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(credentials.getUsername()).setIssuedAt(now)
                .setIssuer(ISSUER).setExpiration(cal.getTime()).signWith(SignatureAlgorithm.HS512, SUPER_SECRET_KEY).compact();
        return new StringBuilder(BEARER).append(jwtToken).toString();
    }

    ApplicationUser findApplicationUserByCredentials(Credentials credentials) {
        return em.createQuery("SELECT au FROM ApplicationUser au WHERE au.username = :username AND au.password = :password", ApplicationUser.class)
                .setParameter(USERNAME, credentials.getUsername()).setParameter(PASSWORD, credentials.getPassword()).getSingleResult();
    }

}
