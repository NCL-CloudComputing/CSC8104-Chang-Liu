package uk.ac.newcastle.enterprisemiddleware.travelAgent;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelService;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-14 16:09
 **/

@Path("/TravelAgent")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TravelAgentRestService {


    @Inject
    Logger logger;

    @Inject
    TravelAgentService travelAgentService;

    @Inject
    @RestClient
    HotelBookingService hotelBookingService;

    @Inject
    @RestClient
    TaxiBookingService taxiBookingService;

    @Inject
    @RestClient
    FlightBookingService flightBookingService;


    /**
     * @description: Add a new TravelAgent to the database
     * @Param travelAgent:
     * @return javax.ws.rs.core.Response
     * @author Chang Liu
     * @create 2023/11/14
     */

    @POST
    @Operation(description = "Add a new TravelAgent to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "TravelAgent created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid TravelAgent supplied in request body"),
            @APIResponse(responseCode = "409", description = "TravelAgent supplied in request body conflicts with an existing Customer"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response TravelAgent(
            @Parameter(description = "JSON representation of TravelAgent object to be added to the database", required = true)
            TravelAgent travelAgent) throws Exception {
        Response.ResponseBuilder builder;

        if (travelAgent == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Long hotelBookingId =null;
        Long taxiBookingId = null;
        Long flightBookingId=null;

        try {

             hotelBookingId = travelAgentService.creatHotelBooking(travelAgent);
            taxiBookingId = travelAgentService.creatTaxiBooking(travelAgent);
             flightBookingId = travelAgentService.creatFlightBooking(travelAgent);
            System.out.println("----------------"+hotelBookingId+"----------------------------");
            travelAgent.setHotelBookingId(hotelBookingId);
            travelAgent.setTaxiBookingId(taxiBookingId);
            travelAgent.setFlightBookingId(flightBookingId);
            TravelAgent travelAgent1 = travelAgentService.createTravelAgent(travelAgent);

            builder = Response.status(Response.Status.CREATED).entity(travelAgent1);

        } catch (Exception e) {
            hotelBookingService.deleteBooking(hotelBookingId);
            taxiBookingService.deleteBooking(taxiBookingId);
            flightBookingService.deleteBooking(flightBookingId);
            throw new RestServiceException(e);
        }

        logger.info("create travelAgent completed. TravelAgent = " + travelAgent);
        return builder.build();
    }
    /**
     * @description: Delete a TravelAgent from the database
     * @Param id
     * @return javax.ws.rs.core.Response
     * @author Chang Liu
     * @create 2023/11/14
     */

    @DELETE
    @Path("/{id:[0-9]+}")
    @Operation(description = "Delete a TravelAgent from the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The TravelAgent has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid TravelAgent id supplied"),
            @APIResponse(responseCode = "404", description = "TravelAgent with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteTravelAgent(
            @Parameter(description = "Id of TravelAgent to be deleted", required = true)
            @Schema(minimum = "0")
            @PathParam("id")
            Long id) {


        Response.ResponseBuilder builder;
        if (id == null || travelAgentService.findTravelAgentById(id) == null) {
            throw new RestServiceException("No TravelAgent with the id " + id + " was found!", Response.Status.NOT_FOUND);

        }
        try {
            travelAgentService.deleteTravelAgent(id);

            hotelBookingService.deleteBooking(travelAgentService.findTravelAgentById(id).getHotelBookingId());
            taxiBookingService.deleteBooking(travelAgentService.findTravelAgentById(id).getTaxiBookingId());
            flightBookingService.deleteBooking(travelAgentService.findTravelAgentById(id).getFlightBookingId());
            builder = Response.noContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.info("delete TravelAgent completed. TravelAgent = " + id);
        return builder.build();
    }

    /**
     * @description: Fetch all TravelAgent
     * @return javax.ws.rs.core.Response
     * @author Chang Liu
     * @create 2023/11/14
     */

    @GET
    @Operation(summary = "Fetch all TravelAgent", description = "Returns a JSON array of all stored TravelAgent objects.")
    public Response retrieveAllTravelAgent() {
        //Create an empty collection to contain the intersection of Customers to be returned
        List<TravelAgent> travelAgents;

        travelAgents = travelAgentService.findAllTravelAgent();

        return Response.ok(travelAgents).build();

    }



}
