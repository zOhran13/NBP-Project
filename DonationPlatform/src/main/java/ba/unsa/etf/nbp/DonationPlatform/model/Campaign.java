package ba.unsa.etf.nbp.DonationPlatform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name="campaign",schema = "NBP03")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String imageLink;
    private Date startDate;
    private Date endDate;
    private Double targetAmount;


    public Campaign(String name, String imageLink, Date startDate, Date endDate, Double targetAmount) {
        this.name = name;
        this.imageLink = imageLink;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetAmount = targetAmount;
    }

    public Campaign() {

    }
}
