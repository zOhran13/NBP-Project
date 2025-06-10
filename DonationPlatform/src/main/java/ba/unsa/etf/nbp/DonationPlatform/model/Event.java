package ba.unsa.etf.nbp.DonationPlatform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.security.Timestamp;
import java.util.Date;

@Entity
@Data
@Table(name="event",schema = "NBP03")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String imageLink;
    private Date eventStart;
    private Date eventEnd;
    private String location;
    private String description;
    private Long volunteerGoal;
    private Double donationGoal;

    public Event(String title, String imageLink, Date eventStart, Date eventEnd, String location, String description, Long volunteerGoal, Double donationGoal) {
        this.title = title;
        this.imageLink = imageLink;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.location = location;
        this.description = description;
        this.volunteerGoal = volunteerGoal;
        this.donationGoal = donationGoal;
    }

    public Event() {

    }
}
