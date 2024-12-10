package io.github.innobridge.llmtools.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

public class GenerateResponse extends Metrics {

    @JsonProperty("model")
    private String model;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("response")
    private String response;

    @JsonProperty("done")
    private boolean done;

    @JsonProperty("done_reason")
    private String doneReason;

    @JsonProperty("context")
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
