package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static io.dekstroza.github.jee7.swarmdemo.app.endpoints.ApplicationConstants.SUPER_SECRET_KEY;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Path("v1.0.0")
public class ApplicationLoginEndpoint {


    @PermitAll
    @Path("login")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response applicationLogin(@QueryParam("username") final String username, @QueryParam("password") final String password) {

        final String jwtToken = createLoginToken(username, password);
        return Response.status(OK).header("Authorization", jwtToken).build();
    }

    public String createLoginToken(final String username, final String password) {
        final Date now = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, 1);

        final String jwtToken = Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(username).setIssuedAt(now)
                .setIssuer("https://dekstroza.io").setExpiration(cal.getTime()).signWith(SignatureAlgorithm.HS512, SUPER_SECRET_KEY).compact();
        return new StringBuilder("Bearer ").append(jwtToken).toString();
    }
}
