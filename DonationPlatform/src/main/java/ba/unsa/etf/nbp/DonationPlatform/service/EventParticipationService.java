package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.dto.EventParticipationDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.Event;
import ba.unsa.etf.nbp.DonationPlatform.model.EventParticipation;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.repository.EventParticipationRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.EventRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventParticipationService {

    private final EventParticipationRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventParticipationService(EventParticipationRepository repository,
                                     EventRepository eventRepository,
                                     UserRepository userRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public EventParticipationDTO toDTO(EventParticipation ep) {
        EventParticipationDTO dto = new EventParticipationDTO();
        dto.setId(ep.getId());
        dto.setStatus(ep.getStatus());
        dto.setEventId(ep.getEvent().getId());
        dto.setUserId(ep.getUser().getId());
        return dto;
    }

    public EventParticipation toEntity(EventParticipationDTO dto) {
        EventParticipation ep = new EventParticipation();
        ep.setId(dto.getId());
        ep.setStatus(dto.getStatus());

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ep.setEvent(event);
        ep.setUser(user);
        return ep;
    }

    public EventParticipationDTO create(EventParticipationDTO dto) {
        EventParticipation saved = repository.save(toEntity(dto));
        return toDTO(saved);
    }

    public List<EventParticipationDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public EventParticipationDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Participation not found"));
    }

    public List<EventParticipationDTO> getByUser(Long userId) {
        return repository.findByUserId(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<EventParticipationDTO> getByEvent(Long eventId) {
        return repository.findByEventId(eventId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public EventParticipationDTO update(Long id, EventParticipationDTO dto) {
        EventParticipation existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participation not found"));

        existing.setStatus(dto.getStatus());

        if (!existing.getEvent().getId().equals(dto.getEventId())) {
            existing.setEvent(eventRepository.findById(dto.getEventId())
                    .orElseThrow(() -> new RuntimeException("Event not found")));
        }

        if (!existing.getUser().getId().equals(dto.getUserId())) {
            existing.setUser(userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found")));
        }

        return toDTO(repository.save(existing));
    }

    public void delete(Long id) {
        EventParticipation ep = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participation not found"));
        repository.delete(ep);
    }
}
