package com.github.dekstroza.hopsfactory.inventoryservice;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/inventory_service")
public class InventoryServiceApplication extends Application {

    public static final String APPLICATION_INVENTORY_SERVICE_V1_JSON = "application/inventory.service.v1+json";
}
