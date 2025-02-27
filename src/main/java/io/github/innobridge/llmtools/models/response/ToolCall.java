package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.FUNCTION;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class ToolCall {
    @JsonProperty(FUNCTION)
    private ToolCallFunction function;

    public ToolCallFunction getFunction() { return function; }
    public void setFunction(ToolCallFunction function) { this.function = function; }
}
