package ba.unsa.etf.nbp.DonationPlatform.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="event_participation",schema = "NBP03")
public class EventParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
