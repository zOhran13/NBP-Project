package ba.unsa.etf.nbp.DonationPlatform.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table( name = "user_role",schema = "NBP03")
public class NbpRole {

    @Id
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private NbpUser user;
}
