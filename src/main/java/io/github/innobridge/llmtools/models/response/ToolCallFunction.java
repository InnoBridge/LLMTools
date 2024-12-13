package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.ARGUMENTS;
import static io.github.innobridge.llmtools.constants.OllamaConstants.INDEX;
import static io.github.innobridge.llmtools.constants.OllamaConstants.NAME;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ToolCallFunction {
    @JsonProperty(INDEX)
    private int index;
    @JsonProperty(NAME)
    private String name;
    @JsonProperty(ARGUMENTS)
    private Map<String, Object> arguments;

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Map<String, Object> getArguments() { return arguments; }
    public void setArguments(Map<String, Object> arguments) { this.arguments = arguments; }
}
