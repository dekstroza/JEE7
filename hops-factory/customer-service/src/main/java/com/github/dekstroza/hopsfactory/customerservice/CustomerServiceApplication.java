package com.github.dekstroza.hopsfactory.customerservice;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/customer_service")
public class CustomerServiceApplication extends Application {

    public static final String APPLICATION_CUSTOMER_SERVICE_V1_JSON = "application/customer.service.v1+json";
}
