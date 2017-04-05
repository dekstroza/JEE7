package io.dekstroza.github.jee7.swarmdemo.app;

public interface RegistrationServiceConfiguration {
    String getTokenServiceURL();

    void setTokenServiceURL(final String tokenServiceURL);

    String getUserServiceURL();

    void setUserServiceURL(final String userServiceURL);

    String getHttpProxyHost();

    void setHttpProxyHost(final String httpProxyHost);

    String getHttpProxyPort();

    void setHttpProxyPort(final String httpProxyPort);
}
