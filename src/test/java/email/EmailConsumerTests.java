package email;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import email.mailgun.MailgunConsumer;
import email.sendgrid.SendGridConsumer;
import email.sendgrid.representation.SendGridPayload;

public class EmailConsumerTests {
    
    ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void sendGrid_createEntity() throws Exception {
        
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("EmailMessage.json");
        EmailMessage emailMessage = mapper.readValue(input, EmailMessage.class);
        
        SendGridConsumer consumer = new SendGridConsumer();
        SendGridPayload payloadObject = (SendGridPayload)consumer.createEntity(emailMessage).getEntity();
        String payload = mapper.writeValueAsString(payloadObject);
        
        InputStream output = this.getClass().getClassLoader().getResourceAsStream("SendGridPayload.json");
        SendGridPayload payloadObjectExpected = mapper.readValue(output, SendGridPayload.class);
        String payloadExpected = mapper.writeValueAsString(payloadObjectExpected);
        
        assertThat(payload).isEqualTo(payloadExpected);
    }
    
    @Test
    public void mailgun_createEntity() throws Exception {
        
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("EmailMessage.json");
        EmailMessage emailMessage = mapper.readValue(input, EmailMessage.class);
        
        MailgunConsumer consumer = new MailgunConsumer();
        Form payloadObject = (Form)consumer.createEntity(emailMessage).getEntity();
        
        MultivaluedMap<String, String> payload = payloadObject.asMap();
        assertThat(payload.get("to").get(0)).isEqualTo("Alice <alice@gmail.com>");
        assertThat(payload.get("to").get(1)).isEqualTo("Bob <bob@gmail.com>");
        assertThat(payload.get("cc").get(0)).isEqualTo("Carol <carol@gmail.com>");
        assertThat(payload.get("cc").get(1)).isEqualTo("Dave <dave@gmail.com>");
        assertThat(payload.get("bcc").get(0)).isEqualTo("Eve <eve@gmail.com>");
        assertThat(payload.get("bcc").get(1)).isEqualTo("Frank <frank@gmail.com>");
        assertThat(payload.get("from").get(0)).isEqualTo("Grace <grace@gmail.com>");
        assertThat(payload.get("subject").get(0)).isEqualTo("Jackdaws love my big sphinx of quartz.");
        assertThat(payload.get("text").get(0)).isEqualTo("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
    }
}
