package io.github.innobridge.llmtools.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.innobridge.llmtools.client.OllamaClient;
import io.github.innobridge.llmtools.models.request.GenerateRequest;
import io.github.innobridge.llmtools.models.response.GenerateResponse;
import io.github.innobridge.llmtools.models.response.ListProcessModelResponse;
import io.github.innobridge.llmtools.models.response.ListResponse;
import io.github.innobridge.llmtools.models.response.ProgressResponse;
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
import java.time.LocalDateTime;
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

import io.github.innobridge.llmtools.models.request.EmbedRequest;
import io.github.innobridge.llmtools.models.response.EmbedResponse;

import static io.github.innobridge.llmtools.constants.OllamaConstants.EMBED_ENDPOINT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.INPUT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TRUNCATE;

import io.github.innobridge.llmtools.models.request.CopyRequest;
import io.github.innobridge.llmtools.models.request.PullRequest;
import io.github.innobridge.llmtools.models.request.PushRequest;
import io.github.innobridge.llmtools.models.request.CreateRequest;
import io.github.innobridge.llmtools.models.request.DeleteRequest;
import io.github.innobridge.llmtools.models.request.ShowRequest;
import io.github.innobridge.llmtools.models.response.ShowResponse;

import static io.github.innobridge.llmtools.constants.OllamaConstants.PULL_ENDPOINT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PULL_STREAM_ENDPOINT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.INSECURE;
import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;

import static io.github.innobridge.llmtools.constants.OllamaConstants.COPY_ENDPOINT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.SOURCE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.DESTINATION;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PUSH_ENDPOINT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PUSH_STREAM_ENDPOINT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.USERNAME;
import static io.github.innobridge.llmtools.constants.OllamaConstants.PASSWORD;

import static io.github.innobridge.llmtools.constants.OllamaConstants.CREATE_ENDPOINT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.CREATE_STREAM_ENDPOINT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.MODELFILE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.QUANTIZE;

import static io.github.innobridge.llmtools.constants.OllamaConstants.DELETE_ENDPOINT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.NAME;
import static io.github.innobridge.llmtools.constants.OllamaConstants.SHOW_ENDPOINT;

import static io.github.innobridge.llmtools.constants.OllamaConstants.LIST_MODELS_ENDPOINT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.BLOB_ENDPOINT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.LIST_RUNNING_MODELS_ENDPOINT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.CHAT_ENDPOINT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.CHAT_STREAM_ENDPOINT;

