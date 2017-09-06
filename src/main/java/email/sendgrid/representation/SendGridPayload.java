
package email.sendgrid.representation;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "personalizations",
    "from",
    "subject",
    "content"
})
/**
 * Auto-generated JSON representation of the SendGrid payload + constituents via the jsonschema2pojo library (http://www.jsonschema2pojo.org/).
 */
public class SendGridPayload {

    @JsonProperty("personalizations")
    private List<Personalization> personalizations = new ArrayList<>();
    @JsonProperty("from")
    private From from;
    @JsonProperty("subject")
    private String subject;
    @JsonProperty("content")
    private List<Content> content = new ArrayList<>();

    @JsonProperty("personalizations")
    public List<Personalization> getPersonalizations() {
        return personalizations;
    }

    @JsonProperty("personalizations")
    public void setPersonalizations(List<Personalization> personalizations) {
        this.personalizations = personalizations;
    }

    @JsonProperty("from")
    public From getFrom() {
        return from;
    }

    @JsonProperty("from")
    public void setFrom(From from) {
        this.from = from;
    }

    @JsonProperty("subject")
    public String getSubject() {
        return subject;
    }

    @JsonProperty("subject")
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @JsonProperty("content")
    public List<Content> getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(List<Content> content) {
        this.content = content;
    }

}
