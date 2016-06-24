package io.dekstroza.github.jee7.swarmdemo.app.api;

/**
 * User credentials
 */
public class Credentials {

    private final String username;
    private final String password;

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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
