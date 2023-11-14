package uk.ac.newcastle.enterprisemiddleware.travelAgent;

import io.quarkus.logging.Log;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import uk.ac.newcastle.enterprisemiddleware.area.Area;
import uk.ac.newcastle.enterprisemiddleware.area.InvalidAreaCodeException;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelService;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

import static io.vertx.codegen.CodeGenProcessor.log;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-13 16:57
 **/
@Dependent
public class TravelAgentService {

    @Inject
    Logger logger;

    @Inject
    TravelAgentRepository travelAgentRepository;

    @Inject
    HotelService hotelService;

    @Inject
    CustomerService customerService;

    @Inject
    @RestClient
    HotelBookingService hotelBookingService;
/**
 * @description find TravelAgent by Id
 * @Param id:
 * @return uk.ac.newcastle.enterprisemiddleware.travelAgent.TravelAgent
 * @author Chang Liu
 * @create 2023/11/13
 */

    public TravelAgent findTravelAgentById(Long id){
        return  travelAgentRepository.findTravelAgentById(id);
    }

    /**
     * @description: find all travelagent information.
     * @return java.util.List<uk.ac.newcastle.enterprisemiddleware.travelAgent.TravelAgent>
     * @author Chang Liu
     * @create 2023/11/13
     */

    public List<TravelAgent> findAllTravelAgent() {
        return travelAgentRepository.findAllTravelAgent();
    }

    /**
     * @description
     * @Param travelAgent:
     * @return uk.ac.newcastle.enterprisemiddleware.travelAgent.TravelAgent
     * @author Chang Liu
     * @create 2023/11/13
     */


    public TravelAgent createTravelAgent(TravelAgent travelAgent) throws Exception{
        logger.info("TravelAgentService.create() - Creating" + travelAgent.getId());
        return travelAgentRepository.createTravelAgent(travelAgent);
    }

/**
 * @description
 * @Param travelAgent:
 * @return uk.ac.newcastle.enterprisemiddleware.travelAgent.TravelAgent
 * @author Chang Liu
 * @create 2023/11/14
 */

    public TravelAgent deleteTravelAgent(Long id) throws Exception {
        logger.info("deleteTravelAgent() - Deleting ");

        if(travelAgentRepository.findTravelAgentById(id)==null){
            throw new RestServiceException("No TravelAgent ID"+id+"was found");
        }
        return travelAgentRepository.findTravelAgentById(id);

    }

    public Long creatHotelBooking(TravelAgent travelAgent) {

        BookingVO bookingVO = new BookingVO();
        bookingVO.setHotelId(hotelService.findById(travelAgent.getHotelid()).getHotelId());
        bookingVO.setCustomerId(customerService.findById(travelAgent.getCustomerId()).getCustomerId());
        bookingVO.setBookingDate(travelAgent.getBookingDate());
        Long id ;

        try {
            Response response = hotelBookingService.createBooking(bookingVO);
            id = response.readEntity(BookingVO.class).getId();

        } catch (ClientErrorException e) {
            if (e.getResponse().getStatusInfo() == Response.Status.NOT_FOUND) {
                throw new InvalidAreaCodeException("create hotel booking fail", e);
            } else {
                throw e;
            }
        }
        return id;
    }






}
