package ba.unsa.etf.nbp.DonationPlatform.seed;


import ba.unsa.etf.nbp.DonationPlatform.model.Event;
import ba.unsa.etf.nbp.DonationPlatform.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(5)
public class EventSeeder implements CommandLineRunner {

    private final EventRepository eventRepository;

    public EventSeeder(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) {
            List<Event> events = Arrays.asList(
                    new Event(
                            "Humanitarni koncert 'Niste sami'",
                            "https://balkans.aljazeera.net/wp-content/uploads/2024/10/461994187_945545787592632_4959838251416091077_n-1728026144.jpg?resize=770%2C513&quality=80",
                            getDateTime(2025, Calendar.JUNE, 27, 18, 0),    // startDate with time 18:00
                            getDateTime(2025, Calendar.JUNE, 27, 22, 0),    // endDate with time 22:00
                            "Zenica",
                            "Koncert za pomoć područjima pogođenim poplavama, uz nastupe poznatih regionalnih izvođača.",
                            50L,
                            10000.0
                    ),
                    new Event(
                            "Humanitarna večer 'Za naše ljude'",
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAG6w2uWQv0dmAAEbRQ0Jsq4Zh-mNzvA3U1w&s",
                            getDateTime(2025, Calendar.OCTOBER, 8, 19, 0),
                            getDateTime(2025, Calendar.OCTOBER, 8, 23, 0),
                            "Sarajevo",
                            "TV program za prikupljanje pomoći stanovnicima poplavljenih područja u BiH.",
                            100L,
                            15000.0
                    ),
                    new Event(
                            "Humanitarni bazar za narod Gaze",
                            "https://balkans.aljazeera.net/wp-content/uploads/2023/10/33YU6CJ-highres-1697954569-1697956409.jpg?w=770&resize=770%2C513",
                            getDateTime(2025, Calendar.NOVEMBER, 3, 10, 0),
                            getDateTime(2025, Calendar.NOVEMBER, 3, 17, 0),
                            "Sarajevo",
                            "Bazar u organizaciji Palestinske zajednice u BiH za prikupljanje pomoći narodu Gaze.",
                            20L,
                            5000.0
                    ),
                    new Event(
                            "Humanitarni koncert za liječenje",
                            "https://storage.radiosarajevo.ba/article/578300/871x540/humanitarni-koncert-sloga-foto-admir-kuburovic-radiosarajevo-9-februar-6.jpg?v1739127386",
                            getDateTime(2025, Calendar.OCTOBER, 19, 18, 30),
                            getDateTime(2025, Calendar.OCTOBER, 19, 22, 30),
                            "Gornji Vakuf",
                            "Koncert za prikupljanje sredstava za liječenje naše sugrađanke.",
                            30L,
                            3000.0
                    )
            );

        for (Event newEvent : events) {
            // You need a way to identify events uniquely,
            // for example, by title and start date:
            Optional<Event> existingEventOpt = eventRepository.findByTitleAndEventStart(
                    newEvent.getTitle(), newEvent.getEventStart());

            if (existingEventOpt.isPresent()) {
                Event existingEvent = existingEventOpt.get();
                // Update the existing event's fields
                existingEvent.setImageLink(newEvent.getImageLink());
                existingEvent.setEventEnd(newEvent.getEventEnd());
                existingEvent.setLocation(newEvent.getLocation());
                existingEvent.setDescription(newEvent.getDescription());
                existingEvent.setVolunteerGoal(newEvent.getVolunteerGoal());
                existingEvent.setDonationGoal(newEvent.getDonationGoal());

                eventRepository.save(existingEvent); // save updated event
            } else {
                // Insert new event
                eventRepository.save(newEvent);
            }
        }


    }
    public static Date getDateTime(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}
