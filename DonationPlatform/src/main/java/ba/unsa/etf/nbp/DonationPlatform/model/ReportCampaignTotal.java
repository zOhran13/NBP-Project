package ba.unsa.etf.nbp.DonationPlatform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
@Getter
@Setter
@Entity
@Immutable
@Table(name = "REPORT_CAMPAIGN_TOTALS")
public class ReportCampaignTotal {
    @Getter
    @Setter
    @Id
    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;

    @Column(name = "TOTAL_COLLECTED")
    private Long totalCollected;

}