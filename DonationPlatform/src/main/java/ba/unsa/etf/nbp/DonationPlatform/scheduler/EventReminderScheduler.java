package ba.unsa.etf.nbp.DonationPlatform.scheduler;

import ba.unsa.etf.nbp.DonationPlatform.enums.RoleName;
import ba.unsa.etf.nbp.DonationPlatform.model.Event;
import ba.unsa.etf.nbp.DonationPlatform.model.EventParticipation;
import ba.unsa.etf.nbp.DonationPlatform.model.Role;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.repository.EventParticipationRepository;
import ba.unsa.etf.nbp.DonationPlatform.service.EmailService;
import ba.unsa.etf.nbp.DonationPlatform.service.email_template.EmailTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class EventReminderScheduler {

    private final EventParticipationRepository eventParticipationRepository;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

    // Pokreće se svaki dan u 00:00 (ponoć)
    @Scheduled(cron = "0 0 0 * * *")
    public void sendVolunteerReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        System.out.println("Provjeravam događaje zakazane za: " + tomorrow.format(DATE_FORMATTER));

        List<EventParticipation> participationsForTomorrow = eventParticipationRepository.findAll().stream()
                .filter(ep -> ep.getEvent().getEventStart() != null &&
                        ep.getEvent().getEventStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(tomorrow)) // Dodana provjera
                .collect(Collectors.toList());

        if (participationsForTomorrow.isEmpty()) {
            System.out.println("Nema događaja za sutra ili su podsjetnici već poslani.");
            return;
        }

        System.out.println("Pronađeno " + participationsForTomorrow.size() + " prijava za sutra kojima treba podsjetnik. Slanje emailova...");

        for (EventParticipation participation : participationsForTomorrow) {
            Event event = participation.getEvent();
            User volunteer = participation.getUser();

                String volunteerName = volunteer.getFirstName() + " " + volunteer.getLastName();
                String eventTitle = event.getTitle();
                String eventDate = event.getEventStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DATE_FORMATTER);
                String eventLocation = event.getLocation();
                String volunteerEmail = volunteer.getEmail();

                String emailBody = emailTemplateService.buildVolunteerReminderEmail(
                        volunteerName, eventTitle, eventDate, eventLocation
                );

                emailService.sendHtmlEmail(volunteerEmail, "Podsjetnik: Tvoj volonterski događaj sutra!", emailBody);
        }
        System.out.println("Završeno slanje podsjetnika volonterima za sutra.");
    }
}