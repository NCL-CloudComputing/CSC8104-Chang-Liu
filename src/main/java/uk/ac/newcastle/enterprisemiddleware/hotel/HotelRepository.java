package uk.ac.newcastle.enterprisemiddleware.hotel;

import uk.ac.newcastle.enterprisemiddleware.customer.Customer;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-10 17:36
 **/
@RequestScoped
public class HotelRepository {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    List<Hotel> findAllByHotelName()
    {
        Query namedQuery = em.createNamedQuery(Hotel.FIND_ALL,Hotel.class);
        return namedQuery.getResultList();
    }


    List<Hotel> findAllByName(String name){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Hotel> criteria = cb.createQuery(Hotel.class);
        Root<Hotel> hotel = criteria.from(Hotel.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new feature in JPA 2.0.
        // criteria.select(contact).where(cb.equal(contact.get(Contact_.firstName), firstName));
        criteria.select(hotel).where(cb.equal(hotel.get("hotelName"), name));
        return em.createQuery(criteria).getResultList();
    }
    Hotel findById(Long hotelId)
    {
        return em.find(Hotel.class,hotelId);
    }
    Hotel findByHotelTel(String hotelTel)
    {
        TypedQuery<Hotel> hotelTels = em.createNamedQuery(Hotel.FIND_BY_TEL, Hotel.class).setParameter("hotelTel", hotelTel);

        return hotelTels.getSingleResult();
    }

    Hotel createHotel(Hotel hotel) throws Exception{
        log.info("HotelRepository.create() - Creating"+hotel.getHotelName());

        em.persist(hotel);
//        em.getTransaction().commit();
        return hotel;
    }
    Hotel updateHotel(Hotel hotel) throws Exception{
        log.info("HotelRepository.update() - Updating"+hotel.getHotelName());

        em.merge(hotel);
        return hotel;
    }
    Hotel deleteHotel(Hotel hotel) throws Exception {
        log.info("HotelRepository.deleteHotel() - Deleting " + hotel.getHotelName());

        if (hotel.getHotelId() != null) {

            em.remove(em.merge(hotel));

        } else {
            log.info("HotelRepository.delete() - No ID was found so can't Delete.");
        }

        return hotel;
    }


}
