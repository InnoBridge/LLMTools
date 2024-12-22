package io.github.innobridge.llmtools.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import static io.github.innobridge.llmtools.constants.OllamaConstants.*;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
public class ToolFunction {
    @JsonProperty(NAME)
    private String name;

    @JsonProperty(DESCRIPTION)
    private String description;

    @JsonProperty(PARAMETERS)
    private Parameters parameters;

    @Data
    @Builder(builderMethodName = "hiddenBuilder")
    @AllArgsConstructor
    public static class Parameters {
        @JsonProperty(TYPE)
        private String type;

        @JsonProperty(REQUIRED)
        private List<String> required;

        @JsonProperty(PROPERTIES)
        private Map<String, Property> properties;

        public static ParametersBuilder builder(String type) {
            return hiddenBuilder().type(type);
        }
    }

    @Data
    @Builder(builderMethodName = "hiddenBuilder")
    @AllArgsConstructor
    public static class Property {
        @JsonProperty(TYPE)
        private String type;

        @JsonProperty(DESCRIPTION)
        private String description;

        @JsonProperty(ENUM)
        private List<String> enumValues;

        public static PropertyBuilder builder(String type) {
            return hiddenBuilder().type(type);
        }
    }
}