import io.github.innobridge.llmtools.models.request.ChatRequest;
import io.github.innobridge.llmtools.models.response.ChatResponse;

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
            log.error("Chat error: {}", e.getMessage(), e);
            // Create empty ChatResponse with error message
            GenerateResponse errorResponse = GenerateResponse.builder()
                .doneReason(e.getMessage())
                .build();
                    
            return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse));
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

    @PostMapping(EMBED_ENDPOINT)
    @Operation(summary = "Generate embeddings from Ollama model")
    @ApiResponses(value= {
            @ApiResponse(responseCode = CREATED,
                    description = "Generate embeddings",
                    content = @Content(mediaType = CONTENT_TYPE,
                            schema = @Schema(implementation = EmbedResponse.class)))
    })
    public Mono<ResponseEntity<EmbedResponse>> embed(
        @RequestParam(required = true, value = MODEL) String model,
        @RequestParam(required = true, value = INPUT) List<String> input,
        @RequestParam(required = false, value = KEEP_ALIVE) Duration keepAlive,
        @RequestParam(required = false, value = TRUNCATE) Boolean truncate,
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
    ) {
        var builder = EmbedRequest.builder()
            .model(model)
            .input(input);
        if (keepAlive != null) builder.keepAlive(keepAlive);
        if (truncate != null) builder.truncate(truncate);
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
        return ollamaClient.embed(builder.build())
            .map(response -> ResponseEntity.ok(response))
            .onErrorResume(e -> {
                log.error("Error generating embeddings", e);
                return Mono.just(ResponseEntity.status(500).body(null));
            });
    }

    @Operation(summary = "Download model from Ollama Library.")
    @ApiResponses(value= {
            @ApiResponse(responseCode = CREATED,
                    description = "Download model from Ollama Library.",
                    content = @Content(mediaType = CONTENT_TYPE,
                            schema = @Schema(implementation = ProgressResponse.class)))
                            })
    @PostMapping(PULL_ENDPOINT)
    public Mono<ResponseEntity<ProgressResponse>> pullModel(
        @RequestParam(required = true, value = MODEL) String model,
        @RequestParam(required = false, value = INSECURE) Boolean insecure,
        @RequestParam(required = false, value = USERNAME) String username,
        @RequestParam(required = false, value = PASSWORD) String password
    ) {
        var builder = PullRequest.builder()
            .model(model);
        if (insecure != null) builder.insecure(insecure);
        if (username != null) builder.username(username);
        if (password != null) builder.password(password);
        
        ResponseEntity<ProgressResponse> response = ollamaClient.pull(builder.build())
            .map(ResponseEntity::ok)
            .onErrorResume(e -> Mono.just(ResponseEntity.status(500).body(null)))
            .block();
            
        return Mono.just(response);
    }

    @Operation(summary = "Download model from Ollama Library streaming progress.")
    @ApiResponses(value= {
            @ApiResponse(responseCode = CREATED,
                    description = "Download model from Ollama Library streaming progress.",
                    content = {
                        @Content(mediaType = MediaType.TEXT_EVENT_STREAM_VALUE,
                                schema = @Schema(implementation = ProgressResponse.class)),
                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ProgressResponse.class))
                    })
                            })
    @PostMapping(value = PULL_STREAM_ENDPOINT, 
                produces = {MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Flux<ServerSentEvent<ProgressResponse>> pullModelStream(
        @RequestParam(required = true, value = MODEL) String model,
        @RequestParam(required = false, value = INSECURE) Boolean insecure,
        @RequestParam(required = false, value = USERNAME) String username,
        @RequestParam(required = false, value = PASSWORD) String password
    ) {
        var builder = PullRequest.builder()
            .model(model);
        if (insecure != null) builder.insecure(insecure);
        if (username != null) builder.username(username);
        if (password != null) builder.password(password);
            
        return ollamaClient.pullStream(builder.build())
        .map(response -> ServerSentEvent.<ProgressResponse>builder()
            .data(response)
            .build())
        .onErrorResume(e -> {
            log.error("Error generating stream", e);
            return Flux.empty();
        });
    
    }

    @Operation(summary = "Copy a model")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED,
                    description = "Model copied successfully",
                    content = @Content(mediaType = CONTENT_TYPE))
    })
    @PostMapping(COPY_ENDPOINT)
    public Mono<ResponseEntity<Object>> copyModel(
            @RequestParam(required = true, value = SOURCE) String source,
            @RequestParam(required = true, value = DESTINATION) String destination) {
        
        CopyRequest copyRequest = new CopyRequest(source, destination);
        return ollamaClient.copy(copyRequest)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> {
                    log.error("Error copying model", e);
                    return Mono.just(ResponseEntity.status(500).build());
                });
    }


    @Operation(summary = "Push a model to remote registry.")
    @ApiResponses(value= {
            @ApiResponse(responseCode = CREATED,
                    description = "Push model to remote registry.",
                    content = @Content(mediaType = CONTENT_TYPE,
                            schema = @Schema(implementation = ProgressResponse.class)))
                            })
    @PostMapping(PUSH_ENDPOINT)
    public Mono<ResponseEntity<ProgressResponse>> pushModel(
        @RequestParam(required = true, value = MODEL) String model,
        @RequestParam(required = false, value = INSECURE) Boolean insecure
    ) {
        var builder = PushRequest.builder()
            .model(model);
        if (insecure != null) builder.insecure(insecure);
        
        return ollamaClient.push(builder.build())
            .map(ResponseEntity::ok)
            .onErrorResume(e -> {
                log.error("Error pushing model", e);
                return Mono.just(ResponseEntity.status(500).body(null));
            });
    }

    @Operation(summary = "Push a model to remote registry with streaming progress.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = CREATED,
            description = "Push model to remote registry with streaming progress.",
            content = @Content(
                mediaType = TEXT_EVENT_STREAM_VALUE,
                schema = @Schema(implementation = ProgressResponse.class)
            )
        )
    })
    @PostMapping(value = PUSH_STREAM_ENDPOINT, produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ProgressResponse>> pushModelStream(
        @RequestParam(required = true, value = MODEL) String model,
        @RequestParam(required = false, value = INSECURE) Boolean insecure
    ) {
        var builder = PushRequest.builder()
            .model(model);
        if (insecure != null) builder.insecure(insecure);
        return ollamaClient.pushStream(builder.build())
            .map(response -> ServerSentEvent.<ProgressResponse>builder()
                .data(response)
                .build())
            .onErrorResume(e -> {
                log.error("Error pushing model stream", e);
                return Flux.empty();
            });        
    }

    @Operation(summary = "Create a model from a Modelfile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED,
                    description = "Model created successfully",
                    content = @Content(mediaType = CONTENT_TYPE,
                            schema = @Schema(implementation = ProgressResponse.class)))
    })
    @PostMapping(CREATE_ENDPOINT)
    public Mono<ResponseEntity<ProgressResponse>> create(
            @RequestParam(required = true, value = MODEL) String model,
            @RequestParam(required = false, value = MODELFILE) String modelfile,
            @RequestParam(required = false, value = QUANTIZE) String quantize) {
        
        var builder = CreateRequest.builder(model);
        if (modelfile != null) builder.modelfile(modelfile);
        if (quantize != null) builder.quantize(quantize);

        ResponseEntity<ProgressResponse> response = ollamaClient.create(builder.build())
            .map(ResponseEntity::ok)
            .onErrorResume(e -> Mono.just(ResponseEntity.status(500).body(
                new ProgressResponse(e.getMessage(), null, null, null)
            )))
            .block();
                
        return Mono.just(response);
    }

    @Operation(summary = "Create a model from a Modelfile with streaming progress")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED,
                    description = "Model creation progress stream",
                    content = @Content(mediaType = TEXT_EVENT_STREAM_VALUE,
                            schema = @Schema(implementation = ProgressResponse.class)))
    })
    @PostMapping(value = CREATE_STREAM_ENDPOINT, produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ProgressResponse>> createStream(
            @RequestParam(required = true, value = MODEL) String model,
            @RequestParam(required = false, value = MODELFILE) String modelfile,
            @RequestParam(required = false, value = QUANTIZE) String quantize) {
        
        var builder = CreateRequest.builder(model);
        if (modelfile != null) builder.modelfile(modelfile);
        if (quantize != null) builder.quantize(quantize);

        return ollamaClient.createStream(builder.build())
                .map(response -> ServerSentEvent.<ProgressResponse>builder()
                        .data(response)
                        .build())
                .onErrorResume(e -> {
                    log.error("Error creating model stream", e);
                    return Flux.empty();
                });
    }

   @Operation(summary = "Delete a model")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED,
                    description = "Model deleted successfully",
                     content = @Content(mediaType = CONTENT_TYPE))
    })
    @DeleteMapping(DELETE_ENDPOINT)
    public Mono<ResponseEntity<Object>> deleteModel(
            @RequestParam(required = true, value = NAME) String name) {
        
        DeleteRequest deleteRequest = new DeleteRequest(name);
        return ollamaClient.delete(deleteRequest)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> {
                    log.error("Error deleting model", e);
                    return Mono.just(ResponseEntity.status(500).build());
                });
    }

    @Operation(summary = "Show model information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED,
                    description = "Show model information",
                    content = @Content(mediaType = CONTENT_TYPE,
                            schema = @Schema(implementation = ShowResponse.class)))
    })
    @PostMapping(SHOW_ENDPOINT)
    public Mono<ResponseEntity<ShowResponse>> showModel(
            @RequestParam(required = true, value = MODEL) String model,
            @RequestParam(required = false, value = SYSTEM) String system,
            @RequestParam(required = false, value = RAW, defaultValue = "false") boolean verbose) {
        
        var showRequest = ShowRequest.builder()
            .model(model);
        if (system != null) showRequest.system(system);
        // verbose will always have a value due to defaultValue = "false"
        showRequest.verbose(verbose);
            
        return ollamaClient.show(showRequest.build())
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> {
                    log.error("Error showing model information", e);
                    return Mono.just(ResponseEntity.status(500).body(null));
                });
    }

    @Operation(summary = "List available models")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List of available models",
                    content = @Content(mediaType = CONTENT_TYPE,
                            schema = @Schema(implementation = ListResponse.class)))
    })
    @GetMapping(LIST_MODELS_ENDPOINT)
    public Mono<ResponseEntity<ListResponse>> listModels() {
        return ollamaClient.listModels()
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error listing models", e);
                    return Mono.just(ResponseEntity.status(500).body(null));
                });
    }

    @Operation(summary = "Check if blob exists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Blob exists"),
            @ApiResponse(responseCode = "404",
                    description = "Blob does not exist")
    })
    @RequestMapping(value = BLOB_ENDPOINT + "/{digest}", method = RequestMethod.HEAD)
    public Mono<?> headBlob(@PathVariable String digest) {
        return ollamaClient.headBlob(digest)
            .map((Boolean exists) -> exists
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build())
            .onErrorResume(e -> {
                log.error("Error checking blob", e);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    // createBlob

    @Operation(summary = "List running models")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List of running models",
                    content = @Content(mediaType = CONTENT_TYPE,
                            schema = @Schema(implementation = ListProcessModelResponse.class)))
    })
    @GetMapping(LIST_RUNNING_MODELS_ENDPOINT)
    public Mono<ResponseEntity<ListProcessModelResponse>> listRunningModels() {
        return ollamaClient.ps()
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error listing running models", e);
                    return Mono.just(ResponseEntity.status(500).body(null));
                });
    }

    @PostMapping(CHAT_ENDPOINT)
    @Operation(summary = "Chat with Ollama model")
    @ApiResponses(value = {
        @ApiResponse(responseCode = CREATED,
            description = "Chat response",
            content = @Content(mediaType = CONTENT_TYPE,
                schema = @Schema(implementation = ChatResponse.class)))
    })
    public Mono<ResponseEntity<ChatResponse>> chat(@RequestBody ChatRequest chatRequest) {
        return ollamaClient.chat(chatRequest)
            .map(response -> ResponseEntity.ok().body(response))
            .onErrorResume(e -> {
                log.error("Chat error: {}", e.getMessage(), e);
                // Create empty ChatResponse with error message
                ChatResponse errorResponse = ChatResponse.builder()
                    .doneReason(e.getMessage())
                    .build();
                
                return Mono.just(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse));
            });
    }

    @PostMapping(value = CHAT_STREAM_ENDPOINT, produces = TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Chat with Ollama model with streaming support")
    @ApiResponse(
        responseCode = CREATED,
        description = "Chat streaming response",
        content = @Content(
            mediaType = TEXT_EVENT_STREAM_VALUE,
            schema = @Schema(implementation = ChatResponse.class)
        )
    )
    public Flux<ServerSentEvent<ChatResponse>> chatStream(@RequestBody ChatRequest chatRequest) {
        return ollamaClient.chatStream(chatRequest)
            .map(response -> ServerSentEvent.<ChatResponse>builder()
                .data(response)
                .build())
            .onErrorResume(e -> {
                log.error("Error generating chat stream", e);
                return Flux.empty();
            });
    }

}
