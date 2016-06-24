package io.dekstroza.github.jee7.swarmdemo.app.services;

import static io.dekstroza.github.jee7.swarmdemo.app.endpoints.ApplicationConstants.*;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

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

    public String authenticateUser(Credentials credentials) throws InvalidCredentialsException {
        try {
            final ApplicationUser applicationUser = applicationUserService.findApplicationUserByCredentials(credentials);
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
}
