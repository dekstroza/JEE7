package com.github.dekstroza.hopsfactory.inventoryservice;

import static com.github.dekstroza.hopsfactory.inventoryservice.Constants.INVENTORY_SERVICE_BASE_URL;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;

import java.util.UUID;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class InventoryEndpointIT {

    public static final String INVENTORY_SERVICE_URL = INVENTORY_SERVICE_BASE_URL + "/inventory";

    @Test
    public void createValidInventory() {
        given().param("supplierId", UUID.randomUUID()).param("price", 12.99).param("quantity", 1000.22).param("name", "super hops 123")
                .param("description", "Fergus's super special hops").when().put(INVENTORY_SERVICE_URL).then().assertThat().statusCode(201).and()
                .contentType(JSON);

    }
}
