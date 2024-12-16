package io.github.innobridge.llmtools.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.List;

import static io.github.innobridge.llmtools.constants.OllamaConstants.*;

/**
 * Response model for embedding operations.
 * Contains the embedding results along with performance metrics.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbedResponse {
    /** The model used for generating embeddings */
    @JsonProperty(MODEL)
    private String model;

    /** List of embedding vectors */
    @JsonProperty(EMBEDDINGS)
    private List<float[]> embeddings;

    /** Total time taken for the embedding operation */
    @JsonProperty(TOTAL_DURATION)
    private Duration totalDuration;

    /** Time taken to load the model */
    @JsonProperty(LOAD_DURATION)
    private Duration loadDuration;

    /** Number of prompt evaluations performed */
    @JsonProperty(PROMPT_EVAL_COUNT)
    private Integer promptEvalCount;
}
