package io.github.innobridge.llmtools.client;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.github.innobridge.llmtools.exceptions.OllamaException;
import io.github.innobridge.llmtools.models.request.ChatCompletionsRequest;
import io.github.innobridge.llmtools.models.request.ChatRequest;
import io.github.innobridge.llmtools.models.request.CompletionsRequest;
import io.github.innobridge.llmtools.models.request.CopyRequest;
import io.github.innobridge.llmtools.models.request.CreateRequest;
import io.github.innobridge.llmtools.models.request.DeleteRequest;
import io.github.innobridge.llmtools.models.request.EmbedRequest;
import io.github.innobridge.llmtools.models.request.GenerateRequest;
import io.github.innobridge.llmtools.models.request.PullRequest;
import io.github.innobridge.llmtools.models.request.PushRequest;
import io.github.innobridge.llmtools.models.request.ShowRequest;
import io.github.innobridge.llmtools.models.response.EmbedResponse;
import io.github.innobridge.llmtools.models.response.GenerateResponse;
import io.github.innobridge.llmtools.models.response.ListResponse;
import io.github.innobridge.llmtools.models.response.ProgressResponse;
import io.github.innobridge.llmtools.models.response.ShowResponse;
import io.github.innobridge.llmtools.models.response.ListProcessModelResponse;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.http.HttpMethod;

import static io.github.innobridge.llmtools.constants.OllamaConstants.API_GENERATE_ROUTE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.API_CREATE_ROUTE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.API_PUSH_ROUTE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.API_COPY_ROUTE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.API_DELETE_ROUTE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.API_SHOW_ROUTE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.API_BLOBS_ROUTE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.API_PS_ROUTE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.API_PULL_ROUTE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.V1_COMPLETIONS_ROUTE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.API_CHAT_ROUTE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.API_CHAT_COMPLETIONS_ROUTE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.API_EMBED_ROUTE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.API_TAGS_ROUTE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.API_VERSION_ROUTE;

/**
 * Implementation of the OllamaClient interface using WebClient.
 */
