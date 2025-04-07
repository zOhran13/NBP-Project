package ba.unsa.etf.nbp.DonationPlatform.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name="donation",schema = "NBP03")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private Date donationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private NbpUser user;

    @ManyToOne
    @JoinColumn(name = "campaign_id", referencedColumnName = "id")
    private Campaign campaign;


}
