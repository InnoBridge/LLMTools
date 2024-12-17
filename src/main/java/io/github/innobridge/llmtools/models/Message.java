package io.github.innobridge.llmtools.models;

import static io.github.innobridge.llmtools.constants.OllamaConstants.CONTENT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.IMAGES;
import static io.github.innobridge.llmtools.constants.OllamaConstants.ROLE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TOOL_CALLS;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import io.github.innobridge.llmtools.models.response.ToolCall;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    @JsonProperty(ROLE)
    private String role;
    @JsonProperty(CONTENT)
    private String content;
    @JsonProperty(IMAGES)
    private List<byte[]> images;
    @JsonProperty(TOOL_CALLS)
    private List<ToolCall> toolCalls;
}
