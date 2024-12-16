package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.FAMILIES;
import static io.github.innobridge.llmtools.constants.OllamaConstants.FAMILY;
import static io.github.innobridge.llmtools.constants.OllamaConstants.FORMAT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PARAMETER_SIZE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PARENT_MODEL;
import static io.github.innobridge.llmtools.constants.OllamaConstants.QUANTIZATION_LEVEL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelDetails {
    @JsonProperty(PARENT_MODEL)
    private String parentModel;
    @JsonProperty(FORMAT)
    private String format;
    @JsonProperty(FAMILY)
    private String family;
    @JsonProperty(FAMILIES)
    private List<String> families;
    
    @JsonProperty(PARAMETER_SIZE)
    private String parameterSize;
    
    @JsonProperty(QUANTIZATION_LEVEL)
    private String quantizationLevel;

    public String getParentModel() { return parentModel; }
    public void setParentModel(String parentModel) { this.parentModel = parentModel; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public String getFamily() { return family; }
    public void setFamily(String family) { this.family = family; }
    public List<String> getFamilies() { return families; }
    public void setFamilies(List<String> families) { this.families = families; }
    public String getParameterSize() { return parameterSize; }
    public void setParameterSize(String parameterSize) { this.parameterSize = parameterSize; }
    public String getQuantizationLevel() { return quantizationLevel; }
    public void setQuantizationLevel(String quantizationLevel) { this.quantizationLevel = quantizationLevel; }
}
