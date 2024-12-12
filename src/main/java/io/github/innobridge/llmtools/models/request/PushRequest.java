package io.github.innobridge.llmtools.models.request;

import static io.github.innobridge.llmtools.constants.OllamaConstants.MODEL;
import static io.github.innobridge.llmtools.constants.OllamaConstants.INSECURE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.STREAM;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PushRequest {
    @JsonProperty(MODEL)
    private final String model;
    
    @JsonProperty(INSECURE)
    private final Boolean insecure;
    
    @JsonProperty(STREAM)
    private final Boolean stream;

    private PushRequest(Builder builder) {
        this.model = builder.model;
        this.insecure = builder.insecure;
        this.stream = builder.stream;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getModel() { return model; }
    public Boolean getInsecure() { return insecure; }
    public Boolean getStream() { return stream; }

    public PushRequest setStream(boolean stream) {
        return PushRequest.builder()
                .model(this.model)
                .insecure(this.insecure)
                .stream(stream)
                .build();
    }

    public static class Builder {
        private String model;
        private Boolean insecure;
        private Boolean stream;

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder insecure(Boolean insecure) {
            this.insecure = insecure;
            return this;
        }

        public Builder stream(Boolean stream) {
            this.stream = stream;
            return this;
        }

        public PushRequest build() {
            if (model == null) {
                throw new IllegalStateException("Model is required");
            }
            return new PushRequest(this);
        }
    }
}
