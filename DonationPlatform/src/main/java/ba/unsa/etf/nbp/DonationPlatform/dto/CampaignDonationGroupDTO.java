package ba.unsa.etf.nbp.DonationPlatform.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CampaignDonationGroupDTO {
    private String campaignName;
    private List<DonationEntryDTO> donations;
    private BigDecimal subtotal;

    public CampaignDonationGroupDTO(String campaignName, List<DonationEntryDTO> donations, BigDecimal subtotal) {
        this.campaignName = campaignName;
        this.donations = donations;
        this.subtotal = subtotal;
    }

}

