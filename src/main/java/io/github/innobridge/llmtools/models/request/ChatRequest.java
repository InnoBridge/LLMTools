package io.github.innobridge.llmtools.models.request;

public record ChatRequest(String model, String messages, boolean stream) {}
