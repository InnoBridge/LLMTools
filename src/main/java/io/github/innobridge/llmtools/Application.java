package io.github.innobridge.llmtools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {
    io.github.innobridge.llmtools.configuration.ApplicationSpecificSpringComponentScanMarker.class,
    io.github.innobridge.llmtools.controller.ApplicationSpecificSpringComponentScanMarker.class
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
