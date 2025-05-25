package ba.unsa.etf.nbp.DonationPlatform.dto;

import ba.unsa.etf.nbp.DonationPlatform.model.Address;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String phoneNumber;
    private LocalDate birthDate;
    private AddressDTO address;
    private String role;


    public UserDTO(String firstName, String lastName, String username, String email, String phoneNumber, LocalDate birthDate, AddressDTO addressDTO, String role) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.address = addressDTO;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

}