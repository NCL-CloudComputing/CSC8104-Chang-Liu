package uk.ac.newcastle.enterprisemiddleware.booking;

import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-02 16:26
 **/

@Entity
@NamedQueries({
        @NamedQuery(name = uk.ac.newcastle.enterprisemiddleware.booking.Booking.FIND_ALL, query = "SELECT b FROM Booking b"),
        @NamedQuery(name = uk.ac.newcastle.enterprisemiddleware.booking.Booking.FIND_ALL_BY_CUSTOMER, query = "SELECT b FROM Booking b Where b.customer.customerId=:customerId"),
        @NamedQuery(name=uk.ac.newcastle.enterprisemiddleware.booking.Booking.FIND_BY_HOTEL_AND_BOOKINGDATE,query = "SELECT b FROM Booking b WHERE b.hotel.hotelId=:hotelId and b.bookingDate = :bookingDate"),
        @NamedQuery(name=uk.ac.newcastle.enterprisemiddleware.booking.Booking.FIND_BY_BOOKING_ID,query ="SELECT b FROM Booking b Where b.bookingId=:bookingId"),

        @NamedQuery(name=uk.ac.newcastle.enterprisemiddleware.booking.Booking.FIND_BY_CUSTOMER_ID,query ="SELECT b FROM Booking b Where b.customer.customerId=:customerId")
                //@NamedQuery(name = uk.ac.newcastle.enterprisemiddleware.booking.Booking.FIND_BY_EMAIL, query = "SELECT b FROM Booking b WHERE b.email = :email")
})
@XmlRootElement
@Table(name = "booking", uniqueConstraints = @UniqueConstraint(columnNames = {"booking_date","hotel_id"}))
public class Booking implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1123548L;

    public static final String FIND_ALL = "Booking.findAll";
    public static final String FIND_ALL_BY_CUSTOMER ="Booking.findAllByCustomer" ;
    // public static final String FIND_BY_EMAIL = "Booking.findByEmail";
    public static final String FIND_BY_HOTEL_AND_BOOKINGDATE="Booking.findByHotelAndBookingDate";
    public static final String FIND_BY_CUSTOMER_ID="Booking.findByCustomerId";
    public static final String FIND_BY_BOOKING_ID="Booking.findByBookingId";



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="booking_id" )
    private Long bookingId;

    @ManyToOne()
    @JoinColumn(name = "customer_id",referencedColumnName = "customer_id")
    @NotNull
    private Customer customer;

    @ManyToOne()
    @JoinColumn(name = "hotel_Id",referencedColumnName = "hotel_Id")
    @NotNull
    private Hotel hotel;

    @NotNull
    @Future(message = "bookingDate can not be in the past. Please choose one from the future")
    @Column(name = "booking_date")
    @Temporal(TemporalType.DATE)
    private Date bookingDate;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(hotel, booking.hotel) && Objects.equals(bookingDate, booking.bookingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotel, bookingDate);
    }
    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", customer=" + customer +
                ", hotel=" + hotel +
                ", bookingDate=" + bookingDate +
                '}';
    }
}
