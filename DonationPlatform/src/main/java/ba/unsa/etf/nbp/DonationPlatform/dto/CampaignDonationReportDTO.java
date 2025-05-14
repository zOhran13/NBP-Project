package ba.unsa.etf.nbp.DonationPlatform.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
public class CampaignDonationReportDTO {
    private String firstName;
    private String lastName;
    private BigDecimal donated;
    private Date date;
    private BigDecimal campaignTotal;

    public CampaignDonationReportDTO(String firstName, String lastName, Date donationDate, BigDecimal donated) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.donated = donated;
        this.date = donationDate;
    }

}
