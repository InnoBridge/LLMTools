package io.github.innobridge.llmtools.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.innobridge.llmtools.client.OllamaClient;
import io.github.innobridge.llmtools.models.request.GenerateRequest;
import io.github.innobridge.llmtools.models.response.GenerateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static io.github.innobridge.llmtools.constants.HTTPConstants.CONTENT_TYPE;
import static io.github.innobridge.llmtools.constants.HTTPConstants.CREATED;
import static io.github.innobridge.llmtools.constants.OllamaConstants.GENERATE_ENDPOINT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODEL;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PROMPT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.SUFFIX;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TEMPLATE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.SYSTEM;
import static io.github.innobridge.llmtools.constants.OllamaConstants.CONTEXT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.RAW;
import static io.github.innobridge.llmtools.constants.OllamaConstants.FORMAT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.KEEP_ALIVE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.IMAGES;
import static io.github.innobridge.llmtools.constants.OllamaConstants.NUM_KEEP;
import static io.github.innobridge.llmtools.constants.OllamaConstants.SEED;
import static io.github.innobridge.llmtools.constants.OllamaConstants.NUM_PREDICT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TOP_K;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TOP_P;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MIN_P;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TFS_Z;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TYPICAL_P;
import static io.github.innobridge.llmtools.constants.OllamaConstants.REPEAT_LAST_N;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TEMPERATURE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.REPEAT_PENALTY;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PRESENCE_PENALTY;
import static io.github.innobridge.llmtools.constants.OllamaConstants.FREQUENCY_PENALTY;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MIROSTAT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MIROSTAT_TAU;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MIROSTAT_ETA;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PENALIZE_NEWLINE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.STOP;
import static io.github.innobridge.llmtools.constants.OllamaConstants.GENERATE_STREAM_ENDPOINT;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@Slf4j
@RestController
public class OllamaController {
    
    private final OllamaClient ollamaClient;

    public OllamaController(OllamaClient ollamaClient) {
        this.ollamaClient = ollamaClient;
    }

    @PostMapping(GENERATE_ENDPOINT)
    @ApiResponses(value= {
            @ApiResponse(responseCode = CREATED,
                    description = "Generate response",
                    content = @Content(mediaType = CONTENT_TYPE,
                            schema = @Schema(implementation = GenerateResponse.class)))
    })
    public Mono<ResponseEntity<GenerateResponse>> generate(
        @RequestParam(required = true, value = MODEL) String model,
        @RequestParam(required = false, value = PROMPT) String prompt,
        @RequestParam(required = false, value = SUFFIX) String suffix,
        @RequestParam(required = false, value = TEMPLATE) String template,
        @RequestParam(required = false, value = SYSTEM) String system,
        @RequestParam(required = false, value = CONTEXT) List<Integer> context,
        @RequestParam(required = false, value = RAW) Boolean raw,
        @RequestParam(required = false, value = FORMAT) JsonNode format,
        @RequestParam(required = false, value = KEEP_ALIVE) Duration keepAlive,
        @RequestParam(required = false, value = IMAGES) List<byte[]> images,
        @RequestParam(required = false, value = NUM_KEEP) Integer numKeep,
        @RequestParam(required = false, value = SEED) Integer seed,
        @RequestParam(required = false, value = NUM_PREDICT) Integer numPredict,
        @RequestParam(required = false, value = TOP_K) Integer topK,
        @RequestParam(required = false, value = TOP_P) Float topP,
        @RequestParam(required = false, value = MIN_P) Float minP,
        @RequestParam(required = false, value = TFS_Z) Float tfsZ,
        @RequestParam(required = false, value = TYPICAL_P) Float typicalP,
        @RequestParam(required = false, value = REPEAT_LAST_N) Integer repeatLastN,
        @RequestParam(required = false, value = TEMPERATURE) Float temperature,
        @RequestParam(required = false, value = REPEAT_PENALTY) Float repeatPenalty,
        @RequestParam(required = false, value = PRESENCE_PENALTY) Float presencePenalty,
        @RequestParam(required = false, value = FREQUENCY_PENALTY) Float frequencyPenalty,
        @RequestParam(required = false, value = MIROSTAT) Integer mirostat,
        @RequestParam(required = false, value = MIROSTAT_TAU) Float mirostatTau,
        @RequestParam(required = false, value = MIROSTAT_ETA) Float mirostatEta,
        @RequestParam(required = false, value = PENALIZE_NEWLINE) Boolean penalizeNewline,
        @RequestParam(required = false, value = STOP) List<String> stop
    ) throws IOException {
        var builder = GenerateRequest.builder();
        builder.model(model);
        if (prompt != null) builder.prompt(prompt);
        if (suffix != null) builder.suffix(suffix);
        if (system != null) builder.system(system);
        if (template != null) builder.template(template);
        if (context != null) builder.context(context);
        if (raw != null) builder.raw(raw);
        if (format != null) builder.format(format);
        if (keepAlive != null) builder.keepAlive(keepAlive);
        if (images != null) builder.images(images);
        if (numKeep != null) builder.numKeep(numKeep);
        if (seed != null) builder.seed(seed);
        if (numPredict != null) builder.numPredict(numPredict);
        if (topK != null) builder.topK(topK);
        if (topP != null) builder.topP(topP);
        if (minP != null) builder.minP(minP);
        if (tfsZ != null) builder.tfsZ(tfsZ);
        if (typicalP != null) builder.typicalP(typicalP);
        if (repeatLastN != null) builder.repeatLastN(repeatLastN);
        if (temperature != null) builder.temperature(temperature);
        if (repeatPenalty != null) builder.repeatPenalty(repeatPenalty);
        if (presencePenalty != null) builder.presencePenalty(presencePenalty);
        if (frequencyPenalty != null) builder.frequencyPenalty(frequencyPenalty);
        if (mirostat != null) builder.mirostat(mirostat);
        if (mirostatTau != null) builder.mirostatTau(mirostatTau);
        if (mirostatEta != null) builder.mirostatEta(mirostatEta);
        if (penalizeNewline != null) builder.penalizeNewline(penalizeNewline);
        if (stop != null) builder.stop(stop);    

        return ollamaClient.generate(builder.build())
        .map(response -> ResponseEntity.ok(response))
        .onErrorResume(e -> {
            return Mono.just(ResponseEntity.status(500).body(null));
        });
    }
    
