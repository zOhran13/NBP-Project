package ba.unsa.etf.nbp.DonationPlatform.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UserDonationReportDTO {
    private List<CampaignDonationGroupDTO> campaigns;
    private BigDecimal total;

    public UserDonationReportDTO(List<CampaignDonationGroupDTO> campaigns, BigDecimal total) {
        this.campaigns = campaigns;
        this.total = total;
    }

}

