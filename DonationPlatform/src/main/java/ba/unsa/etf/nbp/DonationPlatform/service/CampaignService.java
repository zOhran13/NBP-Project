package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.enums.RoleName;
import ba.unsa.etf.nbp.DonationPlatform.model.Campaign;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.repository.CampaignRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.DonationRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.UserRepository;
import ba.unsa.etf.nbp.DonationPlatform.service.email_template.EmailTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CampaignService {
    private static final Logger logger = LoggerFactory.getLogger(CampaignService.class);
    private final CampaignRepository campaignRepository;
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;

    public CampaignService(CampaignRepository campaignRepository, DonationRepository donationRepository, UserRepository userRepository, EmailService emailService, EmailTemplateService emailTemplateService) {
        this.campaignRepository = campaignRepository;
        this.donationRepository = donationRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.emailTemplateService = emailTemplateService;
    }


    // CREATE
    public Campaign createCampaign(Campaign campaign) {
        Campaign savedCampaign = campaignRepository.save(campaign);
        logger.info("Kampanja '{}' uspješno spremljena.", savedCampaign.getName());

        try {
            List<User> allUsers = userRepository.findAllByRoleNames(List.of(
                RoleName.ORGANIZACIJA.name(),
                RoleName.VOLONTER.name(),
                RoleName.DONATOR.name()
            ));

            if (allUsers.isEmpty()) {
                logger.warn("Nema registrovanih korisnika za slanje obavijesti o kampanji.");
                return savedCampaign;
            }

            // Podaci o kampanji koji će se proslijediti u template
            String campaignName = savedCampaign.getName();
            String campaignImageLink = savedCampaign.getImageLink();
            Double campaignTargetAmount = savedCampaign.getTargetAmount();
            String campaignLink = "http://localhost:8080/campaigns/" + savedCampaign.getId();
            Date startDate = savedCampaign.getStartDate();
            Date endDate = savedCampaign.getEndDate();
            for (User user : allUsers) {
                String htmlContent = emailTemplateService.buildNewCampaignNotificationEmail(
                        user.getUsername(),
                        campaignName,
                        campaignImageLink,
                        campaignTargetAmount,
                        campaignLink,
                        startDate,
                        endDate
                );

                String subject = "🎉 Nova kampanja je aktivna: " + campaignName + "!";
                emailService.sendHtmlEmail(user.getEmail(), subject, htmlContent);
            }
            logger.info("Pokrenuto slanje email obavijesti za kampanju '{}' svim korisnicima.", savedCampaign.getName());

        } catch (Exception e) {
            logger.error("Greška prilikom slanja email obavijesti za kampanju {}: {}", savedCampaign.getName(), e.getMessage(), e);
        }

        return savedCampaign;
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

    public Double getAmountDonatedForCampaign(Long campaignId) {
        Double sum = donationRepository.getTotalAmountDonatedForCampaign(campaignId);
        return sum != null ? sum : 0.0;
    }
}