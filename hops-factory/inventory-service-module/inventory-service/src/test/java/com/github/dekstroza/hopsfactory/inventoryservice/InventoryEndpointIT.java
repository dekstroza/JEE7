package com.github.dekstroza.hopsfactory.inventoryservice;

import static com.github.dekstroza.hopsfactory.inventoryservice.Constants.INVENTORY_SERVICE_BASE_URL;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.UUID;

import org.junit.Test;

import com.github.dekstroza.hopsfactory.inventoryservice.domain.Inventory;
import com.jayway.restassured.response.ExtractableResponse;

public class InventoryEndpointIT {

    private static final String INVENTORY_SERVICE_URL = INVENTORY_SERVICE_BASE_URL + "/inventory";

    @Test
    public void createValidInventory() {
        Inventory inventory = new Inventory(UUID.randomUUID(), 12.99, 1000.22, "super hops 123", "fergus's hops");
        ExtractableResponse response = given().body(inventory).contentType(JSON).when().post(INVENTORY_SERVICE_URL).then().assertThat()
                .statusCode(201).and().contentType(JSON).extract();
        assertThat(response.as(Inventory.class), notNullValue());
        assertThat(response.as(Inventory.class).getId(), notNullValue());
        assertThat(response.as(Inventory.class).getAvailableQuantity(), equalTo(inventory.getAvailableQuantity()));
        assertThat(response.as(Inventory.class).getDescription(), equalTo(inventory.getDescription()));
        assertThat(response.as(Inventory.class).getName(), equalTo(inventory.getName()));
        assertThat(response.as(Inventory.class).getPrice(), equalTo(inventory.getPrice()));
        assertThat(response.as(Inventory.class).getSupplierId(), equalTo(inventory.getSupplierId()));

    }
}
