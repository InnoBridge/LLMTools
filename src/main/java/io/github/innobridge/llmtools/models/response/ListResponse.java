package io.github.innobridge.llmtools.models.response;

import static io.github.innobridge.llmtools.constants.OllamaConstants.MODELS;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ListResponse {
    @JsonProperty(MODELS)
    private List<ListModelResponse> models;

    public List<ListModelResponse> getModels() {
        return models;
    }

    public void setModels(List<ListModelResponse> models) {
        this.models = models;
    }
}