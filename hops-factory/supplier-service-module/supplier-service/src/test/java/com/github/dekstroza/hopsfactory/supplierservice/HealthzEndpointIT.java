package com.github.dekstroza.hopsfactory.supplierservice;

import static com.github.dekstroza.hopsfactory.supplierservice.Constants.SUPPLIER_SERVICE_BASE_URL;
import static com.jayway.restassured.RestAssured.given;

import org.junit.Test;

import com.jayway.restassured.http.ContentType;

@SuppressWarnings("ALL")
public class HealthzEndpointIT {

    private static final String HEALTHZ_URL = SUPPLIER_SERVICE_BASE_URL + "/healthz";

    @Test
    public void testHealthCheck__RETURNS_200() {
        given().get(HEALTHZ_URL).then().assertThat().statusCode(200).and().contentType(ContentType.TEXT);
    }
}
