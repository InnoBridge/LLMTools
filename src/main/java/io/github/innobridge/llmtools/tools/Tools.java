package io.github.innobridge.llmtools.tools;

import java.util.List;

import io.github.innobridge.llmtools.models.request.ChatRequest;
import io.github.innobridge.llmtools.models.request.Tool;
import io.github.innobridge.llmtools.models.response.ModelAndSize;
import io.github.innobridge.llmtools.models.response.SortOrder;

public interface Tools {
    boolean isToolsSupported(String modelName);

    List<String> getToolSupportingModels();

    List<ModelAndSize> getToolSupportingModelResponses(SortOrder sortOrder);

    FunctionsExecutor functionCall(ChatRequest chatRequest, List<Tool> tools);
}
