package io.github.innobridge.llmtools.function;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.github.innobridge.llmtools.exceptions.LLMFunctionException;
import io.github.innobridge.llmtools.tools.FunctionDefinition;

@Service
@FunctionDefinition(
    name = "get_current_weather",
    description = "Get the current weather in a given location"
)
public class WeatherService implements LLMFunction<WeatherService.Request, WeatherService.Response> {
    
    private final WebClient weatherClient;
    private final String apiKey;
    
    public WeatherService(WebClient weatherClient, String apiKey) {
        this.weatherClient = weatherClient;
        this.apiKey = apiKey;
    }

    @Override
    public Request fromArguments(Map<String, Object> arguments) {
        try {
            String location = (String) arguments.get("location");
            if (location == null || location.trim().isEmpty()) {
                throw new LLMFunctionException("Location is required");
            }
            Format format = Format.CELSIUS;  // Default format
            String formatStr = (String) arguments.get("format");
            if (formatStr != null && !formatStr.trim().isEmpty() && 
                formatStr.toLowerCase().equals("fahrenheit")) {
                format = Format.FAHRENHEIT;
            }
    
            return new Request(location, format);
        } catch (ClassCastException e) {
            throw new LLMFunctionException("Invalid argument type", e);
        }
    }

    @Override
    public Response apply(Request request) {
        if (request.location() == null || request.location().trim().isEmpty()) {
            throw new LLMFunctionException("Location is required");
        }

        try {
            String uri = UriComponentsBuilder.fromPath("/current.json")
                    .queryParam("key", apiKey)
                    .queryParam("q", request.location())
                    .queryParam("aqi", "no")
                    .build()
                    .toUriString();

            Response response = weatherClient.post()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .bodyValue("")  // Empty body for POST request
                    .retrieve()
                    .bodyToMono(Response.class)
                    .block();

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new LLMFunctionException("Failed to get weather", e);
        }
    }

    enum Format {
        CELSIUS("celsius"), 
        FAHRENHEIT("fahrenheit");

        public final String formatName;

        Format(String formatName) {
            this.formatName = formatName;
        }

        @Override
        public String toString() {
            return formatName;
        }
    }

    @JsonInclude(Include.NON_NULL)
    record Request(
        @JsonProperty(required = true) 
        @JsonPropertyDescription("The city and state e.g. San Francisco, CA") 
        String location,
        @JsonProperty(required = true) 
        @JsonPropertyDescription("The format to return the weather in, e.g. 'celsius' or 'fahrenheit'") 
        Format format) {
    }
    
    record Response(
        Location location,
        Current current
    ) {
        record Current(
            double temp_c,
            // double temp_f,
            // @JsonProperty("wind_mph")
            // double windMph,
            // @JsonProperty("wind_kph")
            // double windKph,
            // @JsonProperty("precip_mm")
            // double precipMm,
            @JsonProperty("precip_in")
            double precipIn,
            // int humidity,
            // int cloud,
            // double uv,
            Condition condition
        ) {}
    
        record Condition(
            String text
        ) {}

        record Location(
            String name,
            // String region,
            String country
            // double lat,
            // double lon,
            // @JsonProperty("tz_id")
            // String tzId,
            // String localtime
        ) {}
    }
    
}
