package email;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.junit.BrokerRunning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmailResourceTests {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    private String url;

    @ClassRule
    public static BrokerRunning brokerRunning = BrokerRunning.isRunning();
    
    @AfterClass
    public static void tearDown() {
        brokerRunning.deleteQueues(Application.queueName);
    }
    
    @Before
    public void setup() {
        url = "http://localhost:" + port + "/api/email";
    }
    
    @Test
    public void nullPost_BadRequest() throws Exception {
        HttpEntity<EmailMessage> request = new HttpEntity<>(null);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    
    @Test
    public void emptyPost_BadRequest() throws Exception {
        EmailMessage message = new EmailMessage();
        
        HttpEntity<EmailMessage> request = new HttpEntity<>(message);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    
    @Test
    public void invalidEmail_BadRequest() throws Exception {
        EmailMessage message = new EmailMessage();
        message.setFrom("alice");
        List<String> tos = new ArrayList<>();
        tos.add("bob");
        message.setTos(tos);
        message.setSubject("subject");
        message.setContent("content");
        
        HttpEntity<EmailMessage> request = new HttpEntity<>(message);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    
    @Test
    public void MissingTo_BadRequest() throws Exception {
        EmailMessage message = new EmailMessage();
        message.setFrom("Alice <alice@gmail.com>");
        List<String> tos = new ArrayList<>();
        message.setTos(tos);
        List<String> ccs = new ArrayList<>();
        ccs.add("Carol <carol@gmail.com>");
        message.setCcs(ccs);
        message.setSubject("subject");
        message.setContent("content");
        
        HttpEntity<EmailMessage> request = new HttpEntity<>(message);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    
    @Test
    public void MissingSubject_BadRequest() throws Exception {
        EmailMessage message = new EmailMessage();
        message.setFrom("Alice <alice@gmail.com>");
        List<String> tos = new ArrayList<>();
        tos.add("Bob <bob@gmail.com>");
        message.setTos(tos);
        message.setContent("content");
        
        HttpEntity<EmailMessage> request = new HttpEntity<>(message);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    
    @Test
    public void MissingContent_BadRequest() throws Exception {
        EmailMessage message = new EmailMessage();
        message.setFrom("Alice <alice@gmail.com>");
        List<String> tos = new ArrayList<>();
        tos.add("Bob <bob@gmail.com>");
        message.setTos(tos);
        message.setSubject("subject");
        
        HttpEntity<EmailMessage> request = new HttpEntity<>(message);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    
    @Test
    public void DuplicateAddressees_BadRequest() throws Exception {
        EmailMessage message = new EmailMessage();
        message.setFrom("Alice <alice@gmail.com>");
        List<String> tos = new ArrayList<>();
        tos.add("Bob <bob@gmail.com>");
        message.setTos(tos);
        List<String> ccs = new ArrayList<>();
        ccs.add("Bob <bob@gmail.com>");
        ccs.add("Carol <carol@gmail.com>");
        message.setCcs(ccs);
        message.setSubject("subject");
        message.setContent("content");
        
        HttpEntity<EmailMessage> request = new HttpEntity<>(message);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    
    @Test
    public void validPost_OK() throws Exception {
        EmailMessage message = new EmailMessage();
        message.setFrom("Alice <alice@gmail.com>");
        List<String> tos = new ArrayList<>();
        tos.add("Bob <bob@gmail.com>");
        tos.add("Eve <eve@gmail.com>");
        message.setTos(tos);
        List<String> ccs = new ArrayList<>();
        ccs.add("Carol <carol@gmail.com>");
        ccs.add("Frank <frank@gmail.com>");
        message.setCcs(ccs);
        List<String> bccs = new ArrayList<>();
        bccs.add("Dave <dave@gmail.com>");
        bccs.add("Grace <grace@gmail.com>");
        message.setBccs(bccs);
        message.setSubject("subject");
        message.setContent("content");
        
        HttpEntity<EmailMessage> request = new HttpEntity<>(message);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    //Following test cases not done here due to time constraints
    //TODO: Implement a complete end-to-end test via CountDownLatches.
    //TODO: Implement RabbitMQ retry test via CountDownLatches.
}
