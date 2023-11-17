package uk.ac.newcastle.enterprisemiddleware.guestbooking;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import uk.ac.newcastle.enterprisemiddleware.booking.Booking;
import uk.ac.newcastle.enterprisemiddleware.booking.BookingRestService;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelService;
import uk.ac.newcastle.enterprisemiddleware.travelAgent.BookingVO;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(BookingRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
public class GuestBookingRestServiceTest {


    private static Customer customer;
    private static Hotel hotel;

    private static BookingVO bookingVO;
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
    static void setupBooking() {


        bookingVO = new BookingVO();

        bookingVO.setCustomerId(1L);
        bookingVO.setHotelId(1L);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,2024);
        calendar.set(Calendar.MONTH,12);
        calendar.set(Calendar.DAY_OF_MONTH,17);
        Date bookingDate = calendar.getTime();
        bookingVO.setBookingDate(bookingDate);
    }


    @Test
    @Transactional
    @Order(1)
    public void testCreateCustomer() throws Exception {
        customerService.createCustomer(customer);

    }

    @Test
    @Transactional
    @Order(2)
    public void testCreateHotel() throws Exception {
        hotelService.createHotel(hotel);
    }
    @Test
    @Order(3)
    void createGuestBooking() {
        given().
                when().
                contentType(ContentType.JSON).
                body(bookingVO).
                post().
                then().
                statusCode(201);
    }
    @Test
    @Order(4)
    public void deleteBooking() {
        Response response = when()
                .get("/customer/1")
                .then()
                .statusCode(200)
                .extract().response();
        Booking[] bookings = response.body().as(Booking[].class);

        when().
                delete(bookings[0].getBookingId().toString()).
                then().
                statusCode(204);
    }



}