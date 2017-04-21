package com.github.dekstroza.hopsfactory.orderservice;

import static com.github.dekstroza.hopsfactory.orderservice.Constants.ORDER_SERVICE_BASE_URL;
import static com.github.dekstroza.hopsfactory.orderservice.domain.Order.ORDER_STATES.NEW_ORDER;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Date;
import java.util.UUID;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.dekstroza.hopsfactory.orderservice.domain.Order;
import com.jayway.restassured.response.ExtractableResponse;

@RunWith(Arquillian.class)
public class OrderEndpointIT {

    private static final String ORDER_SERVICE_URL = ORDER_SERVICE_BASE_URL + "/order";

    @Test
    public void createValidOrder() {
        Order order = new Order(UUID.randomUUID(), UUID.randomUUID(), 100, 1000, NEW_ORDER, new Date());
        ExtractableResponse response = given().body(order).contentType(JSON).when().post(ORDER_SERVICE_URL).then().assertThat().statusCode(201).and()
                .contentType(JSON).extract();

        assertThat(response.as(Order.class), notNullValue());
        assertThat(response.as(Order.class).getId(), notNullValue());
        assertThat(response.as(Order.class).getCustomerId(), equalTo(order.getCustomerId()));
        assertThat(response.as(Order.class).getInventoryId(), equalTo(order.getInventoryId()));
        assertThat(response.as(Order.class).getOrderDate(), equalTo(order.getOrderDate()));
        assertThat(response.as(Order.class).getPrice(), equalTo(order.getPrice()));
        assertThat(response.as(Order.class).getQuantity(), equalTo(order.getQuantity()));
        assertThat(response.as(Order.class).getStatus(), equalTo(order.getStatus()));

    }
}
