package io.github.innobridge.llmtools.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODEL;
import static io.github.innobridge.llmtools.constants.OllamaConstants.SYSTEM;
import static io.github.innobridge.llmtools.constants.OllamaConstants.RAW;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ShowRequest extends Options {
    @JsonProperty(value = MODEL, required = true)
    private String model;
    
    @JsonProperty(SYSTEM)
    private String system;
    
    @JsonProperty(RAW)
    private boolean verbose;

    protected ShowRequest() {
        super();
    }

    public ShowRequest(String model, String system, boolean verbose) {
        super();
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("model is required");
        }
        this.model = model;
        this.system = system;
        this.verbose = verbose;
    }
}
