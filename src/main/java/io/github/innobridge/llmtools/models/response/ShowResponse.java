package io.github.innobridge.llmtools.models.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;  // Changed from LocalDateTime
import java.util.List;
import java.util.Map;

import static io.github.innobridge.llmtools.constants.OllamaConstants.LICENSE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODELFILE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PARAMETERS;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TEMPLATE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.SYSTEM;
import static io.github.innobridge.llmtools.constants.OllamaConstants.DETAILS;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MESSAGES;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODEL_INFO;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PROJECTOR_INFO;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODIFIED_AT;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShowResponse {
    @JsonProperty(LICENSE)
    private String license;
    
    @JsonProperty(MODELFILE)
    private String modelfile;
    
    @JsonProperty(PARAMETERS)
    private String parameters;
    
    @JsonProperty(TEMPLATE)
    private String template;
    
    @JsonProperty(SYSTEM)
    private String system;
    
    @JsonProperty(DETAILS)
    private ModelDetails details;
    
    @JsonProperty(MESSAGES)
    private List<Message> messages;
    
    @JsonProperty(MODEL_INFO)
    private Map<String, Object> modelInfo;
    
    @JsonProperty(PROJECTOR_INFO)
    private Map<String, Object> projectorInfo;
    
    @JsonProperty(MODIFIED_AT)
    private ZonedDateTime modifiedAt;

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getModelfile() {
        return modelfile;
    }

    public void setModelfile(String modelfile) {
        this.modelfile = modelfile;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public ModelDetails getDetails() {
        return details;
    }

    public void setDetails(ModelDetails details) {
        this.details = details;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Map<String, Object> getModelInfo() {
        return modelInfo;
    }

    public void setModelInfo(Map<String, Object> modelInfo) {
        this.modelInfo = modelInfo;
    }

    public Map<String, Object> getProjectorInfo() {
        return projectorInfo;
    }

    public void setProjectorInfo(Map<String, Object> projectorInfo) {
        this.projectorInfo = projectorInfo;
    }

    public ZonedDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
