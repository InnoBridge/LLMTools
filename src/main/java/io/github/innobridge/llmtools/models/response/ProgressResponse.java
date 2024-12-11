package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.STATUS;
import static io.github.innobridge.llmtools.constants.OllamaConstants.DIGEST;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TOTAL;
import static io.github.innobridge.llmtools.constants.OllamaConstants.COMPLETED;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProgressResponse(
    @JsonProperty(STATUS) String status,
    @JsonProperty(DIGEST) String digest,
    @JsonProperty(TOTAL) Long total,
    @JsonProperty(COMPLETED) Long completed
) { }
