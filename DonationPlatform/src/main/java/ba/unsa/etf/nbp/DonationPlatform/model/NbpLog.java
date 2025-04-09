package ba.unsa.etf.nbp.DonationPlatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "NBP_LOG", schema = "NBP")
public class NbpLog {
    @Id
    @ColumnDefault("nbp.nbp_log_id_seq.NEXTVAL")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "ACTION_NAME", nullable = false)
    private String actionName;

    @Size(max = 255)
    @NotNull
    @Column(name = "TABLE_NAME", nullable = false)
    private String tableName;

    @NotNull
    @Column(name = "DATE_TIME", nullable = false)
    private Instant dateTime;

    @Size(max = 255)
    @Column(name = "DB_USER")
    private String dbUser;

    @Getter
    @Setter
    @Entity
    @Table(name = "NBP_ROLE", schema = "NBP")
    public static class NbpRole {
        @Id
        @ColumnDefault("nbp.nbp_role_id_seq.NEXTVAL")
        @Column(name = "ID", nullable = false)
        private Long id;

        @Size(max = 50)
        @NotNull
        @Column(name = "NAME", nullable = false, length = 50)
        private String name;

        @OneToMany(mappedBy = "role")
        private Set<User> nbpUsers = new LinkedHashSet<>();

    }
}