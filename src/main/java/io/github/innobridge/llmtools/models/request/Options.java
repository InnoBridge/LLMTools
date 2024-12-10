package io.github.innobridge.llmtools.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

import static io.github.innobridge.llmtools.constants.OllamaConstants.NUM_KEEP;
import static io.github.innobridge.llmtools.constants.OllamaConstants.SEED;
import static io.github.innobridge.llmtools.constants.OllamaConstants.NUM_PREDICT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TOP_K;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TOP_P;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MIN_P;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TFS_Z;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TYPICAL_P;
import static io.github.innobridge.llmtools.constants.OllamaConstants.REPEAT_LAST_N;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TEMPERATURE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.REPEAT_PENALTY;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PRESENCE_PENALTY;
import static io.github.innobridge.llmtools.constants.OllamaConstants.FREQUENCY_PENALTY;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MIROSTAT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MIROSTAT_TAU;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MIROSTAT_ETA;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PENALIZE_NEWLINE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.STOP;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Options {
    @JsonProperty(NUM_KEEP)
    private Integer numKeep;
    
    @JsonProperty(SEED)
    private Integer seed;
    
    @JsonProperty(NUM_PREDICT)
    private Integer numPredict;
    
    @JsonProperty(TOP_K)
    private Integer topK;
    
    @JsonProperty(TOP_P)
    private Float topP;
    
    @JsonProperty(MIN_P)
    private Float minP;
    
    @JsonProperty(TFS_Z)
    private Float tfsZ;
    
    @JsonProperty(TYPICAL_P)
    private Float typicalP;
    
    @JsonProperty(REPEAT_LAST_N)
    private Integer repeatLastN;
    
    @JsonProperty(TEMPERATURE)
    private Float temperature;
    
    @JsonProperty(REPEAT_PENALTY)
    private Float repeatPenalty;
    
    @JsonProperty(PRESENCE_PENALTY)
    private Float presencePenalty;
    
    @JsonProperty(FREQUENCY_PENALTY)
    private Float frequencyPenalty;
    
    @JsonProperty(MIROSTAT)
    private Integer mirostat;
    
    @JsonProperty(MIROSTAT_TAU)
    private Float mirostatTau;
    
    @JsonProperty(MIROSTAT_ETA)
    private Float mirostatEta;
    
    @JsonProperty(PENALIZE_NEWLINE)
    private Boolean penalizeNewline;
    
    @JsonProperty(STOP)
    private List<String> stop;
}
