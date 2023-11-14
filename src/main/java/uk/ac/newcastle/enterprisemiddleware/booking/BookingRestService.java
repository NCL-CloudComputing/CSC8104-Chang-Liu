package uk.ac.newcastle.enterprisemiddleware.booking;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.reactive.Cache;
import uk.ac.newcastle.enterprisemiddleware.area.InvalidAreaCodeException;
import uk.ac.newcastle.enterprisemiddleware.contact.Contact;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelService;
import uk.ac.newcastle.enterprisemiddleware.travelAgent.BookingVO;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.http.HttpRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-12 14:30
 **/

@Path("/booking")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookingRestService {
    @Inject
    @Named("logger")
    Logger logger;

    @Inject
    BookingRepository bookingRepository;

    @Inject
    BookingService bookingService;

    @Inject
    CustomerService customerService;

    @Inject
    HotelService hotelService;

/**
 * @description: find all bookings information
 * @return javax.ws.rs.core.Response
 * @author Chang Liu
 * @create 2023/11/13
 */

    @GET
    @Operation(summary = "Fetch all Bookings", description = "Returns a JSON array of all stored Contact objects.")
    public Response retrieveAllContacts() {
        //Create an empty collection to contain the intersection of Contacts to be returned
        List<Booking> bookingList;

        bookingList=bookingService.findAllBookings();

        return Response.ok(bookingList).build();
    }
    /**
     * @description:通过customerId找这个人的预定信息list
     * @Param id:
     * @return javax.ws.rs.core.Response
     * @author Chang Liu
     * @create 2023/11/12
     */
    @GET
    @Cache
    @Path("/customer/{id:[0-9]+}")
    @Operation(
            summary = "Fetch a Booking by id",
            description = "Returns a JSON representation of the Booking object with the provided id."
    )
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description ="Booking found"),
            @APIResponse(responseCode = "404", description = "Booking with id not found")
    })
    public Response retrieveAllBookingsBycid(
            @Parameter(description = "Id of Customer to be fetched")
            @Schema(minimum = "0", required = true)
            @PathParam("id")
            long id){
        List<Booking> bookingList;
        Customer customer = customerService.findById(id);

        if(customer==null)
        {
            throw new RestServiceException("No Customer with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }
        bookingList = bookingService.findAllBookingsByCustomer(customer);

        if(bookingList==null)
        {
            throw new RestServiceException("No Booking with the Customer id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        return Response.ok(bookingList).build();

    }
    /**
     * @description:通过bookingid找预定信息
     * @Param id
     * @Param request:
     * @return javax.ws.rs.core.Response
     * @author Chang Liu
     * @create 2023/11/12
     */

    @GET
    @Cache
    @Path("/{id:[0-9]+}")
    @Operation(
            summary = "Fetch a Booking by id",
            description = "Returns a JSON representation of the Customer object with the provided id."
    )
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description ="Booking found"),
            @APIResponse(responseCode = "404", description = "Booking with id not found")
    })
    public Response retrieveBookingById(
            @Parameter(description = "Id of Booking to be fetched")
            @Schema(minimum = "0", required = true)
            @PathParam("id")
            long id, HttpRequest request){


        Booking booking = bookingService.findById(id);

        if (booking == null) {
            // Verify that the customer exists. Return 404, if not present.
            throw new RestServiceException("No Booking with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }
        logger.info("findById " + id + ": found Booking = " + booking);

        return Response.ok(booking).build();
    }

    /**
     * @description：新增预定信息
     * @Param
     * @return javax.ws.rs.core.Response
     * @author Chang Liu
     * @create 2023/11/12
     */

    @SuppressWarnings("unused")
    @POST
    @Operation(description = "Add a new Booking to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Booking created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid Booking supplied in request body"),
            @APIResponse(responseCode = "409", description = "Booking supplied in request body conflicts with an existing Booking"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createBooking(@Parameter(description = "JSON representation of Booking object to be added to the database", required = true) BookingVO booking) {

        if (booking == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {
            // Clear the ID if accidentally set
            booking.setId(null);

            // Go add the new Booking.
            bookingService.createBooking(booking);

            // Create a "Resource Created" 201 Response and pass the Booking back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(booking);


        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        } catch (UniqueBookingException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("booking", "That booking is already exist");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        logger.info("creatBooking completed. Booking = " + booking);
        return builder.build();
    }
    /**
     * @description
     * @Param id:
     * @Param Booking:
     * @return javax.ws.rs.core.Response
     * @author Chang Liu
     * @create 2023/11/12
     */

    @PUT
    @Path("/{bookingId:[0-9]+}")
    @Operation(description = "Update a Booking in the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Booking updated successfully"),
            @APIResponse(responseCode = "400", description = "Invalid Booking supplied in request body"),
            @APIResponse(responseCode = "404", description = "Booking with id not found"),
            @APIResponse(responseCode = "409", description = "Booking details supplied in request body conflict with another existing Booking"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response updateBooking(
            @Parameter(description=  "Id of Booking to be updated", required = true)
            @Schema(minimum = "0")
            @PathParam("bookingId")
            long bookingId,
            @Parameter(description = "JSON representation of Booking object to be updated in the database", required = true)
            Booking booking) {

        if (booking == null || booking.getBookingId() == null) {
            throw new RestServiceException("Invalid Booking supplied in request body", Response.Status.BAD_REQUEST);
        }

        if (booking.getBookingId() != null && booking.getBookingId() !=bookingId) {
            // The client attempted to update the read-only bookingId. This is not permitted.
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("id", "The Booking ID in the request body must match that of the Booking being updated");
            throw new RestServiceException("Booking details supplied in request body conflict with another Booking",
                    responseObj, Response.Status.CONFLICT);
        }

        if (bookingService.findById(booking.getBookingId()) == null) {
            // Verify that the booking exists. Return 404, if not present.
            throw new RestServiceException("No Booking with the id " + bookingId + " was found!", Response.Status.NOT_FOUND);
        }

        Response.ResponseBuilder builder;

        try {
            // Apply the changes the Booking.
            bookingService.updateBooking(booking);

            // Create an OK Response and pass the booking back in case it is needed.
            builder = Response.ok(booking);


        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);
        } catch (UniqueBookingException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("booking", "That booking is already exist");
            throw new RestServiceException("Booking details supplied in request body conflict with another Booking",
                    responseObj, Response.Status.CONFLICT, e);
        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        logger.info("updateBooking completed. Booking = " + booking);
        return builder.build();
    }
    /**
     * @description
     * @Param id:
     * @return javax.ws.rs.core.Response
     * @author Chang Liu
     * @create 2023/11/12
     */

    @DELETE
    @Path("/{bookingId:[0-9]+}")
    @Operation(description = "Delete a Booking from the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The booking has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid Booking id supplied"),
            @APIResponse(responseCode = "404", description = "Booking with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteBooking(
            @Parameter(description = "Id of Booking to be deleted", required = true)
            @Schema(minimum = "0")
            @PathParam("bookingId")
            Long bookingId) {

        Response.ResponseBuilder builder;

        if (bookingId == null||bookingService.findById(bookingId)==null) {
            // Verify that the Booking exists. Return 404, if not present.
            throw new RestServiceException("No Booking with the id " + bookingId + " was found!", Response.Status.NOT_FOUND);
        }

        try {
            bookingService.deleteBooking(bookingId);

            builder = Response.noContent();

        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }
        logger.info("deleteBooking completed. Booking = " + bookingId);
        return builder.build();
    }



}
