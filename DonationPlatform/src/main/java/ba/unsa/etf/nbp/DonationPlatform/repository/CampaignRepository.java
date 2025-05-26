package ba.unsa.etf.nbp.DonationPlatform.repository;

import ba.unsa.etf.nbp.DonationPlatform.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

}
