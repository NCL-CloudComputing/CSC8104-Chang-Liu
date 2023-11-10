package uk.ac.newcastle.enterprisemiddleware.hotel;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-02 16:07
 **/


@Entity
@NamedQueries({
        @NamedQuery(name = uk.ac.newcastle.enterprisemiddleware.hotel.Hotel.FIND_ALL, query = "SELECT c FROM Hotel c ORDER BY c.hotelName ASC"),
        @NamedQuery(name = uk.ac.newcastle.enterprisemiddleware.hotel.Hotel.FIND_BY_TEL, query = "SELECT c FROM Hotel c WHERE c.hotelTel = :hotelTel")
})
@XmlRootElement
@Table(name = "hotel", uniqueConstraints = @UniqueConstraint(columnNames = "hotel_tel"))
public class Hotel implements Serializable {
    /**
     * Default value included to remove warning. Remove or modify at will.
     **/
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Hotel.findAll";
    public static final String FIND_BY_TEL = "Hotel.findByTel";


    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long hotelId;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "hotel_name")
    private String hotelName;

    @NotNull
    @Pattern(regexp = "^0[0-9]{10}")
    @Column(name = "hotel_tel")
    private String hotelTel;

    @NotNull
    @Size(max = 6)
    @Pattern(regexp = "^[a-zA-Z0-9]{6}$")
    @Column(name = "postcode")
    private String postcode;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelTel() {
        return hotelTel;
    }

    public void setHotelTel(String hotelTel) {
        this.hotelTel = hotelTel;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hotel)) return false;
        Hotel hotel = (Hotel) o;
        return hotel.equals(hotel.hotelTel);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hotelTel);
    }

}