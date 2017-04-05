package testenv;

import javax.enterprise.context.ApplicationScoped;

import io.dekstroza.github.jee7.swarmdemo.app.RegistrationServiceConfiguration;

@ApplicationScoped
public class ArquillianRegistrationServiceConfigurationImpl implements RegistrationServiceConfiguration {

    private String tokenServiceURL = "http://localhost:8080/api/v1.0.0";
    private String userServiceURL = "http://localhost:8080/api/v1.0.0";

    private String httpProxyHost = null;
    private String httpProxyPort = null;

    @Override
    public String getTokenServiceURL() {
        return tokenServiceURL;
    }

    @Override
    public void setTokenServiceURL(String tokenServiceURL) {
        this.tokenServiceURL = tokenServiceURL;
    }

    @Override
    public String getUserServiceURL() {
        return this.userServiceURL;
    }

    @Override
    public void setUserServiceURL(String userServiceURL) {
        this.userServiceURL = userServiceURL;
    }

    @Override
    public String getHttpProxyHost() {
        return this.httpProxyHost;
    }

    @Override
    public void setHttpProxyHost(String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    @Override
    public String getHttpProxyPort() {
        return this.httpProxyPort;
    }

    @Override
    public void setHttpProxyPort(String httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }
}
