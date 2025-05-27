package ba.unsa.etf.nbp.DonationPlatform.mapper;

import ba.unsa.etf.nbp.DonationPlatform.dto.CampaignResponseDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.Campaign;

import java.util.Base64;

public class CampaignMapper {

    public static CampaignResponseDTO toDTO(Campaign campaign) {
        CampaignResponseDTO dto = new CampaignResponseDTO();
        dto.setId(campaign.getId());
        dto.setName(campaign.getName());
        dto.setStartDate(campaign.getStartDate());
        dto.setEndDate(campaign.getEndDate());
        dto.setTargetAmount(campaign.getTargetAmount());

        if (campaign.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(campaign.getImage());
            dto.setImage("data:image/jpeg;base64," + base64Image);
        }

        return dto;
    }
}
