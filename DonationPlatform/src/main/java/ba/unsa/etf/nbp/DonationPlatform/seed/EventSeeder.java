package ba.unsa.etf.nbp.DonationPlatform.seed;


import ba.unsa.etf.nbp.DonationPlatform.model.Event;
import ba.unsa.etf.nbp.DonationPlatform.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Order(5)
public class EventSeeder implements CommandLineRunner {

    private final EventRepository eventRepository;

    public EventSeeder(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) {
        if (eventRepository.count() == 0) {
            List<Event> events = Arrays.asList(
                    new Event("Humanitarni koncert 'Niste sami'", "https://balkans.aljazeera.net/wp-content/uploads/2024/10/461994187_945545787592632_4959838251416091077_n-1728026144.jpg?resize=770%2C513&quality=80",
                            getDate(2025, Calendar.MAY, 27), "Zenica",
                            "Koncert za pomoć područjima pogođenim poplavama, uz nastupe poznatih regionalnih izvođača.",
                            50L, 10000.0),
                    new Event("Humanitarna večer 'Za naše ljude'", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAG6w2uWQv0dmAAEbRQ0Jsq4Zh-mNzvA3U1w&s",
                            getDate(2024, Calendar.OCTOBER, 8), "Sarajevo",
                            "TV program za prikupljanje pomoći stanovnicima poplavljenih područja u BiH.",
                            100L, 15000.0),
                    new Event("Humanitarni bazar za narod Gaze", "https://balkans.aljazeera.net/wp-content/uploads/2023/10/33YU6CJ-highres-1697954569-1697956409.jpg?w=770&resize=770%2C513",
                            getDate(2023, Calendar.NOVEMBER, 3), "Sarajevo",
                            "Bazar u organizaciji Palestinske zajednice u BiH za prikupljanje pomoći narodu Gaze.",
                            20L, 5000.0),
                    new Event("Humanitarni koncert za liječenje", "https://storage.radiosarajevo.ba/article/578300/871x540/humanitarni-koncert-sloga-foto-admir-kuburovic-radiosarajevo-9-februar-6.jpg?v1739127386",
                            getDate(2024, Calendar.OCTOBER, 19), "Gornji Vakuf",
                            "Koncert za prikupljanje sredstava za liječenje naše sugrađanke.",
                            30L, 3000.0)
            );

            eventRepository.saveAll(events);
            System.out.println("Seeded events.");
        } else {
            System.out.println("Events already present, skipping seeding.");
        }
    }
    private Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
