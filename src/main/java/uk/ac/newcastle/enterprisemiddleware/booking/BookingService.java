package uk.ac.newcastle.enterprisemiddleware.booking;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelService;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;
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
    List<Booking> findAllBookingsByCustomer(Customer customer){
        return bookingRepository.findAllBookingsByCustomer(customer);
    }
    Booking findById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    Booking findByHotelIdAndBookingDate(Long hotelID, Date bookingDate) {
        return bookingRepository.findByHotelIdAndBookingDate(hotelID, bookingDate);
    }
    Booking findByCustomerId(Long customerId){
        return bookingRepository.findByCustomerId(customerId);
    }
    Booking createBooking(Booking booking) throws Exception {
        return bookingRepository.createBooking(booking);
    }
    Booking updateBooking(Booking booking)throws Exception{
        return bookingRepository.updateBooking(booking);
    }
    Booking deleteBooking(Booking booking) throws Exception {
        return bookingRepository.deleteBooking(booking);
    }

}
