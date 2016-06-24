package io.dekstroza.github.jee7.swarmdemo.app.services;

import static io.dekstroza.github.jee7.swarmdemo.app.endpoints.ApplicationConstants.*;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationUser;
import io.dekstroza.github.jee7.swarmdemo.app.api.Credentials;
import io.dekstroza.github.jee7.swarmdemo.app.api.InvalidCredentialsException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Authentication service implementation
 */
@Stateless
@LocalBean
public class AuthenticationService {

    @EJB
    private ApplicationUserService applicationUserService;

    public String parallelAuthenticateUser(Credentials credentials) throws InvalidCredentialsException, ExecutionException, InterruptedException {
        CompletableFuture<ApplicationUser> appUser = applicationUserService.findApplicationUserByCredentials(credentials);
        CompletableFuture<String> jwtTok = createLoginToken(credentials);
        return jwtTok.get();

    }

    CompletableFuture<String> createLoginToken(final Credentials credentials) {
        return new CompletableFuture<String>().supplyAsync(() -> {
            final Date now = new Date();
            final Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.HOUR_OF_DAY, 1);

            final String jwtToken = Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(credentials.getUsername()).setIssuedAt(now)
                       .setIssuer(ISSUER).setExpiration(cal.getTime()).signWith(SignatureAlgorithm.HS512, SUPER_SECRET_KEY).compact();
            return new StringBuilder(BEARER).append(jwtToken).toString();
        });
    }
}
