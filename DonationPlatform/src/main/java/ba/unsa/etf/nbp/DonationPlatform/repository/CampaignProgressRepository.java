package ba.unsa.etf.nbp.DonationPlatform.repository;

import ba.unsa.etf.nbp.DonationPlatform.model.CampaignProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignProgressRepository extends JpaRepository<CampaignProgress, Long> {
}
