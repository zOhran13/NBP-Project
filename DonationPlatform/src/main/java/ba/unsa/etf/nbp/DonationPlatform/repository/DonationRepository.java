package ba.unsa.etf.nbp.DonationPlatform.repository;

import ba.unsa.etf.nbp.DonationPlatform.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByUserId(Long userId);
    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.campaign.id = :campaignId")
    Double getTotalAmountDonatedForCampaign(@Param("campaignId") Long campaignId);
}
