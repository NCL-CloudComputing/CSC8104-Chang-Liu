package uk.ac.newcastle.enterprisemiddleware.hotel;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.reactive.Cache;
import uk.ac.newcastle.enterprisemiddleware.area.InvalidAreaCodeException;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-10 18:41
 **/
@Path("/hotels")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HotelRestService {

    @Inject
    @Named("logger")
    Logger logger;

    @Inject
    HotelRepository hotelRepository;

    @Inject
    HotelService hotelService;

    @Inject
    CustomerService customerService;

    @GET
    @Operation(summary = "Fetch all Hotels", description = "Returns a JSON array of all stored Hotel objects.")
    public Response retrieveAllHotels(@QueryParam("name") String name) {
        //Create an empty collection to contain the intersection of Hotels to be returned
        List<Hotel> hotels= hotelService.findAllOrderedByHotelName();
        if(name == null){
            hotels=hotelService.findAllOrderedByHotelName();
        }
        else{
            hotels=hotelService.findAllByName(name);
        }
        return Response.ok(hotels).build();
    }
    @GET
    @Cache
    @Path("/tel")
    @Operation(
            summary = "Fetch a Hotel by HotelTel",
            description = "Returns a JSON representation of the Hotel object with the provided telephone number."
    )
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description ="Hotel found"),
            @APIResponse(responseCode = "404", description = "Hotel with telephone number not found")
    })
    public Response retrieveHotelByTel(
            @Parameter(description = "Telephone number of Hotel to be fetched", required = true)
            @QueryParam("hotelTel") String hotelTel)
    {
        Hotel hotel;
        try {
            hotel = hotelService.findByHotelTel(hotelTel);
        } catch (NoResultException e) {
            // Verify that the hotel exists. Return 404, if not present.
            throw new RestServiceException("No Customer with the telephone number " + hotelTel + " was found!", Response.Status.NOT_FOUND);
        }
        return Response.ok(hotel).build();
    }
    @GET
    @Cache
    @Path("/{hotelId:[0-9]+}")
    @Operation(
            summary = "Fetch a Hotel by id",
            description = "Returns a JSON representation of the Hotel object with the provided id."
    )
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description ="Hotel found"),
            @APIResponse(responseCode = "404", description = "Hotel with id not found")
    })
    public Response retrieveHotelById(
            @Parameter(description = "Id of Hotel to be fetched")
            @Schema(minimum = "0", required = true)
            @PathParam("hotelId")
            long hotelId) {

        Hotel hotel = hotelService.findById(hotelId);
        if (hotel == null) {
            // Verify that the contact exists. Return 404, if not present.
            throw new RestServiceException("No Hotel with the id " + hotelId + " was found!", Response.Status.NOT_FOUND);
        }
        logger.info("findById " + hotelId + ": found Hotel = " + hotel);

        return Response.ok(hotel).build();
    }
    @POST
    @Operation(description = "Add a new Hotel to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Hotel created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid Hotel supplied in request body"),
            @APIResponse(responseCode = "409", description = "Hotel supplied in request body conflicts with an existing Hotel"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createHotel(
            @Parameter(description = "JSON representation of Hotel object to be added to the database", required = true)
            Hotel hotel) {

        if (hotel == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {
            // Clear the ID if accidentally set
            hotel.setHotelId(null);

            // Go add the new Hotel.
            hotelService.createHotel(hotel);

            // Create a "Resource Created" 201 Response and pass the contact back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(hotel);


        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        } catch (UniqueHotelTelException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("hotelTel", "That hotel telephone number is already used, please use a unique phoneNumber");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
         } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        logger.info("createHotel completed. Hotel = " + hotel);
        return builder.build();
    }

    /**
     * @description
     * @Param id:
     * @Param hotel:
     * @return javax.ws.rs.core.Response
     * @author Chang Liu
     * @create 2023/11/10
     */
    @PUT
    @Path("/{hotelId:[0-9]+}")
    @Operation(description = "Update a Hotel in the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Hotel updated successfully"),
            @APIResponse(responseCode = "400", description = "Invalid Hotel supplied in request body"),
            @APIResponse(responseCode = "404", description = "Hotel with id not found"),
            @APIResponse(responseCode = "409", description = "Hotel details supplied in request body conflict with another existing Contact"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response updateHotel(
            @Parameter(description=  "Id of Hotel to be updated", required = true)
            @Schema(minimum = "0")
            @PathParam("hotelId")
            long hotelId,
            @Parameter(description = "JSON representation of Hotel object to be updated in the database", required = true)
            Hotel hotel) {

        if (hotel == null || hotel.getHotelId() == null) {
            throw new RestServiceException("Invalid Contact supplied in request body", Response.Status.BAD_REQUEST);
        }

        if (hotel.getHotelId() != null && hotel.getHotelId() != hotelId) {
            // The client attempted to update the read-only hotelId. This is not permitted.
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("id", "The hotel ID in the request body must match that of the Hotel being updated");
            throw new RestServiceException("Hotel details supplied in request body conflict with another Hotel",
                    responseObj, Response.Status.CONFLICT);
        }

        if (hotelService.findById(hotel.getHotelId()) == null) {
            // Verify that the contact exists. Return 404, if not present.
            throw new RestServiceException("No Hotel with the id " + hotelId + " was found!", Response.Status.NOT_FOUND);
        }

        Response.ResponseBuilder builder;

        try {
            // Apply the changes the Contact.
            hotelService.updateHotel(hotel);

            // Create an OK Response and pass the contact back in case it is needed.
            builder = Response.ok(hotel);

        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);
        } catch (UniqueHotelTelException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("hotelTel", "That hotel telephone number is already used, please use a unique phoneNumber");
            throw new RestServiceException("telephone number details supplied in request body conflict with another telephone number",
                    responseObj, Response.Status.CONFLICT, e);
        }  catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        logger.info("updateHotel completed. Contact = " + hotel);
        return builder.build();
    }

    @DELETE
    @Path("/{hotelId:[0-9]+}")
    @Operation(description = "Delete a Hotel from the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The hotel has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid Hotel id supplied"),
            @APIResponse(responseCode = "404", description = "Hotel with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteHotel(
            @Parameter(description = "Id of Contact to be deleted", required = true)
            @Schema(minimum = "0")
            @PathParam("hotelId")
            long hotelId) {

        Response.ResponseBuilder builder;

        Hotel hotel = hotelService.findById(hotelId);

        if (hotel == null) {
            // Verify that the contact exists. Return 404, if not present.
            throw new RestServiceException("No Hotel with the id " + hotelId + " was found!", Response.Status.NOT_FOUND);
        }

        try {
            hotelService.deleteHotel(hotel);

            builder = Response.noContent();

        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }
        logger.info("deleteHotel completed. Hotel = " + hotel);
        return builder.build();
    }

}
