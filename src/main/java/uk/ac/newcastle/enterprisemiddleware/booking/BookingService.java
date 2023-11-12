package uk.ac.newcastle.enterprisemiddleware.booking;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import uk.ac.newcastle.enterprisemiddleware.area.Area;
import uk.ac.newcastle.enterprisemiddleware.area.InvalidAreaCodeException;
import uk.ac.newcastle.enterprisemiddleware.contact.Contact;
import uk.ac.newcastle.enterprisemiddleware.contact.ContactValidator;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelService;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-11 22:44
 **/
@Dependent
@Path("/hotelBooking")
@RegisterRestClient(configKey = "hotelBooking-api")
public class BookingService {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    CustomerService customerService;

    @Inject
    HotelService hotelService;

    @Inject
    BookingRepository bookingRepository;

    @Inject
    BookingValidator bookingValidator;


    /**
     * <p>Returns a List of all Booking objects, sorted alphabetically by customerId.<p/>
     *
     * @return List of Contact objects
     */
    List<Booking> findAllBookingsByCustomer(Customer customer){
        return bookingRepository.findAllBookingsByCustomer(customer);
    }
    /**
     * <p>Returns a single Booking object, specified by a Long bookingId.<p/>
     *
     * @param bookingId The id field of the Contact to be returned
     * @return The Contact with the specified id
     */
    Booking findById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    /**
     * @description:find Booking By hotel and booking Date
     * @Param hotel:
     * @Param bookingDate:
     * @return uk.ac.newcastle.enterprisemiddleware.booking.Booking
     * @author Chang Liu
     * @create 2023/11/10
     */

    Booking findByHotelIdAndBookingDate(Hotel hotel, Date bookingDate) {
        return bookingRepository.findByHotelIdAndBookingDate(hotel, bookingDate);
    }

    /**
     * @description: Booking By customerId
     * @Param customerId:
     * @return uk.ac.newcastle.enterprisemiddleware.booking.Booking
     * @author Chang Liu
     * @create 2023/11/11
     */

    Booking findByCustomerId(Long customerId){
        return bookingRepository.findByCustomerId(customerId);
    }

    /**
     * @description: Writes the provided Booking object to the application database
     * @Param booking:
     * @return uk.ac.newcastle.enterprisemiddleware.booking.Booking
     * @author Chang Liu
     * @create 2023/11/12
     */
    Booking createBooking(Booking booking) throws Exception {
        log.info("HotelBookingService.createBooking() - Creating " + booking.getHotel() + " " + booking.getCustomer());

        // Check to make sure the data fits with the parameters in the Booking model and passes validation.
        bookingValidator.validateBooking(booking);

        // Write the contact to the database.
        return bookingRepository.createBooking(booking);
    }
    /**
     * @description
     * @Param booking
     * @return uk.ac.newcastle.enterprisemiddleware.booking.Booking
     * @author Chang Liu
     * @create 2023/11/12
     */

    Booking updateBooking(Booking booking)throws Exception{
        log.info("HotelBookingService.creatBooking() - Create " + booking.getHotel() + " " + booking.getCustomer());

        // Check to make sure the data fits with the parameters in the Booking model and passes validation.
        bookingValidator.validateBooking(booking);

        return bookingRepository.updateBooking(booking);
    }
    /**
     * @description: delete a Booking information
     * @Param booking
     * @return uk.ac.newcastle.enterprisemiddleware.booking.Booking
     * @author Chang Liu
     * @create 2023/11/12
     */

    Booking deleteBooking(Booking booking) throws Exception {
        log.info("delete() - Deleting " + booking.toString());

        Booking deleteBooking =null;
        if(booking.getBookingId()!=null){
            deleteBooking=bookingRepository.deleteBooking(booking);
        }
        else {
            log.info("delete() - No ID was found so can't Delete.");
        }
        return deleteBooking;
    }

}
