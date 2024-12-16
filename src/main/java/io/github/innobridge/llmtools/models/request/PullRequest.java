package io.github.innobridge.llmtools.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import static io.github.innobridge.llmtools.constants.OllamaConstants.INSECURE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODEL;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PASSWORD;
import static io.github.innobridge.llmtools.constants.OllamaConstants.STREAM;
import static io.github.innobridge.llmtools.constants.OllamaConstants.USERNAME;

@Getter
@Builder
public class PullRequest {
    @JsonProperty(MODEL)
    private final String model;
    
    @JsonProperty(INSECURE)
    private final boolean insecure;
    
    @JsonProperty(USERNAME)
    private final String username;
    
    @JsonProperty(PASSWORD)
    private final String password;
    
    @JsonProperty(STREAM)
    private final Boolean stream;

    public PullRequest setStream(Boolean stream) {
        return PullRequest.builder()
                .model(this.model)
                .insecure(this.insecure)
                .username(this.username)
                .password(this.password)
                .stream(stream)
                .build();
    }
}
