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
    @Lob
    @Column(name = "image", columnDefinition = "BLOB")
    private byte[] image;
    private Date startDate;
    private Date endDate;
    private Double targetAmount;


    public Campaign(String name, byte[] image, Date startDate, Date endDate, Double targetAmount) {
        this.name = name;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetAmount = targetAmount;
    }

    public Campaign() {

    }
}
