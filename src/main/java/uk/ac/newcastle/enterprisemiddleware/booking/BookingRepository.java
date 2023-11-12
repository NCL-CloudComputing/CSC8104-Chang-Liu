package uk.ac.newcastle.enterprisemiddleware.booking;

import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-10 19:19
 **/

@RequestScoped
public class BookingRepository {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    List<Booking> findAllBookingsByCustomer(Customer customer)
    {
        TypedQuery<Booking> namedQuery = em.createNamedQuery(Booking.FIND_ALL_BY_CUSTOMER, Booking.class)
                .setParameter("customerId",customer.getCustomerId());
        return namedQuery.getResultList();
    }
    Booking findById(Long bookingId){
        return em.find(Booking.class,bookingId);
    }
    Booking findByHotelIdAndBookingDate(Long hotelId, Date bookingDate)
    {
        Query query = em.createNamedQuery(Booking.FIND_BY_HOTEL_AND_BOOKINGDATE,Booking.class)
                .setParameter("hotelId", hotelId)
                .setParameter("bookingDate", bookingDate);
        return  (Booking) query.getSingleResult();
    }

    Booking findByCustomerId(Long customerId){
        Query query = em.createNamedQuery(Booking.FIND_BY_CUSTOMER_ID,Booking.class)
                .setParameter("customerId", customerId);
        return  (Booking) query.getSingleResult();

    }
    Booking createBooking(Booking booking) throws Exception{
        log.info("HotelBookingRepository.create() - Booking"+ " "+booking.getHotelId()+" "+booking.getCustomerId()+" "+booking.getBookingDate());

        em.persist(booking);

        return booking;
    }

    Booking updateBooking(Booking booking) throws Exception{

        log.info("BookRepository.update() - Updating" + booking.getHotel()+" "+booking.getCustomer()+" "+booking.getBookingDate() );
        em.persist(booking);

        return booking;
    }

    Booking deleteBooking(Booking booking) throws Exception {
        log.info("BookingRepository.deleteBooking() - Deleting " + booking.toString());

        if (booking.getBookingId() != null) {

            em.remove(em.merge(booking));

        } else {
            log.info("BookingRepository.delete() - No ID was found so can't Delete.");
        }

        return booking;
    }



}
