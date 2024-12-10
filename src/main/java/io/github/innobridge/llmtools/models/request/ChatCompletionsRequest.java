package io.github.innobridge.llmtools.models.request;

public record ChatCompletionsRequest(String model, String messages, boolean stream) {}
