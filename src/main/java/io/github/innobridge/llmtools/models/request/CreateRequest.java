package io.github.innobridge.llmtools.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODEL;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODELFILE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.STREAM;
import static io.github.innobridge.llmtools.constants.OllamaConstants.QUANTIZE;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateRequest {
    @NotNull
    @JsonProperty(MODEL)
    private final String model;
    
    @JsonProperty(MODELFILE)
    private final String modelfile;
    
    @JsonProperty(STREAM)
    private Boolean stream;
    
    @JsonProperty(QUANTIZE)
    private final String quantize;

    public static Builder builder(String model) {
        return new Builder(model);
    }

    public static class Builder {
        private final String model;
        private String modelfile;
        private Boolean stream;
        private String quantize;

        private Builder(String model) {
            if (model == null) {
                throw new IllegalArgumentException("model is required");
            }
            this.model = model;
        }

        public Builder modelfile(String modelfile) {
            this.modelfile = modelfile;
            return this;
        }

        public Builder stream(Boolean stream) {
            this.stream = stream;
            return this;
        }

        public Builder quantize(String quantize) {
            this.quantize = quantize;
            return this;
        }

        public CreateRequest build() {
            return new CreateRequest(this);
        }
    }

    private CreateRequest(Builder builder) {
        this.model = builder.model;
        this.modelfile = builder.modelfile;
        this.stream = builder.stream;
        this.quantize = builder.quantize;
    }

    public CreateRequest setStream(Boolean stream) {
        this.stream = stream;
        return this;
    }

    public String getModel() {
        return model;
    }

    public String getModelfile() {
        return modelfile;
    }

    public Boolean getStream() {
        return stream;
    }

    public String getQuantize() {
        return quantize;
    }
}
