package ba.unsa.etf.nbp.DonationPlatform.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "NBP_USER", schema = "NBP")
public class User {

    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String username;

    private String phoneNumber;

    private LocalDate birthDate;


    @ManyToOne
    @JoinColumn(name = "ADDRESS_ID",referencedColumnName = "ID")
    private Address address;

    @OneToOne
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID", nullable = false)
    private Role role;


    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public Address getAddress() {
        return address;
    }
}
