package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.CONTEXT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.CREATED_AT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.DONE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.DONE_REASON;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODEL;
import static io.github.innobridge.llmtools.constants.OllamaConstants.RESPONSE;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public class GenerateResponse extends Metrics {

    @JsonProperty(MODEL)
    private String model;

    @JsonProperty(CREATED_AT)
    private Instant createdAt;

    @JsonProperty(RESPONSE)
    private String response;

    @JsonProperty(DONE)
    private boolean done;

    @JsonProperty(DONE_REASON)
    private String doneReason;

    @JsonProperty(CONTEXT)
    private List<Integer> context;

    // Getters and Setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDoneReason() {
        return doneReason;
    }

    public void setDoneReason(String doneReason) {
        this.doneReason = doneReason;
    }

    public List<Integer> getContext() {
        return context;
    }

    public void setContext(List<Integer> context) {
        this.context = context;
    }
}
