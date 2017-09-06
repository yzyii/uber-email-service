package email;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Abstract implementation of the RabbitMQ Listener. Each email service provider must implement their own listener.
 * This base class contains a naive implementation of fail-over and retrying by relying on re-queueing failed messages back onto RabbitMQ.
 * A more sophisticated fail-over implementation could rely on shutting down a listener and pinging the services for their status.
 */
@Component
public abstract class EmailConsumer {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    protected Client client;
    @Autowired
    protected Environment env;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * @return An initialised JAVAX Client with any required registered components. 
     */
    public abstract Client initClient();
    
    /**
     * @return An email service provider specific {@link Request} {@link Builder} containing any required headers.
     */
    public abstract Builder getRequestBuilder();
    
    /**
     * @param message - JSON bean representation of an email to be sent to an email service provider.
     * @return An {@link Entity} to be posted to the email service provider API endpoint.
     */
    public abstract Entity<?> createEntity(EmailMessage message);
    
    @PostConstruct
    public void init(){
        client = initClient();
    }
    
    /**
     * Received a message off RabbitMQ and attempts to send it to the email service provider.
     * Implements a naive fail-over mechanism of re-queueing any failed attempts back onto RabbitMQ
     */
    @RabbitListener(queues = Application.queueName)
    public void receiveMessage(final EmailMessage message) {
        Response response = getRequestBuilder().post(createEntity(message));
        
        log.info(getClass().getSimpleName() + " - Sent: [" + message.getSubject() + "] Response: [(" + response.getStatus() + ") " + response.readEntity(String.class) + "]");
        
        if (!(response.getStatus() == Status.OK.getStatusCode() || response.getStatus() == Status.ACCEPTED.getStatusCode())) {
            log.info(getClass().getSimpleName() + " - Requeuing: [" + message.getSubject() + "]");
            rabbitTemplate.convertAndSend(Application.queueName, message);
        }
    }
}