    @PostMapping(GENERATE_STREAM_ENDPOINT)
    @Operation(summary = "Generate streaming response from Ollama model")
    @ApiResponse(
        responseCode = CREATED,
        description = "Successful streaming response",
        content = @Content(
            mediaType = TEXT_EVENT_STREAM_VALUE,
            schema = @Schema(implementation = GenerateResponse.class)
        )
    )
    public Flux<ServerSentEvent<GenerateResponse>> generateStream(
        @RequestParam(required = true, value = MODEL) String model,
        @RequestParam(required = false, value = PROMPT) String prompt,
        @RequestParam(required = false, value = SUFFIX) String suffix,
        @RequestParam(required = false, value = TEMPLATE) String template,
        @RequestParam(required = false, value = SYSTEM) String system,
        @RequestParam(required = false, value = CONTEXT) List<Integer> context,
        @RequestParam(required = false, value = RAW) Boolean raw,
        @RequestParam(required = false, value = FORMAT) JsonNode format,
        @RequestParam(required = false, value = KEEP_ALIVE) Duration keepAlive,
        @RequestParam(required = false, value = IMAGES) List<byte[]> images,
        @RequestParam(required = false, value = NUM_KEEP) Integer numKeep,
        @RequestParam(required = false, value = SEED) Integer seed,
        @RequestParam(required = false, value = NUM_PREDICT) Integer numPredict,
        @RequestParam(required = false, value = TOP_K) Integer topK,
        @RequestParam(required = false, value = TOP_P) Float topP,
        @RequestParam(required = false, value = MIN_P) Float minP,
        @RequestParam(required = false, value = TFS_Z) Float tfsZ,
        @RequestParam(required = false, value = TYPICAL_P) Float typicalP,
        @RequestParam(required = false, value = REPEAT_LAST_N) Integer repeatLastN,
        @RequestParam(required = false, value = TEMPERATURE) Float temperature,
        @RequestParam(required = false, value = REPEAT_PENALTY) Float repeatPenalty,
        @RequestParam(required = false, value = PRESENCE_PENALTY) Float presencePenalty,
        @RequestParam(required = false, value = FREQUENCY_PENALTY) Float frequencyPenalty,
        @RequestParam(required = false, value = MIROSTAT) Integer mirostat,
        @RequestParam(required = false, value = MIROSTAT_TAU) Float mirostatTau,
        @RequestParam(required = false, value = MIROSTAT_ETA) Float mirostatEta,
        @RequestParam(required = false, value = PENALIZE_NEWLINE) Boolean penalizeNewline,
        @RequestParam(required = false, value = STOP) List<String> stop
    ) throws IOException {
        var builder = GenerateRequest.builder();
        builder.model(model);
        if (prompt != null) builder.prompt(prompt);
        if (suffix != null) builder.suffix(suffix);
        if (system != null) builder.system(system);
        if (template != null) builder.template(template);
        if (context != null) builder.context(context);
        if (raw != null) builder.raw(raw);
        if (format != null) builder.format(format);
        if (keepAlive != null) builder.keepAlive(keepAlive);
        if (images != null) builder.images(images);
        if (numKeep != null) builder.numKeep(numKeep);
        if (seed != null) builder.seed(seed);
        if (numPredict != null) builder.numPredict(numPredict);
        if (topK != null) builder.topK(topK);
        if (topP != null) builder.topP(topP);
        if (minP != null) builder.minP(minP);
        if (tfsZ != null) builder.tfsZ(tfsZ);
        if (typicalP != null) builder.typicalP(typicalP);
        if (repeatLastN != null) builder.repeatLastN(repeatLastN);
        if (temperature != null) builder.temperature(temperature);
        if (repeatPenalty != null) builder.repeatPenalty(repeatPenalty);
        if (presencePenalty != null) builder.presencePenalty(presencePenalty);
        if (frequencyPenalty != null) builder.frequencyPenalty(frequencyPenalty);
        if (mirostat != null) builder.mirostat(mirostat);
        if (mirostatTau != null) builder.mirostatTau(mirostatTau);
        if (mirostatEta != null) builder.mirostatEta(mirostatEta);
        if (penalizeNewline != null) builder.penalizeNewline(penalizeNewline);
        if (stop != null) builder.stop(stop);    


        return ollamaClient.generateStream(builder.build())
            .map(response -> ServerSentEvent.<GenerateResponse>builder()
                .data(response)
                .build())
            .onErrorResume(e -> {
                log.error("Error generating stream", e);
                return Flux.empty();
         });
    
    }
}
