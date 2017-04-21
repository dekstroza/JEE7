package com.github.dekstroza.hopsfactory.supplierservice;

import static com.github.dekstroza.hopsfactory.supplierservice.Constants.SUPPLIER_SERVICE_BASE_URL;
import static com.github.dekstroza.hopsfactory.supplierservice.SupplierServiceApplication.APPLICATION_SUPPLIER_SERVICE_V1_JSON;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.dekstroza.hopsfactory.supplierservice.domain.Supplier;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ExtractableResponse;

@RunWith(Arquillian.class)
public class SupplierEndpointIT {

    private static final String SUPPLIER_SERVICE_URL = SUPPLIER_SERVICE_BASE_URL + "/supplier";

    @Test
    public void createValidSupplier_api_NO_VERSION() {
        Supplier supplier = new Supplier("Guinness Inc", "Dublin", "0871984116", "fergus.a.bartley@guinness.com");
        ExtractableResponse response = given().contentType(JSON).body(supplier).when().post(SUPPLIER_SERVICE_URL).then().assertThat().statusCode(201)
                .and().contentType(JSON).extract();
        given().contentType(JSON).pathParam("id", response.as(Supplier.class).getId().toString()).when().get(SUPPLIER_SERVICE_URL + "/{id}").then()
                .assertThat().statusCode(200).and().contentType(JSON);

    }

    @Test
    public void createValidSupplier_api_V1() {
        Supplier supplier = new Supplier("Guinness Inc", "Dublin", "0871984116", "fergus.a.bartley@guinness.com");
        given().contentType(APPLICATION_SUPPLIER_SERVICE_V1_JSON).body(supplier).when().post(SUPPLIER_SERVICE_URL).then().assertThat().statusCode(201)
                .and().contentType(JSON);
        ExtractableResponse response = given().contentType(APPLICATION_SUPPLIER_SERVICE_V1_JSON).body(supplier).when().post(SUPPLIER_SERVICE_URL)
                .then().assertThat().statusCode(201).and().contentType(JSON).and().extract();
        given().contentType(APPLICATION_SUPPLIER_SERVICE_V1_JSON).pathParam("id", response.as(Supplier.class).getId().toString()).when()
                .get(SUPPLIER_SERVICE_URL + "/{id}").then().assertThat().statusCode(200).and().contentType(JSON);

    }

    @Test
    public void findAllSuppliers_api_NO_VERSION() {
        given().contentType(JSON).when().get(SUPPLIER_SERVICE_URL).then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and()
                .body("isEmpty()", is(false));
    }

    @Test
    public void findAllSuppliers_api_V1() {
        given().contentType(APPLICATION_SUPPLIER_SERVICE_V1_JSON).when().get(SUPPLIER_SERVICE_URL).then().assertThat().statusCode(200).and()
                .contentType(JSON).and().body("isEmpty()", is(false));
    }

}
