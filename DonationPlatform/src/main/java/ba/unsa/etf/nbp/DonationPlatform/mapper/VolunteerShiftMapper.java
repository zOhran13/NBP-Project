package ba.unsa.etf.nbp.DonationPlatform.mapper;

import ba.unsa.etf.nbp.DonationPlatform.dto.VolunteerShiftDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.Event;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.model.VolunteerShift;
import org.springframework.stereotype.Component;

@Component
public class VolunteerShiftMapper {

    public VolunteerShiftDTO toDTO(VolunteerShift shift) {
        if (shift == null) return null;

        VolunteerShiftDTO dto = new VolunteerShiftDTO();
        dto.setShiftStart(shift.getShiftStart());
        dto.setShiftEnd(shift.getShiftEnd());
        dto.setHoursWorked(shift.getHoursWorked());

        if (shift.getUser() != null) {
            dto.setUserId(shift.getUser().getId());
        }

        if (shift.getEvent() != null) {
            dto.setEventId(shift.getEvent().getId());
        }

        return dto;
    }

    public VolunteerShift toEntity(VolunteerShiftDTO dto, User user, Event event) {
        if (dto == null) return null;

        VolunteerShift shift = new VolunteerShift();
        shift.setShiftStart(dto.getShiftStart());
        shift.setShiftEnd(dto.getShiftEnd());
        shift.setHoursWorked(dto.getHoursWorked());
        shift.setUser(user);
        shift.setEvent(event);

        return shift;
    }
}