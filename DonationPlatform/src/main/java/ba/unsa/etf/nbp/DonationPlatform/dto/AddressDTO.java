package ba.unsa.etf.nbp.DonationPlatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    String street;
    String city;
    Long postalCode;
    String country;
    public AddressDTO(String street, String city, Long postalCode, String country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }
}
