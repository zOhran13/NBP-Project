package ba.unsa.etf.nbp.DonationPlatform.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name="report",schema = "NBP03")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String content;
    private Date generatedDate;

    @OneToOne
    @JoinColumn(name = "campaign_id", referencedColumnName = "id")
    private Campaign campaign;
}
