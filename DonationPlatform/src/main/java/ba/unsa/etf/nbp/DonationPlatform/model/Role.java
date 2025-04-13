package ba.unsa.etf.nbp.DonationPlatform.model;


import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "NBP_ROLE", schema = "NBP")
public class Role {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "role")
    private Set<User> nbpUsers = new LinkedHashSet<>();

    public Set<User> getNbpUsers() {
        return nbpUsers;
    }

    public void setNbpUsers(Set<User> nbpUsers) {
        this.nbpUsers = nbpUsers;
    }


    // Default constructor
    public Role() {}

    // Constructor
    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}

