package uk.ac.newcastle.enterprisemiddleware.travelAgent;

import uk.ac.newcastle.enterprisemiddleware.booking.Booking;

import java.util.Date;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-14 15:59
 **/



@SuppressWarnings("all")
public class BookingVO {

    private Long id;

    private Long customerId;

    private Long hotelId;

    private Date bookingDate;

    private Long taxiId;

    private Long flightId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Long getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(Long taxiId) {
        this.taxiId = taxiId;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public BookingVO() {
    }

    public BookingVO(Booking booking) {
        this.id = booking.getBookingId();
        this.customerId = booking.getCustomer().getCustomerId();
        this.hotelId = booking.getHotel().getHotelId();
        this.bookingDate = booking.getBookingDate();
    }


}
