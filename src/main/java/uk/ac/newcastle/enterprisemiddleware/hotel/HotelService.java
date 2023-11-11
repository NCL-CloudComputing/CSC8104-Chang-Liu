package uk.ac.newcastle.enterprisemiddleware.hotel;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.logging.Logger;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-10 18:17
 **/
@Dependent
public class HotelService {

    @Inject
    @Named("logger")
    Logger logger;

    @Inject
    HotelRepository hotelRepository;

    @Inject
    HotelValidator hotelValidator;

    List<Hotel> findAllOrderedByHotelName() {
        return hotelRepository.findAllByHotelName();
    }

    Hotel findById(Long hotelId) {
        return hotelRepository.findById(hotelId);
    }
    Hotel findByHotelTel(String hotelTel){
        return hotelRepository.findByHotelTel(hotelTel);
    }

    Hotel createHotel(Hotel hotel)throws Exception
    {
        logger.info("HotelService.create - Creating"+hotel.getHotelName());

        hotelValidator.validateHotel(hotel);

        return hotelRepository.createHotel(hotel);
    }
    Hotel updateHotel(Hotel hotel) throws Exception
    {
        logger.info("HotelService.update() - updating" + hotel.getHotelName());

        hotelValidator.validateHotel(hotel);

        return hotelRepository.updateHotel(hotel);
    }
    Hotel deleteHotel(Hotel hotel) throws Exception {
        logger.info("deleteHotel() - Deleting " + hotel.toString());

        Hotel deletedHotel = null;

        if (hotel.getHotelId() != null) {
            deletedHotel = hotelRepository.deleteHotel(hotel);
        } else {
            logger.info("deleteHotel() - No ID was found so can't Delete.");
        }

        return deletedHotel;
    }

}
