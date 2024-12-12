package io.github.innobridge.llmtools.client;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import io.github.innobridge.llmtools.models.request.EmbedRequest;
import io.github.innobridge.llmtools.models.request.GenerateRequest;
import io.github.innobridge.llmtools.models.request.PullRequest;
import io.github.innobridge.llmtools.models.request.CopyRequest;
import io.github.innobridge.llmtools.models.request.CreateRequest;
import io.github.innobridge.llmtools.models.request.PushRequest;
import io.github.innobridge.llmtools.models.request.DeleteRequest;
import io.github.innobridge.llmtools.models.response.EmbedResponse;
import io.github.innobridge.llmtools.models.response.GenerateResponse;
import io.github.innobridge.llmtools.models.response.ProgressResponse;

/**
 * Interface for interacting with the Ollama API.
 */
public interface OllamaClient {
    /**
     * Create a model from a Modelfile.
     * @param request The create request containing model name, modelfile contents, and path
     */
    Mono<ProgressResponse> create(CreateRequest request);

    /**
     * Create a model from a Modelfile with streaming support.
     * @param request The create request containing model name, modelfile contents, and path
     */
    Flux<ProgressResponse> createStream(CreateRequest request);

    /**
     * Copy a model.
     * @param request The copy request containing source and destination model names
     */
    Mono<Void> copy(CopyRequest request);

    /**
     * Delete a model.
     * @param request The delete request containing the model name
     */
    Mono<Void> delete(DeleteRequest request);

    /**
     * Show details about a model.
     * @param name The name of the model
     */
    Mono<String> show(String name);

    /**
     * Get model information.
     * @param model The name of the model
     */
    Mono<String> getModel(String model);

    /**
     * Create a blob.
     * @param digest The digest of the blob
     * @param file The file to upload
     */
    Mono<String> createBlob(String digest, MultipartFile file);

    /**
     * Check if a blob exists.
     * @param digest The digest of the blob
     */
    Mono<Boolean> headBlob(String digest);

    /**
     * List running models.
     */
    Mono<String> ps();

    /**
     * Pull a model from a registry.
     * @param request The pull request containing model, username, password, and other parameters
     */
    Mono<ProgressResponse> pull(PullRequest request);

    /**
     * Pull a model from a registry with streaming support.
     * @param request The pull request containing model, username, password, and other parameters
     */
    Flux<ProgressResponse> pullStream(PullRequest request);

    /**
     * Push a model to a remote registry.
     * @param request The push request containing model name and other parameters
     */
    Mono<ProgressResponse> push(PushRequest request);

    /**
     * Push a model to a remote registry with streaming support.
     * @param request The push request containing model name and other parameters
     */
    Flux<ProgressResponse> pushStream(PushRequest request);

    /**
     * Generate a response from a model.
     * @param request The generate request containing model, prompt, and other parameters
     */
    Mono<GenerateResponse> generate(GenerateRequest request);

    /**
     * Generate a response from a model with streaming support.
     * @param request The generate request containing model, prompt, and other parameters
     */
    Flux<GenerateResponse> generateStream(GenerateRequest request);

    /**
     * Generate completions from a model.
     * @param model The name of the model
     * @param prompt The prompt to generate completions from
     */
    default Mono<String> completions(String model, String prompt) {
        return completions(model, prompt, false).collectList().map(list -> String.join("", list));
    }

    /**
     * Generate completions from a model with streaming support.
     * @param model The name of the model
     * @param prompt The prompt to generate completions from
     * @param stream If true, returns a streaming response
     */
    Flux<String> completions(String model, String prompt, boolean stream);

    /**
     * Chat with a model.
     * @param model The name of the model
     * @param messages The chat messages
     */
    default Mono<String> chat(String model, String messages) {
        return chat(model, messages, false).collectList().map(list -> String.join("", list));
    }

    /**
     * Chat with a model with streaming support.
     * @param model The name of the model
     * @param messages The chat messages
     * @param stream If true, returns a streaming response
     */
    Flux<String> chat(String model, String messages, boolean stream);

    /**
     * Chat completions with a model.
     * @param model The name of the model
     * @param messages The chat messages
     */
    default Mono<String> chatCompletions(String model, String messages) {
        return chatCompletions(model, messages, false).collectList().map(list -> String.join("", list));
    }

    /**
     * Chat completions with a model with streaming support.
     * @param model The name of the model
     * @param messages The chat messages
     * @param stream If true, returns a streaming response
     */
    Flux<String> chatCompletions(String model, String messages, boolean stream);

    /**
     * Generate embeddings from a model.
     * @param model The name of the model
     * @param prompt The text to generate embeddings for
     */
    Mono<EmbedResponse> embed(EmbedRequest request);

    /**
     * List available models.
     */
    Mono<String> listModels();

    /**
     * List available model tags.
     */
    Mono<String> listTags();

    /**
     * Get API version.
     */
    Mono<String> getVersion();
}
