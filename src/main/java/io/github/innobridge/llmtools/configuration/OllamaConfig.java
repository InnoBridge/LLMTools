package io.github.innobridge.llmtools.configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;

import io.github.innobridge.llmtools.client.OllamaClient;
import io.github.innobridge.llmtools.client.OllamaClientImpl;
import lombok.extern.slf4j.Slf4j;

@Configuration
public class OllamaConfig {
    
    @Bean
    public OllamaClient ollamaClient(
            @Value("${ollama.baseurl}") String baseUrl
    ) {
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
                
        return new OllamaClientImpl(webClient);
    }
}
