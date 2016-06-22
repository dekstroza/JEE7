package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static io.dekstroza.github.jee7.swarmdemo.app.endpoints.ApplicationConstants.*;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.status;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Stateless
@Path("v1.0.0")
public class ApplicationLoginEndpoint {

    @PermitAll
    @Path("login")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Asynchronous
    public void applicationLogin(@QueryParam(USERNAME) final String username, @QueryParam(PASSWORD) final String password,
                                 @Suspended final AsyncResponse response) {
        final String jwtToken = createLoginToken(username, password);
        response.resume(status(OK).header(AUTHORIZATION, jwtToken).build());
    }

    String createLoginToken(final String username, final String password) {
        final Date now = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, 1);

        final String jwtToken = Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(username).setIssuedAt(now).setIssuer(ISSUER)
                .setExpiration(cal.getTime()).signWith(SignatureAlgorithm.HS512, SUPER_SECRET_KEY).compact();
        return new StringBuilder(BEARER).append(jwtToken).toString();
    }
}
