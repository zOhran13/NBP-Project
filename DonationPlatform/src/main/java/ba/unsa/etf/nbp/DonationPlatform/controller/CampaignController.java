package ba.unsa.etf.nbp.DonationPlatform.controller;

import ba.unsa.etf.nbp.DonationPlatform.dto.CampaignResponseDTO;
import ba.unsa.etf.nbp.DonationPlatform.mapper.CampaignMapper;
import ba.unsa.etf.nbp.DonationPlatform.model.Campaign;
import ba.unsa.etf.nbp.DonationPlatform.service.CampaignService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/campaign")
public class CampaignController {
    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public List<CampaignResponseDTO> getAllCampaigns() {
        return campaignService.getAllCampaigns().stream()
                .map(CampaignMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campaign> getCampaignById(@PathVariable Long id) {
        return campaignService.getCampaignById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Campaign> createCampaign(@RequestParam("name") String name,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam("targetAmount") Double targetAmount) throws IOException {
        byte[] imageBytes = imageFile.getBytes();
        Campaign campaign = new Campaign(name, imageBytes, startDate, endDate, targetAmount);
        Campaign saved = campaignService.createCampaign(campaign);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable Long id, @RequestBody Campaign campaign) {
        try {
            Campaign updated = campaignService.updateCampaign(id, campaign);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/donated-amount")
    public ResponseEntity<Double> getAmountDonated(@PathVariable Long id) {
        Double donated = campaignService.getAmountDonatedForCampaign(id);
        return ResponseEntity.ok(donated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        try {
            campaignService.deleteCampaign(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<Campaign> campaign = campaignService.findById(id);
        if (campaign.isEmpty() || campaign.get().getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(campaign.get().getImage(), headers, HttpStatus.OK);
    }

}
