package ba.unsa.etf.nbp.DonationPlatform.service;

import ch.qos.logback.classic.Logger;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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

       sendMail(from, subject, toEmail, content);
    }
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        Email from = new Email(senderEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/html", htmlContent);

        sendMail(from, subject, toEmail, content);
    }
    private void sendMail(Email from, String subject, Email to, Content content) {
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("Email poslan na: " + to.getEmail());
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
            System.out.println("Headers: " + response.getHeaders());

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("Email uspješno poslan!");
            } else {
                System.err.println("Greška prilikom slanja emaila. Status kod: " + response.getStatusCode() + ", Odgovor: " + response.getBody());
            }

        } catch (IOException e) {
            System.err.println("Error prilikom slanja e-maila: " + e.getMessage());
            throw new RuntimeException("Neuspješno slanje emaila", e);
        }
    }
}
