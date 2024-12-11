package io.github.innobridge.llmtools.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;

import static io.github.innobridge.llmtools.constants.OllamaConstants.*;

import java.time.Duration;
import java.util.List;

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

    public EmbedRequest(String model, List<String> input) {
        this.model = model;
        this.input = input;
    }
}
