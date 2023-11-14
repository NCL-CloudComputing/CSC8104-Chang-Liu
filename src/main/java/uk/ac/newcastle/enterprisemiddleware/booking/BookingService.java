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
import uk.ac.newcastle.enterprisemiddleware.travelAgent.BookingVO;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

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
//@Path("/hotelBooking")
//@RegisterRestClient(configKey = "hotelBooking-api")
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

    public List<Booking> findAllBookings(){
        return bookingRepository.findAllBookings();
    }
    /**
     * <p>Returns a List of all Booking objects, sorted alphabetically by customerId.<p/>
     *
     * @return List of Contact objects
     */
    public List<Booking> findAllBookingsByCustomer(Customer customer){
        return bookingRepository.findAllBookingsByCustomer(customer);
    }
    /**
     * <p>Returns a single Booking object, specified by a Long bookingId.<p/>
     *
     * @param bookingId The id field of the Contact to be returned
     * @return The Contact with the specified id
     */
    public Booking findById(Long bookingId) {
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

    public Booking findByHotelIdAndBookingDate(Hotel hotel, Date bookingDate) {
        return bookingRepository.findByHotelIdAndBookingDate(hotel, bookingDate);
    }

    /**
     * @description: Booking By customerId
     * @Param customerId:
     * @return uk.ac.newcastle.enterprisemiddleware.booking.Booking
     * @author Chang Liu
     * @create 2023/11/11
     */

    public Booking findByCustomerId(Long customerId){
        return bookingRepository.findByCustomerId(customerId);
    }

    /**
     * @description: Writes the provided Booking object to the application database
     * @Param booking:
     * @return uk.ac.newcastle.enterprisemiddleware.booking.Booking
     * @author Chang Liu
     * @create 2023/11/12
     */
    public BookingVO createBooking(BookingVO booking) throws Exception {
        log.info("HotelBookingService.createBooking() - Creating " + booking.getHotelId() + " " + booking.getCustomerId());

        Long customerId = booking.getCustomerId();
        Customer customer = customerService.findById(customerId);

        Long hotelId=booking.getHotelId();
        Hotel hotel=hotelService.findById(hotelId);
        Date bookingDate = booking.getBookingDate();
        Booking booking1=new Booking();
        booking1.setBookingDate(bookingDate);
        booking1.setHotel(hotel);
        booking1.setCustomer(customer);
        System.out.println(booking1);


        // Check to make sure the data fits with the parameters in the Booking model and passes validation.
        bookingValidator.validateBooking(booking1);

        Booking booking2 = bookingRepository.createBooking(booking1);

        BookingVO bookingVO = new BookingVO(booking2);



        // Write the contact to the database.
        return bookingVO;
    }
    /**
     * @description
     * @Param booking
     * @return uk.ac.newcastle.enterprisemiddleware.booking.Booking
     * @author Chang Liu
     * @create 2023/11/12
     */

    public Booking updateBooking(Booking booking)throws Exception{
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

    public Booking deleteBooking(Long bookingId) throws Exception {
        log.info("delete() - Deleting " + bookingId);

        Booking booking = bookingRepository.findById(bookingId);
        System.out.println(booking);
        if (booking == null) {
            throw new RestServiceException("No booking with ID" + bookingId + "found");
        }

        return bookingRepository.deleteBooking(booking);
    }

}
