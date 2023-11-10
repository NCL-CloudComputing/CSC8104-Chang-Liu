package uk.ac.newcastle.enterprisemiddleware.booking;

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
        //@NamedQuery(name = uk.ac.newcastle.enterprisemiddleware.booking.Booking.FIND_BY_EMAIL, query = "SELECT b FROM Booking b WHERE b.email = :email")
})
@XmlRootElement

//这个表中不允许重复的是什么？
@Table(name = "booking", uniqueConstraints = @UniqueConstraint(columnNames = "booking_id"))
public class Booking implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Booking.findAll";
   // public static final String FIND_BY_EMAIL = "Booking.findByEmail";

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name="booking_id" )
    private Long bookingId;

    @NotNull
    @NotEmpty
    @Email(message = "The email address must be in the format of name@domain.com")
    private String customer_email;

    @NotNull
    @Pattern(regexp = "^\\([2-9][0-8][0-9]\\)\\s?[0-9]{3}\\-[0-9]{4}$")
    @Column(name = "hotel_number")
    private String hotelNumber;

    @NotNull
    @Past(message = "Birthdates can not be in the past. Please choose one from the future")
    @Column(name = "booking_date")
    @Temporal(TemporalType.DATE)
    private Date bookingDate;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerEmail() {
        return customer_email;
    }

    public void setCustomerEmail(String email) {
        this.customer_email = email;
    }

    public String getHotelNumber() {
        return hotelNumber;
    }

    public void setHotelNumber(String hotelNumber) {
        this.hotelNumber = hotelNumber;
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
        return Objects.equals(hotelNumber, booking.hotelNumber) && Objects.equals(bookingDate, booking.bookingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotelNumber, bookingDate);
    }
}
