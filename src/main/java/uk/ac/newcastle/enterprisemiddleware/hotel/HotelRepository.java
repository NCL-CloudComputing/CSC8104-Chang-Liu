package uk.ac.newcastle.enterprisemiddleware.hotel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
