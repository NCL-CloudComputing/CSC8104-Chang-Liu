package uk.ac.newcastle.enterprisemiddleware.hotel;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
@TestHTTPEndpoint(HotelRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
class HotelRestServiceTest {
    private static Hotel hotel;
    @BeforeAll
    static void setup() {
        hotel = new Hotel();
        hotel.setHotelName("HOTEL");
        hotel.setHotelTel("07436427311");
        hotel.setPostcode("NE45SU");
    }

    @Test
    @Order(2)
    void retrieveAllHotels() {
        Response response = when().
                get().
                then().
                statusCode(200).
                extract().response();

        Hotel[] hotels = response.body().as(Hotel[].class);

        System.out.println(hotels);
        System.out.println(hotel);
        assertTrue(hotel.getHotelName().equals(hotels[0].getHotelName()), "Name is not equals");
        assertTrue(hotel.getHotelTel().equals(hotels[0].getHotelTel()), "Tel is not equal");
        assertTrue(hotel.getPostcode().equals(hotels[0].getPostcode()), "Postcode is not equal");

    }

    @Test
    @Order(1)
    void createHotel() {
        given().
                contentType(ContentType.JSON).body(hotel).
                when()
                .post()
                .then().
                statusCode(201);
    }

    @Test
    void deleteHotel() {
        Response response = when().
                get().
                then().
                statusCode(200).
                extract().response();
        Hotel[] hotels = response.body().as(Hotel[].class);

        when().
                delete(hotels[0].getHotelId().toString()).
                then().
                statusCode(204);
    }
}