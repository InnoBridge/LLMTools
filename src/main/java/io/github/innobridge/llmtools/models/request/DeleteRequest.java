package io.github.innobridge.llmtools.models.request;

import static io.github.innobridge.llmtools.constants.OllamaConstants.NAME;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * A record representing a request to delete a model from Ollama.
 * <p>
 * This request contains the name of the model to be deleted.
 */
public record DeleteRequest(
    @NotBlank(message = "Name is required")
    @JsonProperty(NAME) 
    String name
) {}
