package io.github.innobridge.llmtools.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;

import static io.github.innobridge.llmtools.constants.OllamaConstants.*;

import java.time.Duration;
import java.util.List;

/**
 * Request class for embedding operations.
 * This class encapsulates the parameters needed for generating embeddings from input text
 * using specified models.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbedRequest extends Options {
    @JsonProperty(MODEL)
    private String model;

    @JsonProperty(INPUT)
    private List<String> input;

    @JsonProperty(KEEP_ALIVE)
    private Duration keepAlive;

    @JsonProperty(TRUNCATE)
    private Boolean truncate;

    /**
     * Constructs an EmbedRequest with specified model and input.
     *
     * @param model The name of the model to use for embedding
     * @param input The list of text inputs to generate embeddings for
     */
    public EmbedRequest(String model, List<String> input) {
        this.model = model;
        this.input = input;
    }
}
