package ba.unsa.etf.nbp.DonationPlatform.controller;

import ba.unsa.etf.nbp.DonationPlatform.service.ReportService;
import ba.unsa.etf.nbp.DonationPlatform.service.ReportViewService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportViewController {

    private final ReportViewService reportService;

    public ReportViewController(ReportViewService reportService) {
        this.reportService = reportService;
    }
    @GetMapping("/campaign/{id}/pdf")
    public ResponseEntity<byte[]> getCampaignReportPdf(@PathVariable Long id) {
        byte[] pdf = reportService.generateCampaignReportPdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=campaign_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/user/{userId}/pdf")
    public ResponseEntity<byte[]> getUserReportPdf(@PathVariable Long userId) {
        byte[] pdf = reportService.generateUserFullDonationReport(userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=user_" + userId + "_donations.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    @GetMapping("/user/{userId}/pdf/download")
    public ResponseEntity<byte[]> downloadUserReportPdf(@PathVariable Long userId) {
        byte[] pdf = reportService.generateUserFullDonationReport(userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_" + userId + "_donations.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}

