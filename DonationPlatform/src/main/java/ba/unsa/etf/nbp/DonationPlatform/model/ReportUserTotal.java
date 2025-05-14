package ba.unsa.etf.nbp.DonationPlatform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "report_user_totals")
public class ReportUserTotal {
   @Id
    private Long userId;

    @Column(name = "TOTAL_DONATED")
    private Long totalDonated;

    public Long getTotalDonated() {
        return totalDonated;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}

