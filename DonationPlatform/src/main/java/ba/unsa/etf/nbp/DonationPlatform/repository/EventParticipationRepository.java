package ba.unsa.etf.nbp.DonationPlatform.repository;

import ba.unsa.etf.nbp.DonationPlatform.model.EventParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {
    List<EventParticipation> findByUserId(Long userId);
    List<EventParticipation> findByEventId(Long eventId);
}
