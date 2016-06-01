package io.dekstroza.github.jee7.swarmdemo.app.services.impl;

import static io.dekstroza.github.jee7.swarmdemo.app.endpoints.ApplicationConstants.SUPER_SECRET_KEY;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationUser;
import io.dekstroza.github.jee7.swarmdemo.app.api.Credentials;
import io.dekstroza.github.jee7.swarmdemo.app.api.InvalidCredentialsException;
import io.dekstroza.github.jee7.swarmdemo.app.services.ApplicationUserService;
import io.dekstroza.github.jee7.swarmdemo.app.services.AuthenticationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Authentication service implementation
 */
@Stateless
@Local(AuthenticationService.class)
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @EJB
    private ApplicationUserService applicationUserService;

    @Override
    public String authenticateUser(Credentials credentials) throws InvalidCredentialsException {
        logger.trace("Authentication user with credentials=[{}]", credentials);
        //verify user exists
        try {
            final ApplicationUser applicationUser = applicationUserService.findApplicationUserByCredentials(credentials);
            return createLoginToken(credentials);
        } catch (final NoResultException nre) {
            throw new InvalidCredentialsException("Invalid username or password");
        } catch (final Exception e) {
            if (logger.isTraceEnabled()) {
                logger.error("Error looking up application user:", e);
            }
            throw new InvalidCredentialsException(e.getMessage());
        }

    }

    String createLoginToken(final Credentials credentials) {
        final Date now = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, 1);

        final String jwtToken = Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(credentials.getUsername()).setIssuedAt(now)
                .setIssuer("https://dekstroza.io").setExpiration(cal.getTime()).signWith(SignatureAlgorithm.HS512, SUPER_SECRET_KEY).compact();
        return new StringBuilder("Bearer ").append(jwtToken).toString();
    }
}
