package ba.unsa.etf.nbp.DonationPlatform.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginUserRequest {
    private String email;

    private String password;
}

