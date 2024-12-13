package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.CONTENT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.IMAGES;
import static io.github.innobridge.llmtools.constants.OllamaConstants.ROLE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TOOL_CALLS;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

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

    // Getters and Setters
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public List<byte[]> getImages() { return images; }
    public void setImages(List<byte[]> images) { this.images = images; }
    public List<ToolCall> getToolCalls() { return toolCalls; }
    public void setToolCalls(List<ToolCall> toolCalls) { this.toolCalls = toolCalls; }
}
