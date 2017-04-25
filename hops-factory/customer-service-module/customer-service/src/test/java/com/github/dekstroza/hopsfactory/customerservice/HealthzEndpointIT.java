package com.github.dekstroza.hopsfactory.customerservice;

import com.jayway.restassured.http.ContentType;
import org.junit.Test;

import static com.github.dekstroza.hopsfactory.customerservice.Constants.CUSTOMER_SERVICE_BASE_URL;
import static com.jayway.restassured.RestAssured.given;

public class HealthzEndpointIT {

    private static final String HEALTHZ_URL = CUSTOMER_SERVICE_BASE_URL + "/healthz";

    @Test
    public void testHealthCheck__RETURNS_200() {
        given().get(HEALTHZ_URL).then().assertThat().statusCode(200).and().contentType(ContentType.TEXT);
    }
}
