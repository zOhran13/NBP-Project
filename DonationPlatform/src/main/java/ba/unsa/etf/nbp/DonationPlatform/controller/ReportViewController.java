package ba.unsa.etf.nbp.DonationPlatform.controller;

import ba.unsa.etf.nbp.DonationPlatform.service.ReportService;
import ba.unsa.etf.nbp.DonationPlatform.service.ReportViewService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportViewController {

    private final ReportViewService reportService;

    public ReportViewController(ReportViewService reportService) {
        this.reportService = reportService;
    }
    @GetMapping("/reports/campaign/{id}/pdf")
    public ResponseEntity<byte[]> getCampaignReportPdf(@PathVariable Long id) {
        byte[] pdf = reportService.generateCampaignReportPdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=campaign_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    @GetMapping("/reports/user/{userId}/pdf")
    public ResponseEntity<byte[]> getUserReportPdf(@PathVariable Long userId) {
        byte[] pdf = reportService.generateUserFullDonationReport(userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_" + userId + "_donations.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}

