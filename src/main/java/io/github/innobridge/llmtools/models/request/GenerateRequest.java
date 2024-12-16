package io.github.innobridge.llmtools.models.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODEL;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PROMPT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.SUFFIX;
import static io.github.innobridge.llmtools.constants.OllamaConstants.SYSTEM;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TEMPLATE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.CONTEXT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.STREAM;
import static io.github.innobridge.llmtools.constants.OllamaConstants.RAW;
import static io.github.innobridge.llmtools.constants.OllamaConstants.FORMAT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.KEEP_ALIVE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.IMAGES;

/**
 * Represents a request for generating content using an LLM model.
 * This class encapsulates all parameters needed for making a generation request.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class GenerateRequest extends Options {
    @JsonProperty(MODEL)
    private String model;

    @JsonProperty(PROMPT)
    private String prompt;

    @JsonProperty(SUFFIX)
    private String suffix;

    @JsonProperty(SYSTEM)
    private String system;

    @JsonProperty(TEMPLATE)
    private String template;

    @JsonProperty(CONTEXT)
    private List<Integer> context;

    @JsonProperty(STREAM)
    private Boolean stream;

    @JsonProperty(RAW)
    private Boolean raw;

    @JsonProperty(FORMAT)
    private JsonNode format;

    @JsonProperty(KEEP_ALIVE)
    private Duration keepAlive;

    @JsonProperty(IMAGES)
    private List<byte[]> images;

    /**
     * Converts all fields of this request object into a Map representation.
     * This is useful for serialization and API communication.
     *
     * @return A Map containing all non-null fields of this request
     */
    @JsonIgnore
    public Map<String, Object> getOptions() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(this, new TypeReference<Map<String, Object>>() {});
    }
}
