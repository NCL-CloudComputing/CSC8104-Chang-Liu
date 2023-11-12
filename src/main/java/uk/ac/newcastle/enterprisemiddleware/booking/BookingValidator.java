package uk.ac.newcastle.enterprisemiddleware.booking;

import uk.ac.newcastle.enterprisemiddleware.contact.ContactRepository;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-11 22:28
 **/
@ApplicationScoped
public class BookingValidator {

    @Inject
    Validator validator;

    @Inject
    BookingRepository bookingRepository;
    public void validateBooking(Booking booking) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (bookingAlreadyExists(booking,booking.getHotelId())) {
            throw new UniqueBookingException("UniqueBooking Violation");
        }
    }

    boolean  bookingAlreadyExists(Booking booking, Long hotelId) {
        Booking booking1 = null;
        Booking bookingWithID = null;

        try {
            booking = bookingRepository.findByCustomerId(booking.getCustomerId());
        } catch (NoResultException e) {
            // ignore
        }

        if (booking != null && booking.getHotelId() != null) {
            try {
                bookingWithID = bookingRepository.findByHotelIdAndBookingDate(hotelId,booking.getBookingDate());
                if (bookingWithID != null) {
                    booking = null;
                }
            } catch (NoResultException e) {
                // ignore
            }
        }
        return booking != null;
    }

}
