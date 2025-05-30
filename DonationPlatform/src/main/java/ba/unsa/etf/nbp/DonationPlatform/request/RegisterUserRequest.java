package ba.unsa.etf.nbp.DonationPlatform.request;

import ba.unsa.etf.nbp.DonationPlatform.dto.AddressDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class RegisterUserRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String username;
        private String phoneNumber;
        private LocalDate birthDate;
        private AddressDTO address;
        private Long roleId;

}
