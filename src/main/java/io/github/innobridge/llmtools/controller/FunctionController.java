package io.github.innobridge.llmtools.controller;

import static io.github.innobridge.llmtools.constants.OllamaConstants.FUNCTION;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.innobridge.llmtools.client.OllamaClient;
import io.github.innobridge.llmtools.function.BraveSearchService;
import io.github.innobridge.llmtools.function.LLMFunction;
import io.github.innobridge.llmtools.function.WeatherService;
import io.github.innobridge.llmtools.models.Message;
import io.github.innobridge.llmtools.models.request.ChatRequest;
import io.github.innobridge.llmtools.models.request.Tool;
import io.github.innobridge.llmtools.models.request.ToolFunction;
import io.github.innobridge.llmtools.models.response.ModelAndSize;
import io.github.innobridge.llmtools.models.response.SortOrder;
import io.github.innobridge.llmtools.models.response.ToolCallFunction;
import io.github.innobridge.llmtools.tools.FunctionConverter;
import io.github.innobridge.llmtools.tools.Tools;

@RestController
@RequestMapping("/function")
public class FunctionController {

    private OllamaClient ollamaClient;

    private Tools ollamaTools;

    public FunctionController(OllamaClient ollamaClient, Tools ollamaTools) {
        this.ollamaClient = ollamaClient;
        this.ollamaTools = ollamaTools;
    }

    @GetMapping("/hello")
    public String hello() {
        var builder = ChatRequest.builder();
        builder.model("llama3.2");
        builder.messages(
            List.of(
                Message.builder()
                    .role("user")
                    .content("What is the weather today in Paris? and in New York?")
                    .build()
            )
        );
        builder.tools(
            List.of(
                new Tool(
                    FUNCTION,
                    new ToolFunction(
                        "getCurrentWeather",
                        "Get the current weather for a location",
                        ToolFunction.Parameters.builder("object")
                            .required(List.of("location", "format"))
                            .properties(
                                Map.of(
                                    "location", ToolFunction.Property.builder("string").description("The location to get the weather for, e.g. San Francisco, CA").build(),
                                    "format", ToolFunction.Property.builder("string").description("The format of the response, e.g. celsius or fahrenheit")
                                    .enumValues(List.of("celsius", "fahrenheit")).build()
                                )
                            ).build()
                        )
                    )
                )
        );
        builder.stream(false);

        return ollamaClient.chat(builder.build()).block().getMessage().getToolCalls().stream()
            .map(toolCall -> toolCall.getFunction().getName() + " : " + toolCall.getFunction().getArguments().toString() + "\n")
            .reduce("", String::concat);
    }

    @GetMapping("/hastool/{modelname}")
    public String hasTool(@PathVariable String modelname) {
        boolean hasTools = ollamaTools.isToolsSupported(modelname);
        
        
        return "Model: " + modelname + "\n" +
               "Has Tool Support: " + hasTools + "\n\n";    
    }

    @GetMapping("/toolmodels")
    public List<ModelAndSize> getToolModels(@RequestParam(required = false) SortOrder sortOrder) {
        return ollamaTools.getToolSupportingModelResponses(sortOrder);
    }

    enum FunctionClass {
        WEATHER_SERVICE,
        BRAVE_SEARCH
    }

    private HashMap<FunctionClass, Class<? extends LLMFunction>> functionMap = new HashMap<>();
    {
        functionMap.put(FunctionClass.WEATHER_SERVICE, WeatherService.class);
        functionMap.put(FunctionClass.BRAVE_SEARCH, BraveSearchService.class);
    }

    @GetMapping("/convertToToolFunction")
    public ToolFunction convertToToolFunction(@RequestParam FunctionClass functionClass) {
        return FunctionConverter.convertToToolFunction(functionMap.get(functionClass));
    }

    @GetMapping("/execute/weatherservice/first")
    public Optional<?> executeWeatherServiceFirst(@RequestParam String model, @RequestParam String prompt) {

        var builder = ChatRequest.builder();
        builder.model(model);
        builder.messages(
            List.of(
                Message.builder()
                    .role("user")
                    .content(prompt) 
                    .build()
            )
        );
        builder.stream(false);

        ToolFunction toolFunction = FunctionConverter.convertToToolFunction(WeatherService.class);

        Tool tool = new Tool(
            FUNCTION,
            toolFunction
        );
        builder.tools(List.of(tool));
        var result = ollamaTools.functionCall(builder.build(), List.of(tool));        
        return result.executeFirst(WeatherService.class);
    }

