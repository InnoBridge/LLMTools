package io.github.innobridge.llmtools.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;

public class Metrics {

    @JsonProperty("total_duration")
    private Duration totalDuration;

    @JsonProperty("load_duration")
    private Duration loadDuration;

    @JsonProperty("prompt_eval_count")
    private int promptEvalCount;

    @JsonProperty("prompt_eval_duration")
    private Duration promptEvalDuration;

    @JsonProperty("eval_count")
    private int evalCount;

    @JsonProperty("eval_duration")
    private Duration evalDuration;

    // Getters and Setters
    public Duration getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Duration getLoadDuration() {
        return loadDuration;
    }

    public void setLoadDuration(Duration loadDuration) {
        this.loadDuration = loadDuration;
    }

    public int getPromptEvalCount() {
        return promptEvalCount;
    }

    public void setPromptEvalCount(int promptEvalCount) {
        this.promptEvalCount = promptEvalCount;
    }

    public Duration getPromptEvalDuration() {
        return promptEvalDuration;
    }

    public void setPromptEvalDuration(Duration promptEvalDuration) {
        this.promptEvalDuration = promptEvalDuration;
    }

    public int getEvalCount() {
        return evalCount;
    }

    public void setEvalCount(int evalCount) {
        this.evalCount = evalCount;
    }

    public Duration getEvalDuration() {
        return evalDuration;
    }

    public void setEvalDuration(Duration evalDuration) {
        this.evalDuration = evalDuration;
    }
}
