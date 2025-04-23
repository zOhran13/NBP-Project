package ba.unsa.etf.nbp.DonationPlatform.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class EventParticipationDTO {
    private Long id;
    private String status;
    private Long eventId;
    private Long userId;
}
