package com.github.dekstroza.hopsfactory.supplierservice;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/supplier_service")
public class SupplierServiceApplication extends Application {

    public static final String APPLICATION_SUPPLIER_SERVICE_V1_JSON = "application/supplier.service.v1+json";
}
