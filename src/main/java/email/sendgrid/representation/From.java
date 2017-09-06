
package email.sendgrid.representation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "email"
})
/**
 * Auto-generated JSON representation of the SendGrid payload + constituents via the jsonschema2pojo library (http://www.jsonschema2pojo.org/).
 */
public class From {

    @JsonProperty("email")
    private String email;

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

}
