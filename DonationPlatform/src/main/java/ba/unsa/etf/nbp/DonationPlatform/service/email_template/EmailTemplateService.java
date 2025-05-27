package ba.unsa.etf.nbp.DonationPlatform.service.email_template;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.text.SimpleDateFormat;
@Service
@AllArgsConstructor
public class EmailTemplateService {

    private final TemplateEngine templateEngine;
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy.");
    public String buildVolunteerReminderEmail(String volunteerName, String eventTitle, String eventDate, String eventLocation) {
        Context context = new Context();
        context.setVariable("volunteerName", volunteerName);
        context.setVariable("eventTitle", eventTitle);
        context.setVariable("eventDate", eventDate);
        context.setVariable("eventLocation", eventLocation);
        return templateEngine.process("volunteer_reminder", context);
    }
    public String buildNewCampaignNotificationEmail(String userName, String campaignName, String campaignImageLink, Double campaignTargetAmount, String campaignLink, Date startDate, Date endDate) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("campaignName", campaignName);
        context.setVariable("campaignImageLink", campaignImageLink);
        context.setVariable("campaignTargetAmount", campaignTargetAmount); 
        context.setVariable("campaignLink", campaignLink);

        // Formatiramo datume u stringove prije slanja u template
        if (startDate != null) {
            context.setVariable("campaignStartDate", DATE_FORMATTER.format(startDate));
        } else {
            context.setVariable("campaignStartDate", "Nije specificiran");
        }
        if (endDate != null) {
            context.setVariable("campaignEndDate", DATE_FORMATTER.format(endDate));
        } else {
            context.setVariable("campaignEndDate", "Nije specificiran");
        }
        return templateEngine.process("new_campaign_notification.html", context);
    }
}

