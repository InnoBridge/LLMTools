package io.github.innobridge.llmtools.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import static io.github.innobridge.llmtools.constants.OllamaConstants.SOURCE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.DESTINATION;

/**
 * Represents a request to copy or clone a model.
 * This record encapsulates the source and destination model names for a model copy operation.
 * 
 * @param source The name of the source model to copy from
 * @param destination The name of the destination model to copy to
 */
public record CopyRequest(
    @JsonProperty(SOURCE) String source,
    @JsonProperty(DESTINATION) String destination
) {}
