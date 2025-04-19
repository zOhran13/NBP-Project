package ba.unsa.etf.nbp.DonationPlatform.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
@Getter
@Setter
public class VolunteerShiftDTO {
    private LocalTime shiftStart;
    private LocalTime  shiftEnd;
    private Long hoursWorked;
    private Long userId;
    private Long eventId;
}
