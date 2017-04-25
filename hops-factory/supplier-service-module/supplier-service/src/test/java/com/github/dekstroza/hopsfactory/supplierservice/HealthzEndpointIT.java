package com.github.dekstroza.hopsfactory.supplierservice;

import static com.github.dekstroza.hopsfactory.supplierservice.Constants.SUPPLIER_SERVICE_BASE_URL;
import static com.jayway.restassured.RestAssured.given;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jayway.restassured.http.ContentType;
import org.wildfly.swarm.arquillian.DefaultDeployment;

@SuppressWarnings("ALL")
public class HealthzEndpointIT {

    private static final String HEALTHZ_URL = SUPPLIER_SERVICE_BASE_URL + "/healthz";

    @Test
    public void testHealthCheck__RETURNS_200() {
        given().get(HEALTHZ_URL).then().assertThat().statusCode(200).and().contentType(ContentType.TEXT);
    }
}
