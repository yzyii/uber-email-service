package email.sendgrid;

import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import email.EmailConsumer;
import email.EmailMessage;
import email.sendgrid.representation.Content;
import email.sendgrid.representation.From;
import email.sendgrid.representation.Personalization;
import email.sendgrid.representation.SendGridPayload;
import email.sendgrid.representation.EmailAddress;

/**
 * Implementation of the RabbitMQ listener for the SendGrid email service.
 */
@Component
public class SendGridConsumer extends EmailConsumer {
    
    @Override
    public Client initClient() {
        return ClientBuilder.newClient();
    }
    
    @Override
    public Builder getRequestBuilder() {
        String sendGridURL = env.getProperty("sendgrid.address");
        
        return client
                .target(sendGridURL)
                .request()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + env.getProperty("sendgrid.key"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    }
    
    @Override
    public Entity<?> createEntity(EmailMessage message) {
        SendGridPayload payload = new SendGridPayload();
        
        Personalization personalization = new Personalization();
        payload.getPersonalizations().add(personalization);
        
        if (message.getTos().size() > 0) {
        	personalization.setTo(new ArrayList<>());
        }
        for (String toString : message.getTos()) {
            EmailAddress to = new EmailAddress();
            to.setEmail(toString);
            personalization.getTo().add(to);
        }
        if (message.getCcs().size() > 0) {
        	personalization.setCc(new ArrayList<>());
        }
        for (String ccString : message.getCcs()) {
            EmailAddress cc = new EmailAddress();
            cc.setEmail(ccString);
            personalization.getCc().add(cc);
        }
        if (message.getBccs().size() > 0) {
        	personalization.setBcc(new ArrayList<>());
        }
        for (String bccString : message.getBccs()) {
            EmailAddress bcc = new EmailAddress();
            bcc.setEmail(bccString);
            personalization.getBcc().add(bcc);
        }
        
        From from = new From();
        from.setEmail(message.getFrom());
        payload.setFrom(from);
        
        payload.setSubject(message.getSubject());
        
        Content content = new Content();
        content.setType(MediaType.TEXT_PLAIN);
        content.setValue(message.getContent());
        payload.getContent().add(content);
        
        return Entity.json(payload);
    }
}
