package ba.unsa.etf.nbp.DonationPlatform.controller;

import ba.unsa.etf.nbp.DonationPlatform.model.Donation;
import ba.unsa.etf.nbp.DonationPlatform.service.DonationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
public class DonationController {
    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping
    public ResponseEntity<Donation> createDonation(@RequestBody Donation donation) {
        return ResponseEntity.ok(donationService.createDonation(donation));
    }

    @GetMapping("/user/{userId}")
    public List<Donation> getUserDonations(@PathVariable Long userId) {
        return donationService.getUserDonations(userId);
    }
}
