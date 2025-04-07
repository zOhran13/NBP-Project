package ba.unsa.etf.nbp.DonationPlatform.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name="campaign_progress",schema = "NBP03")
public class CampaignProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double currentAmount;
    private Date lastUpdate;

    @OneToOne
    @JoinColumn(name = "campaign_id", referencedColumnName = "id")
    private Campaign campaign;


}
