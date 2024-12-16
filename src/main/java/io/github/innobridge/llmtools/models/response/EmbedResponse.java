package io.github.innobridge.llmtools.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.List;

import static io.github.innobridge.llmtools.constants.OllamaConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbedResponse {
    @JsonProperty(MODEL)
    private String model;

    @JsonProperty(EMBEDDINGS)
    private List<float[]> embeddings;

    @JsonProperty(TOTAL_DURATION)
    private Duration totalDuration;

    @JsonProperty(LOAD_DURATION)
    private Duration loadDuration;

    @JsonProperty(PROMPT_EVAL_COUNT)
    private Integer promptEvalCount;
}
