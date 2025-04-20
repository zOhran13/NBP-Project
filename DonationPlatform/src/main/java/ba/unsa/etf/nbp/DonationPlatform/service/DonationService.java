package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.dto.DonationDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.Campaign;
import ba.unsa.etf.nbp.DonationPlatform.model.Donation;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.repository.CampaignRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.DonationRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;

    public DonationService(DonationRepository donationRepository,
                           UserRepository userRepository,
                           CampaignRepository campaignRepository) {
        this.donationRepository = donationRepository;
        this.userRepository = userRepository;
        this.campaignRepository = campaignRepository;
    }

    public List<DonationDTO> getAllDonations() {
        return donationRepository.findAll()
                .stream()
                .map(donation -> convertToDTO(donation)) // Mapira donaciju u DTO
                .collect(Collectors.toList());
    }

    public Optional<DonationDTO> getDonationById(Long id) {
        return donationRepository.findById(id)
                .map(donation -> convertToDTO(donation)); // Mapira donaciju u DTO
    }

    public DonationDTO createDonation(DonationDTO donationDTO) {
        Optional<User> user = userRepository.findById(donationDTO.getUserId());
        Optional<Campaign> campaign = campaignRepository.findById(donationDTO.getCampaignId());

        if (user.isEmpty() || campaign.isEmpty()) {
            return null; // User or campaign not found
        }

        Donation donation = new Donation();
        donation.setAmount(donationDTO.getAmount());
        donation.setDonationDate(donationDTO.getDonationDate());
        donation.setUser(user.get());
        donation.setCampaign(campaign.get());

        Donation savedDonation = donationRepository.save(donation);
        return convertToDTO(savedDonation); // Vraća DTO
    }

    public DonationDTO updateDonation(Long id, DonationDTO donationDTO) {
        Optional<Donation> existingDonation = donationRepository.findById(id);

        if (existingDonation.isEmpty()) {
            throw new RuntimeException("Donation not found");
        }

        Donation donation = existingDonation.get();
        donation.setAmount(donationDTO.getAmount());
        donation.setDonationDate(donationDTO.getDonationDate());

        Optional<User> user = userRepository.findById(donationDTO.getUserId());
        Optional<Campaign> campaign = campaignRepository.findById(donationDTO.getCampaignId());

        if (user.isPresent() && campaign.isPresent()) {
            donation.setUser(user.get());
            donation.setCampaign(campaign.get());
        }

        Donation updatedDonation = donationRepository.save(donation);
        return convertToDTO(updatedDonation); // Vraća ažurirani DTO
    }

    public void deleteDonation(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        donationRepository.delete(donation);
    }

    public List<DonationDTO> getUserDonations(Long userId) {
        return donationRepository.findByUserId(userId)
                .stream()
                .map(donation -> convertToDTO(donation)) // Mapira donaciju u DTO
                .collect(Collectors.toList());
    }

    private DonationDTO convertToDTO(Donation donation) {
        DonationDTO dto = new DonationDTO();
        dto.setId(donation.getId());
        dto.setAmount(donation.getAmount());
        dto.setDonationDate(donation.getDonationDate());
        dto.setUserId(donation.getUser().getId());
        dto.setCampaignId(donation.getCampaign().getId());
        return dto;
    }
}
