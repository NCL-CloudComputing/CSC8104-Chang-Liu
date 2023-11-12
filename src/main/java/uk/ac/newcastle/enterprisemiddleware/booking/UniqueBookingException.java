package uk.ac.newcastle.enterprisemiddleware.booking;

import javax.validation.ValidationException;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-11 22:29
 **/

public class UniqueBookingException extends ValidationException {
    public UniqueBookingException(String message) {
        super(message);
    }

    public UniqueBookingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueBookingException(Throwable cause) {
        super(cause);
    }
}
