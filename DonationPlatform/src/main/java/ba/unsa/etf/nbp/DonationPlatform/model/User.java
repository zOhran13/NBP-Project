package ba.unsa.etf.nbp.DonationPlatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "NBP_USER", schema = "NBP")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NBP_USER_id_gen")
    @SequenceGenerator(name = "NBP_USER_id_gen", sequenceName = "NBP_USER_ID_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Size(max = 255)
    @NotNull
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Size(max = 255)
    @NotNull
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Size(max = 255)
    @NotNull
    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Size(max = 255)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "ADDRESS_ID")
    private Long addressId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "ROLE_ID", nullable = false)
    private Role role;

}