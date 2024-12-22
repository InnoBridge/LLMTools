package io.github.innobridge.llmtools.tools;

import org.springframework.stereotype.Component;
import io.github.innobridge.llmtools.client.OllamaClient;
import io.github.innobridge.llmtools.function.LLMFunction;
import io.github.innobridge.llmtools.models.request.ShowRequest;
import io.github.innobridge.llmtools.models.request.Tool;
import io.github.innobridge.llmtools.models.request.ChatRequest;
import io.github.innobridge.llmtools.models.response.ListModelResponse;
import io.github.innobridge.llmtools.models.response.ListResponse;
import io.github.innobridge.llmtools.models.response.ModelAndSize;
import io.github.innobridge.llmtools.models.response.ModelResponse;
import io.github.innobridge.llmtools.models.response.SortOrder;
import io.github.innobridge.llmtools.models.response.ToolCall;

import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.HashMap;

import static io.github.innobridge.llmtools.constants.OllamaConstants.BYTE_SHORTHAND;
import static io.github.innobridge.llmtools.constants.OllamaConstants.GIGA_BYTE_SHORTHAND;
import static io.github.innobridge.llmtools.constants.OllamaConstants.KILO_BYTE_SHORTHAND;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MEGA_BYTE_SHORTHAND;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TERA_BYTE_SHORTHAND;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TOOLS_INDICATOR;
import static io.github.innobridge.llmtools.tools.FunctionConverter.getAnnotatedName;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@SuppressWarnings("rawtypes")
public class OllamaTools implements Tools {
        
        private final OllamaClient ollamaClient;
        private final Map<String, LLMFunction> functionRepository = new HashMap<>();

        public OllamaTools(OllamaClient ollamaClient, List<LLMFunction> functions) {
            this.ollamaClient = ollamaClient;
            functions.forEach(function -> functionRepository.put(getAnnotatedName(function.getClass()), function));
        }

        @Override
        public boolean isToolsSupported(String modelName) {
            var builder = ShowRequest.builder().model(modelName);
            var showResponse = ollamaClient.show(builder.build());
            var response = showResponse.block();
            String template = response.getTemplate();
            
            return template != null && template.contains(TOOLS_INDICATOR);
        }

        @Override
        public List<String> getToolSupportingModels() {
            var listResponse = ollamaClient.listModels().block();
            if (listResponse == null || listResponse.getModels() == null) {
                return new ArrayList<>();
            }

            return listResponse.getModels().stream()
                .map(model -> model.getName())
                .filter(this::isToolsSupported)
                .collect(Collectors.toList());
        }

        @Override
        public List<ModelAndSize> getToolSupportingModelResponses(SortOrder sortOrder) {
            ListResponse listResponse = ollamaClient.listModels().block();
            if (listResponse == null || listResponse.getModels() == null) {
                return new ArrayList<>();
            }

            return sortModelsBySize(listResponse, sortOrder);
        }

        @Override
        public FunctionsExecutor functionCall(ChatRequest chatRequest, List<Tool> tools) {
            if (!isToolsSupported(chatRequest.getModel())) {
                throw new IllegalArgumentException("Model " + chatRequest.getModel() + " does not support function calls");
            }
            chatRequest.setTools(tools);
            List<ToolCall> toolCalls = ollamaClient.chat(chatRequest).block().getMessage().getToolCalls();
            
            return new FunctionsExecutor(
                toolCalls == null ? new ArrayList<>() : toolCalls.stream()
                    .map(toolCall -> toolCall.getFunction())
                    .collect(Collectors.toList())    
            , this.functionRepository);
        }

        private String formatSize(long bytes) {
            if (bytes < 1024) {
                return bytes + " " + BYTE_SHORTHAND;
            }
            
            final String[] units = new String[] { 
                KILO_BYTE_SHORTHAND, 
                MEGA_BYTE_SHORTHAND, 
                GIGA_BYTE_SHORTHAND, 
                TERA_BYTE_SHORTHAND };

            int unitIndex = -1;
            double size = bytes;
        
            while (size >= 1024 && unitIndex < units.length - 1) {
                size /= 1024;
                unitIndex++;
            }
        
            return String.format("%.1f", size).replaceAll("\\.?0*$", "") + " " + units[unitIndex];
        }

        private List<ModelAndSize> sortModelsBySize(ListResponse listResponse, SortOrder sortOrder) {
            Stream<ListModelResponse> supportedModels = listResponse.getModels().stream()
            .filter(model -> isToolsSupported(model.getName())); 
            
            if (sortOrder != null && sortOrder != SortOrder.NONE) {
                if (sortOrder == SortOrder.ASC) {
                    supportedModels = supportedModels.sorted(Comparator.comparing(ModelResponse::getSize));
                } else {
                    supportedModels = supportedModels.sorted(Comparator.comparing(ModelResponse::getSize).reversed());
                }
            };

            return supportedModels.map(model -> new ModelAndSize(model.getName(), formatSize(model.getSize())))
                .collect(Collectors.toList());
        }
        
}
