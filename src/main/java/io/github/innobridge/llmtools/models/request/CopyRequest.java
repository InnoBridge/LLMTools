package io.github.innobridge.llmtools.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import static io.github.innobridge.llmtools.constants.OllamaConstants.SOURCE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.DESTINATION;

public record CopyRequest(
    @JsonProperty(SOURCE) String source,
    @JsonProperty(DESTINATION) String destination
) {}
