package com.github.dekstroza.hopsfactory.orderservice;

import static com.github.dekstroza.hopsfactory.orderservice.Constants.ORDER_SERVICE_BASE_URL;
import static com.jayway.restassured.RestAssured.given;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jayway.restassured.http.ContentType;

@RunWith(Arquillian.class)
public class HealthzEndpointIT {

    private static final String HEALTHZ_URL = ORDER_SERVICE_BASE_URL + "/healthz";

    @Test
    public void testHealthCheck__RETURNS_200() {
        given().get(HEALTHZ_URL).then().assertThat().statusCode(200).and().contentType(ContentType.TEXT);
    }
}
