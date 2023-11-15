//package uk.ac.newcastle.enterprisemiddleware.travelAgent;
//
//import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//@Path("/booking")
//@RegisterRestClient(configKey = "FlightBooking-api")
//public interface FlightBookingService {
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    Response createBooking(BookingVO bookingVO);
//
//    @DELETE
//    @Path("/{id:[0-9]+}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    Response deleteBooking(@PathParam("id") Long id);
//}
