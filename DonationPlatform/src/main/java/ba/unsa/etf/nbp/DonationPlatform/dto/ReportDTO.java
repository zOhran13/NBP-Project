package ba.unsa.etf.nbp.DonationPlatform.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;

@Data
@Getter
@Setter
public class ReportDTO {
    private Long id;
    private String type;
    private String content;
    private Date generatedDate;
    private Long campaignId;
}
