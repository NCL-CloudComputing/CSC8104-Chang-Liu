package uk.ac.newcastle.enterprisemiddleware.booking;

import uk.ac.newcastle.enterprisemiddleware.customer.Customer;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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

}
