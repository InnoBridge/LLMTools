package io.github.innobridge.llmtools.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TYPE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.FUNCTION;

public class Tool {
    @JsonProperty(TYPE)
    private String type;

    @JsonProperty(FUNCTION)
    private ToolFunction function;

    public Tool() {
    }

    public Tool(String type, ToolFunction function) {
        this.type = type;
        this.function = function;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ToolFunction getFunction() {
        return function;
    }

    public void setFunction(ToolFunction function) {
        this.function = function;
    }
}
