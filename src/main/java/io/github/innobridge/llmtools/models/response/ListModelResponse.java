package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.NAME;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODEL;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODIFIED_AT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.SIZE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.DIGEST;
import static io.github.innobridge.llmtools.constants.OllamaConstants.DETAILS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;

public class ListModelResponse {
    @JsonProperty(NAME)
    private String name;

    @JsonProperty(MODEL)
    private String model;

    @JsonProperty(MODIFIED_AT)
    private ZonedDateTime modifiedAt;

    @JsonProperty(SIZE)
    private long size;

    @JsonProperty(DIGEST)
    private String digest;

    @JsonProperty(DETAILS)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ModelDetails details;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public ZonedDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(ZonedDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }

    public String getDigest() { return digest; }
    public void setDigest(String digest) { this.digest = digest; }

    public ModelDetails getDetails() { return details; }
    public void setDetails(ModelDetails details) { this.details = details; }
}
