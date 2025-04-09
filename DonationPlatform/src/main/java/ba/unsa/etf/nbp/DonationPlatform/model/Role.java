package ba.unsa.etf.nbp.DonationPlatform.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "NBP_ROLE", schema = "NBP")
public class Role {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "role")
    private List<User> users;

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


}

