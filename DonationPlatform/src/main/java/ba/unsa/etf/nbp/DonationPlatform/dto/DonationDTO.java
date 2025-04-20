package ba.unsa.etf.nbp.DonationPlatform.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class DonationDTO {
    private Long id;
    private Double amount;
    private Date donationDate;
    private Long userId;
    private Long campaignId;
}
