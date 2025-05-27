package ba.unsa.etf.nbp.DonationPlatform.dto;

import lombok.Data;
import java.util.Date;

@Data
public class CampaignResponseDTO {
    private Long id;
    private String name;
    private String image;
    private Date startDate;
    private Date endDate;
    private Double targetAmount;
}