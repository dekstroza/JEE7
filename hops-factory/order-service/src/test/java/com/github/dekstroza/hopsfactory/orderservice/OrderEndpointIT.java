package com.github.dekstroza.hopsfactory.orderservice;

import static com.github.dekstroza.hopsfactory.orderservice.Constants.ORDER_SERVICE_BASE_URL;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;

import java.util.UUID;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class OrderEndpointIT {

    private static final String ORDER_SERVICE_URL = ORDER_SERVICE_BASE_URL + "/order";

    @Test
    public void createValidOrder() {
        given().param("inventoryId", UUID.randomUUID()).param("customerId", UUID.randomUUID()).param("quantity", 100).when().post(ORDER_SERVICE_URL)
                .then().assertThat().statusCode(201).and().contentType(JSON);

    }
}
