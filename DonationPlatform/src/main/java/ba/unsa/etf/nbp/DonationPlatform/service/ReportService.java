package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.dto.ReportDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.Campaign;
import ba.unsa.etf.nbp.DonationPlatform.model.Report;
import ba.unsa.etf.nbp.DonationPlatform.repository.CampaignRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    public List<ReportDTO> getAllReports() {
        return reportRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ReportDTO> getReportById(Long id) {
        return reportRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<ReportDTO> createReport(ReportDTO reportDTO) {
        Optional<Campaign> campaignOpt = campaignRepository.findById(reportDTO.getCampaignId());
        if (campaignOpt.isEmpty()) {
            return Optional.empty();
        }

        Report report = new Report();
        report.setType(reportDTO.getType());
        report.setContent(reportDTO.getContent());
        report.setGeneratedDate(reportDTO.getGeneratedDate());
        report.setCampaign(campaignOpt.get());

        Report saved = reportRepository.save(report);
        return Optional.of(convertToDTO(saved));
    }

    public boolean deleteReport(Long id) {
        if (reportRepository.existsById(id)) {
            reportRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ReportDTO convertToDTO(Report report) {
        ReportDTO dto = new ReportDTO();
        dto.setId(report.getId());
        dto.setType(report.getType());
        dto.setContent(report.getContent());
        dto.setGeneratedDate(report.getGeneratedDate());
        dto.setCampaignId(report.getCampaign().getId());
        return dto;
    }
}
