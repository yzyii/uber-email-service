package email;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JSON representation of an email received from the web frontend and passed through RabbitMQ.
 */
public class EmailMessage {

    @JsonProperty
    @NotEmpty
    private List<String> tos = new ArrayList<>();
    @JsonProperty
    @NotNull
    private List<String> ccs = new ArrayList<>();
    @JsonProperty
    @NotNull
    private List<String> bccs = new ArrayList<>();
    @JsonProperty
    @NotEmpty
    private String from;
    @JsonProperty
    @NotEmpty
    private String subject;
    @JsonProperty
    @NotEmpty
    private String content;
    @JsonProperty
    @NotNull
    private Integer retryCount = 0;
    
    public EmailMessage() {
    }

    public List<String> getTos() {
        return tos;
    }

    public void setTos(List<String> tos) {
        this.tos = tos;
    }

    public List<String> getCcs() {
        return ccs;
    }

    public void setCcs(List<String> ccs) {
        this.ccs = ccs;
    }

    public List<String> getBccs() {
        return bccs;
    }

    public void setBccs(List<String> bccs) {
        this.bccs = bccs;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }
}
