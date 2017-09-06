
package email.sendgrid.representation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "to"
})
/**
 * Auto-generated JSON representation of the SendGrid payload + constituents via the jsonschema2pojo library (http://www.jsonschema2pojo.org/).
 */
public class Personalization {

    @JsonProperty("to")
    private List<EmailAddress> to;
    @JsonProperty("cc")
    private List<EmailAddress> cc;
    @JsonProperty("bcc")
    private List<EmailAddress> bcc;

    @JsonProperty("to")
    public List<EmailAddress> getTo() {
        return to;
    }

    @JsonProperty("to")
    public void setTo(List<EmailAddress> to) {
        this.to = to;
    }

    @JsonProperty("cc")
    public List<EmailAddress> getCc() {
        return cc;
    }

    @JsonProperty("cc")
    public void setCc(List<EmailAddress> cc) {
        this.cc = cc;
    }

    @JsonProperty("bcc")
    public List<EmailAddress> getBcc() {
        return bcc;
    }

    @JsonProperty("bcc")
    public void setBcc(List<EmailAddress> bcc) {
        this.bcc = bcc;
    }

}
