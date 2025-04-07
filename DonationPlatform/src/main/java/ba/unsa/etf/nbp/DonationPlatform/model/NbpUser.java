package ba.unsa.etf.nbp.DonationPlatform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table( name = "app_user" , schema = "NBP03")
public class NbpUser {

    @Id
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;


    @ManyToOne
    @JoinColumn(name = "ADDRESS_ID",referencedColumnName = "ID")
    private Address address;

    @OneToOne
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID", nullable = false)
    private NbpRole role;
}
