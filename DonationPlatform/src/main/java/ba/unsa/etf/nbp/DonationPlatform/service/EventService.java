package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.model.Event;
import ba.unsa.etf.nbp.DonationPlatform.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event updateEvent(Long id, Event eventDetails) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        existing.setTitle(eventDetails.getTitle());
        existing.setEventStart(eventDetails.getEventStart());
        existing.setEventEnd(eventDetails.getEventEnd());
        existing.setLocation(eventDetails.getLocation());
        existing.setDescription(eventDetails.getDescription());
        existing.setVolunteerGoal(eventDetails.getVolunteerGoal());
        existing.setDonationGoal(eventDetails.getDonationGoal());

        return eventRepository.save(existing);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        eventRepository.delete(event);
    }
}