package io.github.innobridge.llmtools.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.github.innobridge.llmtools.constants.OllamaConstants.INSECURE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODEL;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PASSWORD;
import static io.github.innobridge.llmtools.constants.OllamaConstants.STREAM;
import static io.github.innobridge.llmtools.constants.OllamaConstants.USERNAME;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PullRequest {
    @JsonProperty(MODEL)
    private String model;
    
    @JsonProperty(INSECURE)
    private boolean insecure;
    
    @JsonProperty(USERNAME)
    private String username;
    
    @JsonProperty(PASSWORD)
    private String password;
    
    @JsonProperty(STREAM)
    private Boolean stream;

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
