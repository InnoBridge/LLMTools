package io.github.innobridge.llmtools.exceptions;

public class LLMFunctionException extends RuntimeException {
    public LLMFunctionException(String message) {
        super(message);
    }

    public LLMFunctionException(String message, Throwable cause) {
        super(message, cause);
    }
}
