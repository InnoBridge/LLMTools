package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.EXPIRES_AT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.SIZE_VRAM;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessModelResponse extends ModelResponse {
    @JsonProperty(EXPIRES_AT)
    private Instant expiresAt;
    
    @JsonProperty(SIZE_VRAM)
    private long sizeVRAM;

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }

    public long getSizeVRAM() { return sizeVRAM; }
    public void setSizeVRAM(long sizeVRAM) { this.sizeVRAM = sizeVRAM; }
}
