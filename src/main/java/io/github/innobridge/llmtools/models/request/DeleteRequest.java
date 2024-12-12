package io.github.innobridge.llmtools.models.request;

import static io.github.innobridge.llmtools.constants.OllamaConstants.NAME;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record DeleteRequest(
    @NotBlank(message = "Name is required")
    @JsonProperty(NAME) 
    String name
) {}
