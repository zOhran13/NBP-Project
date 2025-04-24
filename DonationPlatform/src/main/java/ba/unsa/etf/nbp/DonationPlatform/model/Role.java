package ba.unsa.etf.nbp.DonationPlatform.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "NBP_ROLE", schema = "NBP")
public class Role {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NBP_ROLE_id_gen")
    @SequenceGenerator(name = "NBP_ROLE_id_gen", sequenceName = "NBP_ROLE_ID_SEQ", allocationSize = 1)
    private Long id;

    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private Set<User> nbpUsers = new LinkedHashSet<>();

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

}

