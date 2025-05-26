package ba.unsa.etf.nbp.DonationPlatform.service.email_template;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailTemplateService {

    private final TemplateEngine templateEngine;

    public String buildVolunteerReminderEmail(String volunteerName, String eventTitle, String eventDate, String eventLocation) {
        Context context = new Context();
        context.setVariable("volunteerName", volunteerName);
        context.setVariable("eventTitle", eventTitle);
        context.setVariable("eventDate", eventDate);
        context.setVariable("eventLocation", eventLocation);
        return templateEngine.process("volunteer_reminder", context);
    }
}