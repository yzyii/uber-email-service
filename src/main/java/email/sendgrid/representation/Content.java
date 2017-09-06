
package email.sendgrid.representation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "value"
})
/**
 * Auto-generated JSON representation of the SendGrid payload + constituents via the jsonschema2pojo library (http://www.jsonschema2pojo.org/).
 */
public class Content {

    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private String value;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

}
