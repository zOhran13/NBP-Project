package ba.unsa.etf.nbp.DonationPlatform.model;


import jakarta.persistence.*;

@Entity
@Table(name = "user_role", schema = "NBP03")
public class Role {
    @Id
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
