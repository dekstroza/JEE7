package com.github.dekstroza.hopsfactory.customerservice;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.dekstroza.hopsfactory.customerservice.domain.Customer;
import com.jayway.restassured.response.ExtractableResponse;

@SuppressWarnings("ALL")
@RunWith(Arquillian.class)
public class CustomerEndpointIT {

    private static final String CUSTOMER_CREATE_URL = "http://localhost:8080/customer_service/customer";

    @Test
    public void testCreateValidCustomer__RETURNS_201() {
        Customer customer = new Customer("deki", "deki", "b@cc.com", "secretpassword");
        ExtractableResponse response = given().body(customer).contentType(JSON).when().post(CUSTOMER_CREATE_URL).then().assertThat().statusCode(201)
                .and().contentType(JSON).extract();

        assertThat(response.as(Customer.class), notNullValue());
        assertThat(response.as(Customer.class).getId(), notNullValue());
        assertThat(response.as(Customer.class).getFirstname(), equalTo(customer.getFirstname()));
        assertThat(response.as(Customer.class).getLastname(), equalTo(customer.getLastname()));
        assertThat(response.as(Customer.class).getEmail(), equalTo(customer.getEmail()));
        assertThat(response.as(Customer.class).getPassword(), equalTo(customer.getPassword()));
    }

}
