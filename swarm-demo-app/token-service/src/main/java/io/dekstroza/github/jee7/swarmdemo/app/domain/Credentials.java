package io.dekstroza.github.jee7.swarmdemo.app.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Credentials {

    public static final String SUPER_SECRET_KEY = "mysupersecretkey";
    public static final String BEARER = "Bearer ";
    public static final String ISSUER = "https://dekstroza.io";
    private final String username;
    private final String password;
    private static final Calendar cal = Calendar.getInstance();

    /**
     * Credentials representation
     *
     * @param username
     *            Username
     * @param password
     *            Password
     */
    public Credentials(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String generateJWToken() {
        final Date now = new Date();
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, 1);
        final String jwtToken = Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(this.username).setIssuedAt(now).setIssuer(ISSUER)
                .setExpiration(cal.getTime()).signWith(SignatureAlgorithm.HS512, SUPER_SECRET_KEY).compact();
        return new StringBuilder(BEARER).append(jwtToken).toString();
    }
}
