package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.MODIFIED_AT;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;

public class ListModelResponse extends ModelResponse {
    @JsonProperty(MODIFIED_AT)
    private ZonedDateTime modifiedAt;

    public ZonedDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(ZonedDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
}
