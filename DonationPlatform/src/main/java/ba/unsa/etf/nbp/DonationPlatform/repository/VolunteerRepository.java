package ba.unsa.etf.nbp.DonationPlatform.repository;

import ba.unsa.etf.nbp.DonationPlatform.model.VolunteerShift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerRepository extends JpaRepository<VolunteerShift, Long> {
    List<VolunteerShift> findByUser_Id(Long userId);
    long countByEventId(Long eventId);
    List<VolunteerShift> findByEventId(Long eventId);
}
