package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.dto.VolunteerShiftDTO;
import ba.unsa.etf.nbp.DonationPlatform.enums.RoleName;
import ba.unsa.etf.nbp.DonationPlatform.model.Event;
import ba.unsa.etf.nbp.DonationPlatform.model.Role;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.model.VolunteerShift;
import ba.unsa.etf.nbp.DonationPlatform.repository.EventRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.RoleRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.UserRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.VolunteerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VolunteerService {
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Transactional
    public VolunteerShift registerVolunteer(VolunteerShiftDTO shiftDTO) {
        User user = userRepository.findById(shiftDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Event event = eventRepository.findById(shiftDTO.getEventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        if (shiftDTO.getShiftStart() == null || shiftDTO.getShiftEnd() == null) {
            throw new IllegalArgumentException("Start time and end time must be provided.");
        }

        Role newRole = roleRepository.findByName(RoleName.VOLONTER.name())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(newRole);
        userRepository.save(user);
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
