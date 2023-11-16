//package uk.ac.newcastle.enterprisemiddleware.customer;
//
//import io.quarkus.test.common.QuarkusTestResource;
//import io.quarkus.test.common.http.TestHTTPEndpoint;
//import io.quarkus.test.h2.H2DatabaseTestResource;
//import io.quarkus.test.junit.QuarkusTest;
//import io.restassured.http.ContentType;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//
//import static io.restassured.RestAssured.given;
//import static org.junit.jupiter.api.Assertions.*;
//@QuarkusTest
//@TestHTTPEndpoint(CustomerRestService.class)
//class CustomerRestServiceTest {
//    private static Customer customer;
//
//    @BeforeAll
//    static void setup() {
//        customer = new Customer();
//        customer.setFirstName("Chang");
//        customer.setLastName("Liu");
//        customer.setCustomerEmail("lcunique@hotmail.com");
//        customer.setCustomerTel("07436427311");
//    }
//
//    @Test
//    void retrieveAllCustomers() {
//        given().
//                contentType(ContentType.JSON).
//                body(customer).
//                when()
//                .post().
//                then().
//                statusCode(201);
//    }
//
//
//    }
//
//    @Test
//    void retrieveCustomerByEmail() {
//    }
//
//    @Test
//    void createCustomer() {
//    }
//
//    @Test
//    void updateCustomer() {
//    }
//
//    @Test
//    void deleteCustomer() {
//    }
//}