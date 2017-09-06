package email.mailgun;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.springframework.stereotype.Component;

import email.EmailConsumer;
import email.EmailMessage;

/**
 * Implementation of the RabbitMQ listener for the Mailgun email service.
 */
@Component
public class MailgunConsumer extends EmailConsumer {
    
    @Override
    public Client initClient() {
        return ClientBuilder.newClient().register(HttpAuthenticationFeature.basic("api", env.getProperty("mailgun.key")));
    }
    
    @Override
    public Builder getRequestBuilder() {
        String mailgunURL = env.getProperty("mailgun.address") + env.getProperty("mailgun.domain") + "/messages";
        
        return client
                .target(mailgunURL)
                .request();
    }
    
    @Override
    public Entity<?> createEntity(EmailMessage message) {
        Form formData = new Form();
        formData.param("from", message.getFrom());
        for (String to : message.getTos()) {
            formData.param("to", to);
        }
        for (String cc : message.getCcs()) {
            formData.param("cc", cc);
        }
        for (String bcc : message.getBccs()) {
            formData.param("bcc", bcc);
        }
        formData.param("subject", message.getSubject());
        formData.param("text", message.getContent());
        
        return Entity.entity(formData, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
    }
}
