package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.dto.UserDTO;
import ba.unsa.etf.nbp.DonationPlatform.dto.VolunteerShiftDTO;
import ba.unsa.etf.nbp.DonationPlatform.enums.RoleName;
import ba.unsa.etf.nbp.DonationPlatform.mapper.VolunteerShiftMapper;
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

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

    private final VolunteerShiftMapper vShiftMapper;
    @Autowired
    private UserService userService;

    @Autowired
    public VolunteerService(VolunteerShiftMapper mapper) {

        this.vShiftMapper = mapper;
    }

    public VolunteerShift registerVolunteer(VolunteerShiftDTO shiftDTO, String userEmail) {
        Role newRole = roleRepository.findByName(RoleName.VOLONTER.name())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Event event = eventRepository.findById(shiftDTO.getEventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        if (shiftDTO.getShiftStart() == null || shiftDTO.getShiftEnd() == null) {
            throw new IllegalArgumentException("Start time and end time must be provided.");
        }
        if (shiftDTO.getShiftStart().isAfter(shiftDTO.getShiftEnd())) {
            throw new IllegalArgumentException("Shift start time cannot be after end time.");
        }

        user.setRole(newRole);
        userRepository.save(user);

        VolunteerShift shift = vShiftMapper.toEntity(shiftDTO, user, event);
        return volunteerRepository.save(shift);
    }
    public VolunteerShiftDTO addHoursWorked(Long volunteerShiftId, int additionalHours) {
        if (additionalHours < 0) {
            throw new IllegalArgumentException("Additional hours cannot be negative.");
        }
        VolunteerShift shift = volunteerRepository.findById(volunteerShiftId)
                .orElseThrow(() -> new EntityNotFoundException("Volunteer shift not found"));
        shift.setHoursWorked(shift.getHoursWorked()+additionalHours);
        volunteerRepository.save(shift);
    return vShiftMapper.toDTO(shift);
    }

    public VolunteerShiftDTO editShiftTime(Long shiftId, LocalTime shiftStart, LocalTime shiftEnd) {
        if (shiftStart.isAfter(shiftEnd)) {
            throw new IllegalArgumentException("Shift start time cannot be after end time.");
        }

        VolunteerShift shift = volunteerRepository.findById(shiftId)
                .orElseThrow(() -> new EntityNotFoundException("Volunteer shift not found"));
        shift.setShiftStart(shiftStart);
        shift.setShiftEnd(shiftEnd);

        volunteerRepository.save(shift);
        return vShiftMapper.toDTO(shift);
    }
    public long countVolunteersByEventId(Long eventId) {
        // Implementiraj logiku, npr. koristi repository da izbrojiš volontere
        return volunteerRepository.findByEventId(eventId).size();
    }

    public List<VolunteerShift> getUserShifts(String userEmail) {
        Optional<User> currentUser = userRepository.findByEmail(userEmail);
        if(currentUser.isPresent()) {
            return volunteerRepository.findByUser_Id(currentUser.get().getId());
        }
        else {
            throw new EntityNotFoundException("User not found");
        }
    }
}
