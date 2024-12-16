package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.MODELS;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ListProcessModelResponse {
    @JsonProperty(MODELS)
    private List<ProcessModelResponse> models;

    public List<ProcessModelResponse> getModels() {
        return models;
    }

    public void setModels(List<ProcessModelResponse> models) {
        this.models = models;
    }
}