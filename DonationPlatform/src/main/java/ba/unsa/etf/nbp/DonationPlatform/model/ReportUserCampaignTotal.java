package ba.unsa.etf.nbp.DonationPlatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@Entity
@Immutable
@IdClass(ReportUserCampaignTotalId.class)
@Table(name = "REPORT_USER_CAMPAIGN_TOTALS")
public class ReportUserCampaignTotal {
    @Id
    @Column(name = "USER_ID")
    private Long userId;

    @Id
    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;

    @Column(name = "SUBTOTAL")
    private Long subtotal;

}