@Slf4j
public class OllamaClientImpl implements OllamaClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public OllamaClientImpl(WebClient webclient) {
        this.webClient = webclient;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Mono<ProgressResponse> create(CreateRequest request) {
        request.setStream(false);
        return webClient.post()
                .uri(API_CREATE_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), this::handleErrorResponse)
                .bodyToMono(ProgressResponse.class)
                .doOnError(this::handleErrorLogging);
    }

    @Override
    public Flux<ProgressResponse> createStream(CreateRequest request) {
        request.setStream(true);
        return webClient.post()
                .uri(API_CREATE_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), this::handleErrorResponse)
                .bodyToFlux(ProgressResponse.class)
                .doOnError(this::handleErrorLogging);
    }

    @Override
    public Mono<Void> delete(DeleteRequest request) {
        return webClient.method(HttpMethod.DELETE)
                .uri(API_DELETE_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }

    @Override
    public Mono<Void> copy(CopyRequest request) {
        return webClient.post()
                .uri(API_COPY_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }

    @Override
    public Mono<ShowResponse> show(ShowRequest request) {
        return webClient.post()
                .uri(API_SHOW_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShowResponse.class);
    }

    // @Override
    // public Mono<String> createBlob(String digest, MultipartFile file) {
        // return null;
        // return DataBufferUtils.join(file.getResource().getContent())
        //         .flatMap(dataBuffer -> webClient.post()
        //                 .uri(API_BLOBS_ROUTE + "/{digest}", digest)
        //                 .contentType(MediaType.APPLICATION_OCTET_STREAM)
        //                 .bodyValue(dataBuffer)
        //                 .retrieve()
        //                 .bodyToMono(String.class));
    // }

    @Override
    public Mono<Boolean> headBlob(String digest) {
        return webClient.head()
                .uri(API_BLOBS_ROUTE + "/{digest}", digest)
                .exchangeToMono(response -> Mono.just(response.statusCode().is2xxSuccessful()));
    }

    @Override
    public Mono<ListProcessModelResponse> ps() {
        return webClient.get()
                .uri(API_PS_ROUTE)
                .retrieve()
                .bodyToMono(ListProcessModelResponse.class);
    }

    @Override
    public Mono<ProgressResponse> pull(PullRequest request) {
        request = request.setStream(false);
        return webClient.post()
                .uri(API_PULL_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ProgressResponse.class)
                .doOnError(this::handleErrorLogging);
    }

    @Override
    public Flux<ProgressResponse> pullStream(PullRequest request) {
        request = request.setStream(true);
        return webClient.post()
                .uri(API_PULL_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(ProgressResponse.class)
                .doOnError(this::handleErrorLogging);
    }

    @Override
    public Mono<ProgressResponse> push(PushRequest request) {
        request = request.setStream(false);
        return webClient.post()
                .uri(API_PUSH_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), this::handleErrorResponse)
                .bodyToMono(ProgressResponse.class)
                .doOnError(this::handleErrorLogging);
    }

    @Override
    public Flux<ProgressResponse> pushStream(PushRequest request) {
        request = request.setStream(true);
        return webClient.post()
                .uri(API_PUSH_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(ProgressResponse.class)
                .doOnError(this::handleErrorLogging);
    }

    @Override
    public Mono<GenerateResponse> generate(GenerateRequest request) {
        request.setStream(false);
        return webClient.post()
                .uri(API_GENERATE_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), this::handleErrorResponse)
                .bodyToMono(GenerateResponse.class)
                .doOnError(this::handleErrorLogging);
    }

    @Override
    public Flux<GenerateResponse> generateStream(GenerateRequest request) {
        request.setStream(true);
        return webClient.post()
                .uri(API_GENERATE_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), this::handleErrorResponse)
                .bodyToFlux(GenerateResponse.class)
                .doOnError(this::handleErrorLogging);
    }

    @Override
    public Flux<String> completions(String model, String prompt, boolean stream) {
        return webClient.post()
                .uri(V1_COMPLETIONS_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(new CompletionsRequest(model, prompt, stream))
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(response -> {
                    try {
                        JsonNode node = objectMapper.readTree(response);
                        return Mono.just(node.path("choices").get(0).path("text").asText());
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
    }

    @Override
    public Flux<String> chat(String model, String messages, boolean stream) {
        return webClient.post()
                .uri(API_CHAT_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(new ChatRequest(model, messages, stream))
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(response -> {
                    try {
                        JsonNode node = objectMapper.readTree(response);
                        return Mono.just(node.path("message").path("content").asText());
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
    }

    @Override
    public Flux<String> chatCompletions(String model, String messages, boolean stream) {
        return webClient.post()
                .uri(API_CHAT_COMPLETIONS_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(new ChatCompletionsRequest(model, messages, stream))
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(response -> {
                    try {
                        JsonNode node = objectMapper.readTree(response);
                        return Mono.just(node.path("choices").get(0).path("message").path("content").asText());
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
    }

    @Override
    public Mono<EmbedResponse> embed(EmbedRequest request) {
        return webClient.post()
                .uri(API_EMBED_ROUTE)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EmbedResponse.class);
    }

    @Override
    public Mono<ListResponse> listModels() {
        return webClient.get()
                .uri(API_TAGS_ROUTE)
                .retrieve()
                .bodyToMono(ListResponse.class);
    }

    @Override
    public Mono<String> getVersion() {
        return webClient.get()
                .uri(API_VERSION_ROUTE)
                .retrieve()
                .bodyToMono(String.class);
    }

    private Mono<Throwable> handleErrorResponse(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class).flatMap(errorMessage -> {
          return Mono.error(new OllamaException(clientResponse.statusCode().value(), errorMessage));
        });
    }

    private void handleErrorLogging(Throwable ex) {
        if (ex instanceof OllamaException) {
            OllamaException ollamaEx = (OllamaException) ex;
            log.error("Error calling Ollama API: Status Code: {} - Message: {}", ollamaEx.getStatusCode(),
                ollamaEx.getErrorMessage());
        }
      }    
}
