package uk.ac.newcastle.enterprisemiddleware.guestbooking;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import uk.ac.newcastle.enterprisemiddleware.booking.Booking;
import uk.ac.newcastle.enterprisemiddleware.booking.BookingService;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-13 15:45
 **/
@Path("/guestBooking")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
public class GuestBookingRestService {
    @Inject
    CustomerService customerService;

    @Inject
    BookingService bookingService;

    @Inject
    UserTransaction userTransaction;

    @Inject
    Logger logger;

    /**
     * @description
     * @Param guestBooking:
     * @return javax.ws.rs.core.Response
     * @author Chang Liu
     * @create 2023/11/13
     */

    @POST
    @Operation(description = "Add a new GuestBooking to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "GuestBooking created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid Booking supplied in request body"),
            @APIResponse(responseCode = "409", description = "Booking supplied in request body conflicts with an existing Booking"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    public Response createGuestBooking(@Parameter(description = "JSON representation of GuestBooking object to be added to the database", required = true)
           GuestBooking guestBooking) throws Exception {


        Response builder;
        if (guestBooking.getBooking() == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        try {

            userTransaction.begin();

            Customer customer = guestBooking.getCustomer();
            Booking booking = guestBooking.getBooking();
            customerService.createCustomer(customer);
            bookingService.createBooking(booking);
            userTransaction.commit();

            builder =  Response.status(Response.Status.CREATED).entity("Create GustBooking successfully!").build();

        } catch (Exception e) {
            try {
                userTransaction.rollback();
            }catch (Exception ce)
            {
                logger.info("rollback fail"+ce.getMessage());
            }
            logger.info("The transaction fail"+e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity("Build Fail!").build();
        }
        return builder;
        }
}
