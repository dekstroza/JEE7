package com.github.dekstroza.hopsfactory.orderservice;

import com.github.dekstroza.hopsfactory.orderservice.domain.Order;
import com.jayway.restassured.response.ExtractableResponse;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.UUID;

import static com.github.dekstroza.hopsfactory.orderservice.Constants.ORDER_SERVICE_BASE_URL;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderEndpointIT {

    private static final String ORDER_SERVICE_URL = ORDER_SERVICE_BASE_URL + "/order";

    @Test
    public void createValidOrder() {
        Order order = new Order(UUID.randomUUID(), UUID.randomUUID(), 100, 1000, "NEW_ORDER");
        ExtractableResponse response = given().body(order).contentType(JSON).post(ORDER_SERVICE_URL).then().extract();
        System.out.println("response = " + response.body().asString());
        assertThat(response.statusCode(), Matchers.is(201));
        assertThat(response.contentType(), Matchers.is(JSON.toString()));
        assertThat(response.as(Order.class), notNullValue());
        assertThat(response.as(Order.class).getId(), notNullValue());
        assertThat(response.as(Order.class).getCustomerId(), equalTo(order.getCustomerId()));
        assertThat(response.as(Order.class).getInventoryId(), equalTo(order.getInventoryId()));
        assertThat(response.as(Order.class).getOrderDate(), notNullValue());
        assertThat(response.as(Order.class).getPrice(), equalTo(order.getPrice()));
        assertThat(response.as(Order.class).getQuantity(), equalTo(order.getQuantity()));
        assertThat(response.as(Order.class).getStatus(), equalTo(order.getStatus()));

    }
}
