package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.CREATED_AT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.DONE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.DONE_REASON;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MESSAGE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODEL;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.innobridge.llmtools.models.Message;
import lombok.Builder;
import lombok.AllArgsConstructor;

import java.time.Instant;

/**
 * Represents a response from a chat completion API.
 * This class extends Metrics to include performance metrics along with the chat response data.
 * It contains information about the model used, creation timestamp, message content,
 * completion status, and reason for completion.
 */
@Builder
@AllArgsConstructor
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

    /**
     * Default constructor for ChatResponse.
     * Created to support Jackson deserialization and Builder pattern.
     */
    public ChatResponse() {
        super();
    }

    // Getters
    /**
     * Gets the model name used for this chat response.
     *
     * @return the model name as a String
     */
    public String getModel() { 
        return model; 
    }

    /**
     * Gets the creation timestamp of this chat response.
     *
     * @return the creation time as an Instant
     */
    public Instant getCreatedAt() { 
        return createdAt; 
    }

    /**
     * Gets the message content of this chat response.
     *
     * @return the Message object containing the response content
     */
    public Message getMessage() { 
        return message; 
    }

    /**
     * Gets the reason why the chat response was completed.
     *
     * @return the completion reason as a String
     */
    public String getDoneReason() { 
        return doneReason; 
    }

    /**
     * Checks if the chat response is complete.
     *
     * @return true if the response is complete, false otherwise
     */
    public boolean isDone() { 
        return done; 
    }

    // Setters
    /**
     * Sets the model name for this chat response.
     *
     * @param model the model name to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Sets the creation timestamp for this chat response.
     *
     * @param createdAt the creation time to set
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Sets the message content for this chat response.
     *
     * @param message the Message object to set
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * Sets the completion reason for this chat response.
     *
     * @param doneReason the completion reason to set
     */
    public void setDoneReason(String doneReason) {
        this.doneReason = doneReason;
    }

    /**
     * Sets the completion status of this chat response.
     *
     * @param done true if the response is complete, false otherwise
     */
    public void setDone(boolean done) {
        this.done = done;
    }
}
