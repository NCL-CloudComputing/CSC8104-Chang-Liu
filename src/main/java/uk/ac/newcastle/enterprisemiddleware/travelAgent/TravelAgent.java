package uk.ac.newcastle.enterprisemiddleware.travelAgent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import uk.ac.newcastle.enterprisemiddleware.booking.Booking;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @description
 * @author Chang Liu
 * @create 2023/11/13
 */

@SuppressWarnings("all")
@Entity
@NamedQueries({
        @NamedQuery(name = TravelAgent.FIND_ALL_TRAVELAGENT, query = "SELECT t FROM TravelAgent t ORDER BY t.id ASC "),
})
@Table(name = "travelAgent")
@XmlRootElement
public class TravelAgent implements Serializable {

    private static final long serialVersionUID = 24987231L;

    public static final String FIND_ALL_TRAVELAGENT = "TravelAgent.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    @NotNull
    private Long customerId;

    @Column(name = "taxi_id")
    @NotNull
    private Long  taxiId;

    @Column(name = "flight_id")
    @NotNull
    private Long  flightId;

    @Column(name = "hotel_id")
    @NotNull
    private Long hotelid;

    @Column(name = "taxi_booking_id")
    @JsonIgnore
    private Long taxiBookingId;
//    @Schema(readOnly = true)

    @Column(name = "flight_booking_id")
    @JsonIgnore
    private Long flightBookingId;

    @Column(name = "hotel_booking_id")
    @JsonIgnore
    private Long hotelBookingId;

    @OneToOne(cascade =CascadeType.REMOVE)
    @JoinColumn(name="booking_id",referencedColumnName = "bookingId")
    @JsonIgnore
    private Booking booking;

    @NotNull
    @Column(name = "booking_date")
    @Future(message = "futureDate can not be in the past. Please choose one from the future")
    @Temporal(TemporalType.DATE)
    private Date bookingDate;

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

    public Long getHotelid() {
        return hotelid;
    }

    public void setHotelid(Long hotelid) {
        this.hotelid = hotelid;
    }

    public Long getTaxiBookingId() {
        return taxiBookingId;
    }

    public void setTaxiBookingId(Long taxiBookingId) {
        this.taxiBookingId = taxiBookingId;
    }

    public Long getFlightBookingId() {
        return flightBookingId;
    }

    public void setFlightBookingId(Long flightBookingId) {
        this.flightBookingId = flightBookingId;
    }

    public Long getHotelBookingId() {
        return hotelBookingId;
    }

    public void setHotelBookingId(Long hotelBookingId) {
        this.hotelBookingId = hotelBookingId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

}
