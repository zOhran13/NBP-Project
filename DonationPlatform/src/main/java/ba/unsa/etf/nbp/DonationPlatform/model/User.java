package ba.unsa.etf.nbp.DonationPlatform.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name = "NBP_USER", schema = "NBP")
public class User {

    @Id
    @Getter
    private Long id;

    private String firstName;

    private String lastName;

    @Getter
    private String email;

    private String password;

    @Getter
    private String username;

    private String phoneNumber;

    private LocalDate birthDate;

    @Getter
    @ManyToOne
    @JoinColumn(name = "ADDRESS_ID",referencedColumnName = "ID")
    private Address address;

    @Getter
    @OneToOne
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID", nullable = false)
    private Role role;

    public User(String firstName, String lastName, String email, String password, String username, String phoneNumber, LocalDate birthDate, Address address, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.address = address;
        this.role = role;
    }

    public User() {

    }
}
