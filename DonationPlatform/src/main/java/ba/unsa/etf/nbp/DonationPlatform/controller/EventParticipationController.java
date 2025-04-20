package ba.unsa.etf.nbp.DonationPlatform.controller;

import ba.unsa.etf.nbp.DonationPlatform.dto.EventParticipationDTO;
import ba.unsa.etf.nbp.DonationPlatform.service.EventParticipationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-participations")
public class EventParticipationController {

    private final EventParticipationService service;

    public EventParticipationController(EventParticipationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EventParticipationDTO> create(@RequestBody EventParticipationDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public List<EventParticipationDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventParticipationDTO> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public List<EventParticipationDTO> getByUser(@PathVariable Long userId) {
        return service.getByUser(userId);
    }

    @GetMapping("/event/{eventId}")
    public List<EventParticipationDTO> getByEvent(@PathVariable Long eventId) {
        return service.getByEvent(eventId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventParticipationDTO> update(@PathVariable Long id, @RequestBody EventParticipationDTO dto) {
        try {
            return ResponseEntity.ok(service.update(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
