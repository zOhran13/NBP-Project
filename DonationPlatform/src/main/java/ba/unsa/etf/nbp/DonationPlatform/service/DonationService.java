package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.model.Donation;
import ba.unsa.etf.nbp.DonationPlatform.repository.DonationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DonationService {

    private final DonationRepository donationRepository;

    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    public Optional<Donation> getDonationById(Long id) {
        return donationRepository.findById(id);
    }

    public Donation createDonation(Donation donation) {
        return donationRepository.save(donation);
    }

    public Donation updateDonation(Long id, Donation donationDetails) {
        Donation existing = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        existing.setAmount(donationDetails.getAmount());
        existing.setDonationDate(donationDetails.getDonationDate());
        existing.setUser(donationDetails.getUser());
        existing.setCampaign(donationDetails.getCampaign());

        return donationRepository.save(existing);
    }

    public void deleteDonation(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        donationRepository.delete(donation);
    }
    public List<Donation> getUserDonations(Long userId) {
        return donationRepository.findByUserId(userId);
    }
}