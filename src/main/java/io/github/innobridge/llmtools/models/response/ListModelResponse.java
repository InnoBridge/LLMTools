package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.MODIFIED_AT;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class ListModelResponse extends ModelResponse {
    @JsonProperty(MODIFIED_AT)
    private Instant modifiedAt;

    public Instant getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(Instant modifiedAt) { this.modifiedAt = modifiedAt; }
}
