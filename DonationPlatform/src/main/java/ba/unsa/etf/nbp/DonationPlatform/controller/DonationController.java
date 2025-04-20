package ba.unsa.etf.nbp.DonationPlatform.controller;

import ba.unsa.etf.nbp.DonationPlatform.dto.DonationDTO;
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
    public ResponseEntity<DonationDTO> createDonation(@RequestBody DonationDTO donationDTO) {
        DonationDTO createdDonation = donationService.createDonation(donationDTO);
        if (createdDonation != null) {
            return ResponseEntity.ok(createdDonation);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public List<DonationDTO> getUserDonations(@PathVariable Long userId) {
        return donationService.getUserDonations(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonationDTO> updateDonation(@PathVariable Long id, @RequestBody DonationDTO donationDTO) {
        try {
            DonationDTO updatedDonation = donationService.updateDonation(id, donationDTO);
            return ResponseEntity.ok(updatedDonation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable Long id) {
        try {
            donationService.deleteDonation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
