package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.model.VolunteerShift;
import ba.unsa.etf.nbp.DonationPlatform.repository.VolunteerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    public VolunteerShift registerVolunteer(VolunteerShift shift) {
        return volunteerRepository.save(shift);
    }

    public List<VolunteerShift> getUserShifts(Long userId) {
        return volunteerRepository.findByUserId(userId);
    }
}