    @GetMapping("/execute/weatherservice/last")
    public Optional<?> executeWeatherServiceLast(@RequestParam String model, @RequestParam String prompt) {

        var builder = ChatRequest.builder();
        builder.model(model);
        builder.messages(
            List.of(
                Message.builder()
                    .role("user")
                    .content(prompt) 
                    .build()
            )
        );
        builder.stream(false);

        ToolFunction toolFunction = FunctionConverter.convertToToolFunction(WeatherService.class);

        Tool tool = new Tool(
            FUNCTION,
            toolFunction
        );
        builder.tools(List.of(tool));
        var result = ollamaTools.functionCall(builder.build(), List.of(tool));        
        return result.executeLast(WeatherService.class);
    }

    @GetMapping("/execute/weatherservice/all")
    public List<Optional<?>> executeWeatherServiceAll(@RequestParam String model, @RequestParam String prompt) {

        var builder = ChatRequest.builder();
        builder.model(model);
        builder.messages(
            List.of(
                Message.builder()
                    .role("user")
                    .content(prompt) 
                    .build()
            )
        );
        builder.stream(false);

        ToolFunction toolFunction = FunctionConverter.convertToToolFunction(WeatherService.class);

        Tool tool = new Tool(
            FUNCTION,
            toolFunction
        );
        builder.tools(List.of(tool));
        var result = ollamaTools.functionCall(builder.build(), List.of(tool));        
        return result.executeAll(WeatherService.class);
    }

    @GetMapping("/execute/bravesearch/all")
    public List<Optional<?>> executeBraveSearch(@RequestParam String model, @RequestParam String prompt) {
        var builder = ChatRequest.builder();
        builder.model(model);
        builder.messages(
            List.of(
                Message.builder()
                    .role("user")
                    .content(prompt) 
                    .build()
            )
        );
        builder.stream(false);

        ToolFunction toolFunction = FunctionConverter.convertToToolFunction(BraveSearchService.class);
        Tool tool = new Tool(FUNCTION, toolFunction);
        builder.tools(List.of(tool));
        
        var result = ollamaTools.functionCall(builder.build(), List.of(tool));        
        return result.executeAll(BraveSearchService.class);
    }

    @GetMapping("/execute/all")
    public List<Optional<?>> executeAll(@RequestParam String model, @RequestParam String prompt) {
        var builder = ChatRequest.builder();
        builder.model(model);
        builder.messages(
            List.of(
                Message.builder()
                    .role("user")
                    .content(prompt) 
                    .build()
            )
        );
        builder.stream(false);
        
        var result = ollamaTools.functionCall(builder.build(), List.of(
            new Tool(FUNCTION, FunctionConverter.convertToToolFunction(WeatherService.class)),
            new Tool(FUNCTION, FunctionConverter.convertToToolFunction(BraveSearchService.class))
        ));
        return result.executeAll();
    }

    @GetMapping("/invokes/weatherservice")
    public Boolean invokesWeatherService(@RequestParam String model, @RequestParam String prompt) {
        var builder = ChatRequest.builder();
        builder.model(model);
        builder.messages(
            List.of(
                Message.builder()
                    .role("user")
                    .content(prompt) 
                    .build()
            )
        );
        builder.stream(false);

        ToolFunction toolFunction = FunctionConverter.convertToToolFunction(WeatherService.class);

        Tool tool = new Tool(
            FUNCTION,
            toolFunction
        );
        builder.tools(List.of(tool));
        var result = ollamaTools.functionCall(builder.build(), List.of(tool));     
        return result.invokesFunction(WeatherService.class);
    }

    @GetMapping("/functioncall")
    public List<ToolCallFunction> getFunctionCalls(@RequestParam String model, @RequestParam String prompt) {
        var builder = ChatRequest.builder();
        builder.model(model);
        builder.messages(
            List.of(
                Message.builder()
                    .role("user")
                    .content(prompt) 
                    .build()
            )
        );
        builder.stream(false);

        var result = ollamaTools.functionCall(builder.build(), List.of(
            new Tool(FUNCTION, FunctionConverter.convertToToolFunction(WeatherService.class)),
            new Tool(FUNCTION, FunctionConverter.convertToToolFunction(BraveSearchService.class))
        ));        
        return result.getFunctionCalls();
    }
}
