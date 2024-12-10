package io.github.innobridge.llmtools.models.request;

public record CompletionsRequest(String model, String prompt, boolean stream) {}
