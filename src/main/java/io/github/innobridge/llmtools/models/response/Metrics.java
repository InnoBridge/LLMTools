package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.TOTAL_DURATION;
import static io.github.innobridge.llmtools.constants.OllamaConstants.LOAD_DURATION;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PROMPT_EVAL_COUNT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PROMPT_EVAL_DURATION;
import static io.github.innobridge.llmtools.constants.OllamaConstants.EVAL_COUNT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.EVAL_DURATION;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;

public class Metrics {
    @JsonProperty(TOTAL_DURATION)
    private Duration totalDuration;

    @JsonProperty(LOAD_DURATION)
    private Duration loadDuration;

    @JsonProperty(PROMPT_EVAL_COUNT)
    private int promptEvalCount;

    @JsonProperty(PROMPT_EVAL_DURATION)
    private Duration promptEvalDuration;

    @JsonProperty(EVAL_COUNT)
    private int evalCount;

    @JsonProperty(EVAL_DURATION)
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
