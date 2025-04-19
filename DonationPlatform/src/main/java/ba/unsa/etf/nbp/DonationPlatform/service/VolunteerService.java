package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.dto.VolunteerShiftDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.Event;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.model.VolunteerShift;
import ba.unsa.etf.nbp.DonationPlatform.repository.EventRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.UserRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.VolunteerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public VolunteerService(VolunteerRepository volunteerRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.volunteerRepository = volunteerRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }


    public VolunteerShift registerVolunteer(VolunteerShiftDTO shiftDTO) {
        User user = userRepository.findById(shiftDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Event event = eventRepository.findById(shiftDTO.getEventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        if (shiftDTO.getShiftStart() == null || shiftDTO.getShiftEnd() == null) {
            throw new IllegalArgumentException("Start time and end time must be provided.");
        }
        VolunteerShift shift = new VolunteerShift();
        shift.setShiftStart(shiftDTO.getShiftStart());
        shift.setShiftEnd(shiftDTO.getShiftEnd());
        shift.setHoursWorked(shiftDTO.getHoursWorked());
        shift.setUser(user);
        shift.setEvent(event);

        return volunteerRepository.save(shift);
    }

    public List<VolunteerShift> getUserShifts(Long userId) {
        return volunteerRepository.findByUserId(userId);
    }
}
