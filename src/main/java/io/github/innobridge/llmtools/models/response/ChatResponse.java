package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.CREATED_AT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.DONE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.DONE_REASON;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MESSAGE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODEL;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.innobridge.llmtools.models.Message;
import lombok.Builder;

import java.time.Instant;

@Builder
public class ChatResponse extends Metrics {
    @JsonProperty(MODEL)
    private String model;

    @JsonProperty(CREATED_AT)
    private Instant createdAt;

    @JsonProperty(MESSAGE)
    private Message message;

    @JsonProperty(DONE_REASON)
    private String doneReason;

    @JsonProperty(DONE)
    private boolean done;

   

    // Getters
    public String getModel() { return model; }
    public Instant getCreatedAt() { return createdAt; }
    public Message getMessage() { return message; }
    public String getDoneReason() { return doneReason; }
    public boolean isDone() { return done; }

    // Setters
    public void setModel(String model) { this.model = model; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setMessage(Message message) { this.message = message; }
    public void setDoneReason(String doneReason) { this.doneReason = doneReason; }
    public void setDone(boolean done) { this.done = done; }
}
