package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.dto.CampaignDonationGroupDTO;
import ba.unsa.etf.nbp.DonationPlatform.dto.CampaignDonationReportDTO;
import ba.unsa.etf.nbp.DonationPlatform.dto.DonationEntryDTO;
import ba.unsa.etf.nbp.DonationPlatform.dto.UserDonationReportDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.Campaign;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.repository.CampaignRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.ReportCampaignTotalRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.ReportUserTotalRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.UserRepository;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReportViewService {

    private final ReportUserTotalRepository reportUserTotalRepository;
    private final ReportCampaignTotalRepository campaignTotalRepo;
    private final CampaignRepository campaignRepo;
    private final UserRepository userRepo;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ReportViewService(ReportUserTotalRepository userRepo, ReportCampaignTotalRepository campaignRepo, CampaignRepository campaignRepository, UserRepository userRepository) {
        this.reportUserTotalRepository = userRepo;
        this.campaignTotalRepo = campaignRepo;
        this.campaignRepo = campaignRepository;
        this.userRepo= userRepository;

    }
    public List<CampaignDonationReportDTO> getDonationsByCampaign(Long campaignId) {
        String donationQuery = """
        SELECT u.first_name, u.last_name, d.donation_date, d.amount AS donated
        FROM nbp03.donation d
        JOIN nbp.nbp_user u ON d.user_id = u.id
        WHERE d.campaign_id = ?
        ORDER BY donated DESC
    """;

        List<CampaignDonationReportDTO> donations = jdbcTemplate.query(
                donationQuery,
                new Object[]{campaignId},
                (rs, rowNum) -> new CampaignDonationReportDTO(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("donation_date"),
                        rs.getBigDecimal("donated")
                )
        );

        String totalQuery = "SELECT total_collected FROM nbp03.REPORT_CAMPAIGN_TOTALS WHERE campaign_id = ?";

        List<BigDecimal> totals = jdbcTemplate.query(
                totalQuery,
                new Object[]{campaignId},
                (rs, rowNum) -> rs.getBigDecimal("total_collected")
        );

        BigDecimal totalCollected = totals.isEmpty() ? BigDecimal.ZERO : totals.get(0);


        for (CampaignDonationReportDTO dto : donations) {
            dto.setCampaignTotal(totalCollected);
        }

        return donations;
    }
    public UserDonationReportDTO getUserFullDonationReportData(Long userId) {
        String sql = """
        SELECT 
            c.id AS campaign_id,
            c.name AS campaign_name,
            d.donation_date,
            d.amount,
            ru.subtotal,
            rut.total_donated
        FROM donation d
        JOIN campaign c ON d.campaign_id = c.id
        JOIN REPORT_USER_CAMPAIGN_TOTALS ru 
            ON ru.user_id = d.user_id AND ru.campaign_id = d.campaign_id
        JOIN REPORT_USER_TOTALS rut 
            ON rut.user_id = d.user_id
        WHERE d.user_id = ?
        ORDER BY c.id, d.donation_date
    """;

        List<CampaignDonationGroupDTO> campaignGroups = new ArrayList<>();
        Map<Long, CampaignDonationGroupDTO> campaignMap = new LinkedHashMap<>();
        AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.ZERO);

        jdbcTemplate.query(sql, new Object[]{userId}, rs -> {
            Long campaignId = rs.getLong("campaign_id");
            String campaignName = rs.getString("campaign_name");
            LocalDate date = rs.getDate("donation_date").toLocalDate();
            BigDecimal amount = rs.getBigDecimal("amount");
            BigDecimal subtotal = rs.getBigDecimal("subtotal");
            BigDecimal totalDonated = rs.getBigDecimal("total_donated");

            total.set(totalDonated);

            DonationEntryDTO donation = new DonationEntryDTO(date, amount);

            CampaignDonationGroupDTO group = campaignMap.get(campaignId);
            if (group == null) {
                group = new CampaignDonationGroupDTO(campaignName, new ArrayList<>(), subtotal);
                campaignMap.put(campaignId, group);
                campaignGroups.add(group);
            }

            group.getDonations().add(donation);
        });

        return new UserDonationReportDTO(campaignGroups, total.get());
    }




    public byte[] generateCampaignReportPdf(Long campaignId) {
        List<CampaignDonationReportDTO> donations = getDonationsByCampaign(campaignId);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        Campaign campaign = campaignRepo.getReferenceById(campaignId);
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph("Donations Report for Campaign: " + campaign.getName()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.addCell("First Name");
            table.addCell("Last Name");
            table.addCell("Date");
            table.addCell("Amount Donated");

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            for (CampaignDonationReportDTO dto : donations) {
                table.addCell(dto.getFirstName());
                table.addCell(dto.getLastName());
                table.addCell(dto.getDate() != null ? formatter.format(dto.getDate()) : "");
                table.addCell(dto.getDonated().toPlainString());
            }

            if (!donations.isEmpty()) {
                BigDecimal total = donations.get(0).getCampaignTotal();
                table.addCell("TOTAL");
                table.addCell("");
                table.addCell("");
                table.addCell(total.toPlainString());
            }

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    public byte[] generateUserFullDonationReport(Long userId) {
        UserDonationReportDTO reportData = getUserFullDonationReportData(userId);
        User user = userRepo.getReferenceById(userId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("User Donation Report"));
            document.add(new Paragraph("User: " + user.getFirstName() + " " + user.getLastName()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(3);
            table.addCell("Campaign Name");
            table.addCell("Date");
            table.addCell("Amount");

            for (CampaignDonationGroupDTO campaign : reportData.getCampaigns()) {
                for (DonationEntryDTO donation : campaign.getDonations()) {
                    table.addCell( campaign.getCampaignName());
                    table.addCell(donation.getDate().toString());
                    table.addCell(donation.getAmount().toPlainString());
                }

                // Add subtotal row
                table.addCell("");
                table.addCell("");
                table.addCell(campaign.getSubtotal().toPlainString());
            }

            // Add total row
            table.addCell("TOTAL");
            table.addCell("");
            table.addCell(reportData.getTotal().toPlainString());

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return out.toByteArray();
    }




}

