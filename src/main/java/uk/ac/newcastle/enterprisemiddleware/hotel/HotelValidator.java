package uk.ac.newcastle.enterprisemiddleware.hotel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-10 18:11
 **/


@ApplicationScoped
public class HotelValidator {

    @Inject
    Validator validator;

    @Inject
    HotelRepository hotelRepository;


    void validateHotel(Hotel hotel) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Hotel>> violations = validator.validate(hotel);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (phoneNumberAlreadyExists(hotel.getHotelTel(), hotel.getHotelId())) {
            throw new UniqueHotelTelException("Unique Hotel Telephone number Violation");
        }
    }


    boolean phoneNumberAlreadyExists(String hotelTel, Long id) {
        Hotel hotel = null;
        Hotel hotelWithID = null;

        try {
            hotel = hotelRepository.findByHotelTel(hotelTel);
        } catch (NoResultException e) {
            // ignore
        }

        if (hotel != null && id != null) {
            try {
                hotelWithID = hotelRepository.findById(id);
                if (hotelWithID != null && hotelWithID.getHotelTel().equals(hotelTel)) {
                    hotel = null;
                }
            } catch (NoResultException e) {
                // ignore
            }
        }
        return hotel != null;

    }
}