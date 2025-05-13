package ba.unsa.etf.nbp.DonationPlatform.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.sender.email}")
    private String senderEmail;

    public void sendResetPasswordEmail(String to, String resetUrl) {
        Email from = new Email(senderEmail);
        Email toEmail = new Email(to);
        String subject = "Resetuj lozinku";
        String contentString = "<p>Kliknite na sljedeći link da resetujete lozinku:</p>"
                + "<a href=\"" + resetUrl + "\">Resetuj lozinku</a>";
        Content content = new Content("text/html", contentString);

        Mail mail = new Mail(from, subject, toEmail, content);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
            System.out.println("Headers: " + response.getHeaders());

        } catch (IOException e) {
            System.err.println("Greška prilikom slanja e-maila: " + e.getMessage());
        }
    }
}
