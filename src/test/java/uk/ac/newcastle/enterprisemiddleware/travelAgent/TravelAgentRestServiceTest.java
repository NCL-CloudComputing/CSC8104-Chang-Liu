package uk.ac.newcastle.enterprisemiddleware.travelAgent;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import uk.ac.newcastle.enterprisemiddleware.booking.Booking;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelService;

import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.Calendar;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(TravelAgentRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
public class TravelAgentRestServiceTest {
    private static Customer customer;
    private static Hotel hotel;
    private static TravelAgent travelAgent;
    @Inject
    CustomerService customerService;

    @Inject
    HotelService hotelService;


    @BeforeAll
    static void setupCustomer() {
        customer = new Customer();
        customer.setFirstName("Chang");
        customer.setLastName("Liu");
        customer.setCustomerEmail("lcunique@hotmail.com");
        customer.setCustomerTel("07436427311");
    }
    @BeforeAll
    static void setupHotel() {
        hotel = new Hotel();
        hotel.setHotelName("HOTEL");
        hotel.setHotelTel("07436427311");
        hotel.setPostcode("NE45SU");
    }
    @BeforeAll
    static void setupTravelAgent()
    {
        travelAgent = new TravelAgent();
        travelAgent.setHotelid(1L);
        travelAgent.setTaxiId(1L);
        travelAgent.setFlightId(1L);
        travelAgent.setCustomerId(1L);
        travelAgent.setBookingDate(new Date(2028,12,11));

    }
    @Test
    @Transactional
    @Order(1)
    public void testCreateCustomer() throws Exception {
        Customer customer1 = customerService.createCustomer(customer);
        System.out.println(customer1);
    }
    @Test
    @Transactional
    @Order(2)
    public void testCreateHotel() throws Exception {
        Hotel hotel1 = hotelService.createHotel(hotel);
        System.out.println(hotel1);

    }
    @Test
    @Order(3)
    public void testCreateTravelAgent() {
        given().
                when().
                contentType(ContentType.JSON).
                body(travelAgent).
                post().
                then().
                statusCode(201);
    }


    @Test
    @Order(4)
    void travelAgent() {
        Response response = when().
                get().
                then().
                statusCode(200).
                extract().response();

        TravelAgent[] result = response.body().as(TravelAgent[].class);
        travelAgent.setId(result[0].getId());
        System.out.println(result[0]);

        assertEquals(1, result.length);
        assertTrue(travelAgent.getFlightId().equals(result[0].getFlightId()), "The flight id is not equal");
        assertTrue(travelAgent.getTaxiId().equals(result[0].getTaxiId()), "The taxi id is not equal");
        assertTrue(travelAgent.getHotelid().equals(result[0].getHotelid()), "The hotel id is not equal");
        assertTrue(travelAgent.getCustomerId().equals(result[0].getCustomerId()), "The customer id is not equal");
        assertTrue(travelAgent.getBookingDate().equals(result[0].getBookingDate()), "The booking date is not equal");


    }
    @Test
    @Order(5)
    void retrieveAllTravelAgent() {
        Response response = when().
                get().
                then().
                statusCode(200).
                extract().response();

        TravelAgent[] travelAgents = response.body().as(TravelAgent[].class);

        assertTrue(travelAgent.getFlightId().equals(travelAgents[0].getFlightId()), "Booking Hotel is not equals");
        //assertTrue(customer.equals(bookings[0].getCustomer()), "Customer is not equals");
        //assertEquals(bookingVo.getBookingDate().equals(bookings[0].getBookingDate()), true, "Customer Date is not equals");


    }
    @Test
    @Order(6)
    void deleteTravelAgent() {
        Response response = when()
                .get()
                .then()
                .statusCode(200)
                .extract().response();
        TravelAgent[] travelAgents = response.body().as(TravelAgent[].class);

                when().
                delete(travelAgents[0].getId().toString()).
                then().
                statusCode(204);
    }


}