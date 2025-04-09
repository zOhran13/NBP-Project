package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.model.Campaign;
import ba.unsa.etf.nbp.DonationPlatform.repository.CampaignRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    // CREATE
    public Campaign createCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    // READ (all)
    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    // READ (by ID)
    public Optional<Campaign> getCampaignById(Long id) {
        return campaignRepository.findById(id);
    }

    // UPDATE
    public Campaign updateCampaign(Long id, Campaign updatedCampaign) {
        return campaignRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedCampaign.getName());
                    existing.setStartDate(updatedCampaign.getStartDate());
                    existing.setEndDate(updatedCampaign.getEndDate());
                    existing.setTargetAmount(updatedCampaign.getTargetAmount());
                    return campaignRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Campaign not found with id " + id));
    }

    // DELETE
    public void deleteCampaign(Long id) {
        if (!campaignRepository.existsById(id)) {
            throw new RuntimeException("Campaign not found with id " + id);
        }
        campaignRepository.deleteById(id);
    }
}