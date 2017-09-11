package email;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

/**
 * Houses the email REST service endpoint exposed to the web frontend.
 */
@Controller
@Path("/email")
public class EmailResource {
    
    private final Logger log = LoggerFactory.getLogger(EmailResource.class);
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * Endpoint to accept email requests from the web frontend.
     * The received JSON is validated against RFC 2822 via the JavaMail API.
     * 
     * Note that JavaMail does not cover all cases and some addresses may still be invalid.
     * e.g. test@test is apparently a valid address.
     * 
     * @return {@link Status#OK} - if the {@link EmailMessage} is valid and has been queued.
     * @return {@link Status#BAD_REQUEST} - if the {@link EmailMessage} is not valid.
     * @return {@link Status#INTERNAL_SERVER_ERROR} - if a RabbitMQ error has occured.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response email(@NotNull @Valid EmailMessage emailMessage) {
        String error = isValidEmailMessage(emailMessage);
        
        if (StringUtils.isEmpty(error)) {
            log.info("Processing email: " + emailMessage.getSubject());
            rabbitTemplate.convertAndSend(Application.queueName, emailMessage);
            
            return Response.ok().build();
        }
        
        return Response.status(Status.BAD_REQUEST).entity(error).build();
    }
    
    private String isValidEmailMessage(EmailMessage emailMessage) {
        String error = "";
        
        Set<String> duplicateEmailsSet = new HashSet<>();
        
        try {
            validate(emailMessage.getTos(), duplicateEmailsSet);
            validate(emailMessage.getCcs(), duplicateEmailsSet);
            validate(emailMessage.getBccs(), duplicateEmailsSet);
            validate(emailMessage.getFrom());
        } catch (AddressException e) {
            error = e.getMessage();
            log.info("Rejected email: " + e.getMessage());
        }
        
        return error;
    }
    
    /**
     * Validates the list of TO, CC and BCC emails. Emails must be unique within all 3 collections.
     * 
     * @param emails - list of emails to be validated
     * @param emailAddressSet - the set of previously validated emails.
     * @throws AddressException if any email address is not valid or already exists in the emailAddressSet
     */
    private void validate(List<String> emails, Set<String> emailAddressSet) throws AddressException {
        for (String email : emails) {
            validate(email);
            if (!emailAddressSet.contains(email)) {
                emailAddressSet.add(email);
            } else {
                throw new AddressException("Duplicate email detected among addressees.");
            }
        }
    }
    
    private void validate(String email) throws AddressException {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException e) {
            throw new AddressException("Invalid email: [" + email + "].");
        }
    }
}
