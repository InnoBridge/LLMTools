package io.github.innobridge.llmtools.tools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define function metadata for LLM tools.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface FunctionDefinition {
    /**
     * The name of the function
     */
    String name();

    /**
     * The description of what the function does
     */
    String description();
}
