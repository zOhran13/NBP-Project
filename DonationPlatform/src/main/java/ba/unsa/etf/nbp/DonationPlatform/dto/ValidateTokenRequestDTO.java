package ba.unsa.etf.nbp.DonationPlatform.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class ValidateTokenRequestDTO {
    private List<String> roles;
}
