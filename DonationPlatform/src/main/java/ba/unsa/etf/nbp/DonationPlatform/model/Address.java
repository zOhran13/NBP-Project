package ba.unsa.etf.nbp.DonationPlatform.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Table(name="address",schema = "NBP03")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String street;
    private String city;
    private Long postalCode;
    private String country;

    public Address(String street, String city, Long postalCode, String country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public Address() {

    }
}
