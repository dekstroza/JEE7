package com.github.dekstroza.hopsfactory.orderservice;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/order_service")
public class OrderServiceApplication extends Application {

    public static final String APPLICATION_ORDER_SERVICE_V1_JSON = "application/order.service.v1+json";
}
