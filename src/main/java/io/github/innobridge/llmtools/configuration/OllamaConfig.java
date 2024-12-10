package io.github.innobridge.llmtools.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.innobridge.llmtools.client.OllamaClient;
import io.github.innobridge.llmtools.client.OllamaClientImpl;

@Configuration
public class OllamaConfig {
    
    @Bean
    public OllamaClient ollamaClient(@Value("${ollama.baseurl}") String baseUrl) {
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
                
        return new OllamaClientImpl(webClient);
    }
}
