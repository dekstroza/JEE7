package com.github.dekstroza.hopsfactory.customerservice;

import static com.github.dekstroza.hopsfactory.customerservice.endpoints.CustomerEndpoint.*;
import static com.github.dekstroza.hopsfactory.customerservice.util.ValidationMessages.*;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.dekstroza.hopsfactory.customerservice.util.ValidationMessages;

@RunWith(Arquillian.class)
public class CustomerEndpointIT {

    public static final String CUSTOMER_CREATE_URL = "http://localhost:8080/customer_service/customer";

    @Test
    public void testCreateValidCustomer__RETURNS_201() {
        given().param(FIRSTNAME, "deki").param(LASTNAME, "deki").param(EMAIL, "kdejan@gmail.com").when().put(CUSTOMER_CREATE_URL).then().assertThat()
                .statusCode(201).and().contentType(JSON);
    }

    @Test
    public void testCreateCustomer_VALIDATE_PARAM_PRESENCE_RESPONSE_AND_MESSAGES() {
        given().param(LASTNAME, "deki").param(EMAIL, "kdejan@gmail.com").when().put(CUSTOMER_CREATE_URL).then().assertThat().statusCode(400).and()
                .contentType(JSON).and().body(equalTo(MISSING_FIRSTNAME_MSG));

        given().param(FIRSTNAME, "deki").param(EMAIL, "kdejan@gmail.com").when().put(CUSTOMER_CREATE_URL).then().assertThat().statusCode(400).and()
                .contentType(JSON).and().body(equalTo(MISSING_LASTNAME_MSG));

        given().param(FIRSTNAME, "deki").param(LASTNAME, "deki").when().put(CUSTOMER_CREATE_URL).then().assertThat().statusCode(400).and()
                .contentType(JSON).and().body(equalTo(MISSING_EMAIL_MSG));

    }

    @Test
    public void testCreateCustomer_VALIDATE_PARAM_SIZE_OR_FORMAT_RESPONSE_AND_MESSAGES() {
        given().param(FIRSTNAME, "d").param(LASTNAME, "deki").param(EMAIL, "kdejan@gmail.com").when().put(CUSTOMER_CREATE_URL).then().assertThat()
                .statusCode(400).and().contentType(JSON).and().body(equalTo(FIRSTNAME_INVALID_LENGTH_MSG));

        given().param(FIRSTNAME, "deki").param(LASTNAME, "d").param(EMAIL, "kdejan@gmail.com").when().put(CUSTOMER_CREATE_URL).then().assertThat()
                .statusCode(400).and().contentType(JSON).and().body(equalTo(LASTNAME_INVALID_LENGTH_MSG));

        given().param(FIRSTNAME, "deki").param(LASTNAME, "deki").param(EMAIL, "k@b").when().put(CUSTOMER_CREATE_URL).then().assertThat()
                .statusCode(400).and().contentType(JSON).and().body(equalTo(ValidationMessages.INVALID_EMAIL_FORMAT_MSG));

    }
}
