package ba.unsa.etf.nbp.DonationPlatform.controller;

import ba.unsa.etf.nbp.DonationPlatform.security.JwtTokenHelper;
import ba.unsa.etf.nbp.DonationPlatform.service.ReportService;
import ba.unsa.etf.nbp.DonationPlatform.service.ReportViewService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportViewController {

    private final ReportViewService reportService;

    private final JwtTokenHelper jwtTokenHelper;

    public ReportViewController(ReportViewService reportService, JwtTokenHelper jwtTokenHelper) {
        this.reportService = reportService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @GetMapping("/campaign/{id}/pdf")
    public ResponseEntity<byte[]> getCampaignReportPdf(@PathVariable Long id) {
        byte[] pdf = reportService.generateCampaignReportPdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=campaign_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/user/pdf")
    public ResponseEntity<byte[]> getUserReportPdf(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        Long userId = extractUserIdFromRequest(request);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        byte[] pdf = reportService.generateUserFullDonationReport(userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=user_" + userId + "_donations.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/user/pdf/download")
    public ResponseEntity<byte[]> downloadUserReportPdf(HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        byte[] pdf = reportService.generateUserFullDonationReport(userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_" + userId + "_donations.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // remove "Bearer "
            Claims claims = jwtTokenHelper.getClaimsFromToken(token);
            return Long.valueOf(claims.getSubject()); // Assuming subject is user ID
        }

        return null;
    }

}

