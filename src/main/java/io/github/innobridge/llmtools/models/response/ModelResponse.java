package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelResponse {
    @JsonProperty(NAME)
    private String name;
    
    @JsonProperty(MODEL)
    private String model;
    
    @JsonProperty(SIZE)
    private long size;
    
    @JsonProperty(DIGEST)
    private String digest;
    
    @JsonProperty(DETAILS)
    private ModelDetails details;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }

    public String getDigest() { return digest; }
    public void setDigest(String digest) { this.digest = digest; }

    public ModelDetails getDetails() { return details; }
    public void setDetails(ModelDetails details) { this.details = details; }
}
