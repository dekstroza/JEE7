package io.dekstroza.github.jee7.swarmdemo.app;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class RegistrationServiceConfigurationImpl implements RegistrationServiceConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RegistrationServiceConfigurationImpl.class);

    private String tokenServiceURL;
    private String userServiceURL;

    private String httpProxyHost;
    private String httpProxyPort;

    @PostConstruct
    public void initialize() {
        tokenServiceURL = System.getenv("TOKEN_SERVICE_URL");
        userServiceURL = System.getenv("USER_SERVICE_URL");
        httpProxyHost = System.getenv("HTTP_PROXY_HOST");
        httpProxyPort = System.getenv("HTTP_PROXY_PORT");
        log.info(this.toString());
    }

    @Override
    public String getTokenServiceURL() {
        return tokenServiceURL;
    }

    @Override
    public void setTokenServiceURL(final String tokenServiceURL) {
        this.tokenServiceURL = tokenServiceURL;
    }

    @Override
    public String getUserServiceURL() {
        return userServiceURL;
    }

    @Override
    public void setUserServiceURL(final String userServiceURL) {
        this.userServiceURL = userServiceURL;
    }

    @Override
    public String getHttpProxyHost() {
        return httpProxyHost;
    }

    @Override
    public void setHttpProxyHost(final String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    @Override
    public String getHttpProxyPort() {
        return httpProxyPort;
    }

    @Override
    public void setHttpProxyPort(final String httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    @Override
    public String toString() {
        return "RegistrationServiceConfigurationImpl{" + "tokenServiceURL='" + tokenServiceURL + '\'' + ", userServiceURL='" + userServiceURL + '\''
                + ", httpProxyHost='" + httpProxyHost + '\'' + ", httpProxyPort='" + httpProxyPort + '\'' + '}';
    }
}
