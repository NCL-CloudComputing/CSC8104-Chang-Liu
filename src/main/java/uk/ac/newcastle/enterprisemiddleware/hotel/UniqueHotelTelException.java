package uk.ac.newcastle.enterprisemiddleware.hotel;

import javax.validation.ValidationException;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-10 18:15
 **/

public class UniqueHotelTelException extends ValidationException {
    public UniqueHotelTelException(String message) {
        super(message);
    }

    public UniqueHotelTelException() {
    }

    public UniqueHotelTelException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueHotelTelException(Throwable cause) {
        super(cause);
    }
}
