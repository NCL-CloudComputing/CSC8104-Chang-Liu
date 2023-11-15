package uk.ac.newcastle.enterprisemiddleware.travelAgent;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/bookings")
@RegisterRestClient(configKey = "TaxiBooking-api")
public interface TaxiBookingService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createBooking(BookingVO bookingVO);

    @DELETE
    @Path("/{id:[0-9]+}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response deleteBooking(@PathParam("id") Long id);


}
