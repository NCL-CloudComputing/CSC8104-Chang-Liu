package uk.ac.newcastle.enterprisemiddleware.customer;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @description
 * @author Chang Liu
 * @create 2023/11/17
 */

@QuarkusTest
@TestHTTPEndpoint(CustomerRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
public class CustomerRestServiceTest {
    private static Customer customer;

    @BeforeAll
    static void setup() {
        customer = new Customer();
        customer.setFirstName("Chang");
        customer.setLastName("Liu");
        customer.setCustomerEmail("lcunique@hotmail.com");
        customer.setCustomerTel("07436427311");
    }

    @Test
    @Order(2)
    public void retrieveAllCustomers() {
        Response response = when().
                get().
                then().
                statusCode(200).
                extract().response();

        Customer[] customers = response.body().as(Customer[].class);

        System.out.println(customers);
        System.out.println(customer);
        assertTrue(customer.getFirstName().equals(customers[0].getFirstName()), "CustomerName is not equals");
        assertTrue(customer.getLastName().equals(customers[0].getLastName()), "CustomerName is not equal");
        assertTrue(customer.getCustomerEmail().equals(customers[0].getCustomerEmail()), "Email is not equal");
        assertTrue(customer.getCustomerTel().equals(customers[0].getCustomerTel()), "Phone number is not equal");

    }


    @Test
    @Order(1)
    public void createCustomer() {
        given().
                contentType(ContentType.JSON).body(customer).
                when()
                .post()
                .then().
                statusCode(201);

    }

    @Test
    @Order(3)
    void deleteCustomer() {
        Response response = when().
                get().
                then().
                statusCode(200).
                extract().response();
        Customer[] result = response.body().as(Customer[].class);

        when().
                delete(result[0].getCustomerId().toString()).
                then().
                statusCode(204);
    }
}