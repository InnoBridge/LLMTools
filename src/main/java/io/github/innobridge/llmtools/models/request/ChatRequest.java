package io.github.innobridge.llmtools.models.request;

import static io.github.innobridge.llmtools.constants.OllamaConstants.FORMAT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.KEEP_ALIVE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MESSAGES;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODEL;
import static io.github.innobridge.llmtools.constants.OllamaConstants.STREAM;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TOOLS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;

import io.github.innobridge.llmtools.models.Message;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ChatRequest extends Options {
    @NotNull
    @JsonProperty(MODEL)
    private String model;

    @JsonProperty(MESSAGES)
    private List<Message> messages;

    @JsonProperty(STREAM)
    private Boolean stream;

    @JsonProperty(FORMAT)
    @JsonDeserialize(using = FormatDeserializer.class)
    private String format;

    @JsonProperty(KEEP_ALIVE)
    private Duration keepAlive;

    @JsonProperty(TOOLS)
    private List<Tool> tools;

    
   
    public static class FormatDeserializer extends JsonDeserializer<String> {
        private static final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            if (node.isTextual()) {
                return node.asText();
            } else if (node.isObject()) {
                return objectMapper.writeValueAsString(node);
            }
            throw new JsonMappingException(p, "Format must be either a string or JSON object");
        }
    }

}
