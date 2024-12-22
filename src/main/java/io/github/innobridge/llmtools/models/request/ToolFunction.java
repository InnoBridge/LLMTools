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

        @lombok.Generated
        public static class ParametersBuilder {
            private String type;
            private List<String> required;
            private Map<String, Property> properties;

            ParametersBuilder() {
            }

            public ParametersBuilder type(String type) {
                this.type = type;
                return this;
            }

            public ParametersBuilder required(List<String> required) {
                this.required = required;
                return this;
            }

            public ParametersBuilder properties(Map<String, Property> properties) {
                this.properties = properties;
                return this;
            }

            public Parameters build() {
                return new Parameters(type, required, properties);
            }
        }

        @lombok.Generated
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

        @lombok.Generated
        public static class PropertyBuilder {
            private String type;
            private String description;
            private List<String> enumValues;

            PropertyBuilder() {
            }

            public PropertyBuilder type(String type) {
                this.type = type;
                return this;
            }

            public PropertyBuilder description(String description) {
                this.description = description;
                return this;
            }

            public PropertyBuilder enumValues(List<String> enumValues) {
                this.enumValues = enumValues;
                return this;
            }

            public Property build() {
                return new Property(type, description, enumValues);
            }
        }

        @lombok.Generated
        public static PropertyBuilder builder(String type) {
            return hiddenBuilder().type(type);
        }
    }
}
