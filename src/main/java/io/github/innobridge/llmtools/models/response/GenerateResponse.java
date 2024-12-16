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

    /**
     * Gets the model name.
     * @return the model name
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model name.
     * @param model the model name to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the creation timestamp.
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     * @param createdAt the creation timestamp to set
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the response text.
     * @return the response text
     */
    public String getResponse() {
        return response;
    }

    /**
     * Sets the response text.
     * @param response the response text to set
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * Checks if the response generation is complete.
     * @return true if done, false otherwise
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Sets the completion status.
     * @param done the completion status to set
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * Gets the reason for completion.
     * @return the completion reason
     */
    public String getDoneReason() {
        return doneReason;
    }

    /**
     * Sets the reason for completion.
     * @param doneReason the completion reason to set
     */
    public void setDoneReason(String doneReason) {
        this.doneReason = doneReason;
    }

    /**
     * Gets the context tokens.
     * @return the list of context tokens
     */
    public List<Integer> getContext() {
        return context;
    }

    /**
     * Sets the context tokens.
     * @param context the list of context tokens to set
     */
    public void setContext(List<Integer> context) {
        this.context = context;
    }
}
