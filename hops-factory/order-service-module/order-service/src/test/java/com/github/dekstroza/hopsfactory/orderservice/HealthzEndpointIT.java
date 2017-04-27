package com.github.dekstroza.hopsfactory.orderservice;

import static com.github.dekstroza.hopsfactory.orderservice.Constants.ORDER_SERVICE_BASE_URL;
import static com.jayway.restassured.RestAssured.given;

import org.junit.Test;

import com.jayway.restassured.http.ContentType;

public class HealthzEndpointIT {

    private static final String HEALTHZ_URL = ORDER_SERVICE_BASE_URL + "/healthz";

    @Test
    public void testHealthCheck__RETURNS_200() {
        given().get(HEALTHZ_URL).then().assertThat().statusCode(200).and().contentType(ContentType.TEXT);
    }
}
