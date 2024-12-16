package io.github.innobridge.llmtools.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODEL;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODELFILE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.STREAM;
import static io.github.innobridge.llmtools.constants.OllamaConstants.QUANTIZE;

/**
 * Request model for creating Ollama model instances.
 */
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

    /**
     * Creates a new builder instance for CreateRequest.
     * @param model The model name (required)
     * @return A new Builder instance
     */
    public static Builder builder(String model) {
        return new Builder(model);
    }

    /**
     * Builder class for CreateRequest.
     */
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

        /**
         * Sets the modelfile content.
         * @param modelfile The modelfile content
         * @return The builder instance
         */
        public Builder modelfile(String modelfile) {
            this.modelfile = modelfile;
            return this;
        }

        /**
         * Sets the stream flag.
         * @param stream Whether to stream the response
         * @return The builder instance
         */
        public Builder stream(Boolean stream) {
            this.stream = stream;
            return this;
        }

        /**
         * Sets the quantize parameter.
         * @param quantize The quantization level
         * @return The builder instance
         */
        public Builder quantize(String quantize) {
            this.quantize = quantize;
            return this;
        }

        /**
         * Builds the CreateRequest instance.
         * @return A new CreateRequest instance
         */
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

    /**
     * Sets the stream flag.
     * @param stream Whether to stream the response
     * @return This CreateRequest instance
     */
    public CreateRequest setStream(Boolean stream) {
        this.stream = stream;
        return this;
    }

    /**
     * Gets the model name.
     * @return The model name
     */
    public String getModel() {
        return model;
    }

    /**
     * Gets the modelfile content.
     * @return The modelfile content
     */
    public String getModelfile() {
        return modelfile;
    }

    /**
     * Gets the stream flag.
     * @return The stream flag
     */
    public Boolean getStream() {
        return stream;
    }

    /**
     * Gets the quantize parameter.
     * @return The quantization level
     */
    public String getQuantize() {
        return quantize;
    }
}
