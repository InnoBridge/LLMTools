package io.github.innobridge.llmtools.tools;

import io.github.innobridge.llmtools.models.request.ToolFunction;

import static io.github.innobridge.llmtools.constants.OllamaConstants.APPLY;
import static io.github.innobridge.llmtools.constants.OllamaConstants.BOOLEAN;
import static io.github.innobridge.llmtools.constants.OllamaConstants.EQUALS;
import static io.github.innobridge.llmtools.constants.OllamaConstants.GET;
import static io.github.innobridge.llmtools.constants.OllamaConstants.GET_CLASS;
import static io.github.innobridge.llmtools.constants.OllamaConstants.HASH_CODE;
import static io.github.innobridge.llmtools.constants.OllamaConstants.INTEGER;
import static io.github.innobridge.llmtools.constants.OllamaConstants.IS;
import static io.github.innobridge.llmtools.constants.OllamaConstants.LAMBDA;
import static io.github.innobridge.llmtools.constants.OllamaConstants.NUMBER;
import static io.github.innobridge.llmtools.constants.OllamaConstants.OBJECT;
import static io.github.innobridge.llmtools.constants.OllamaConstants.STRING;
import static io.github.innobridge.llmtools.constants.OllamaConstants.TO_STRING;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 * Utility class to convert annotated functions to ToolFunction format
 */
public class FunctionConverter {
    
    /**
     * Converts a class annotated with @FunctionDefinition to a ToolFunction
     * @param clazz The class to convert
     * @return ToolFunction representation of the annotated class
     */
    public static ToolFunction convertToToolFunction(Class<?> clazz) {
        // Get the parameters from the apply method if it's a Function interface
        Method applyMethod = Arrays.stream(clazz.getMethods())
            .filter(method -> method.getName().equals(APPLY))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Class must implement Function interface"));
        

        Class<?> parameterType = applyMethod.getParameterTypes()[0];

        // Build the ToolFunction
        return new ToolFunction(
            getAnnotatedName(clazz),
            getAnnotatedDescription(clazz),
            buildParameters(parameterType)
        );
    }

    private static ToolFunction.Parameters buildParameters(Class<?> parameterType) {
        // For this example, we'll assume the parameter type is a record with getters
        // You can expand this to handle more complex parameter types
        Method[] methods = parameterType.getDeclaredMethods();
        
        Map<String, ToolFunction.Property> properties = Arrays.stream(methods)
            .filter(method -> method.getParameterCount() == 0) // Only get methods with no parameters
            .filter(method -> !method.getName().equals(TO_STRING))
            .filter(method -> !method.getName().equals(HASH_CODE))
            .filter(method -> !method.getName().equals(EQUALS))
            .filter(method -> !method.getName().equals(GET_CLASS))
            .filter(method -> !method.getName().startsWith(LAMBDA))
            .collect(Collectors.<Method, String, ToolFunction.Property>toMap(
                FunctionConverter::getPropertyName,
                method -> {
                    ToolFunction.Property.PropertyBuilder builder = ToolFunction.Property.builder(getPropertyType(method.getReturnType()));
                    
                    // Set description from annotation if present
                    JsonPropertyDescription desc = method.getAnnotation(JsonPropertyDescription.class);
                    if (desc != null) {
                        builder.description(desc.value());
                    }                    

                    // Set enum values if it's an enum type
                    if (method.getReturnType().isEnum()) {
                        builder.enumValues(Arrays.stream(method.getReturnType().getEnumConstants())
                            .map(Object::toString)
                            .toList());
                    }                    

                    return builder.build();
                },
                (existing, replacement) -> existing // Keep the first occurrence in case of duplicates
            ));


        return ToolFunction.Parameters.builder(OBJECT)
            .properties(properties)
            .required(properties.keySet().stream().toList())
            .build();
    }

    private static String getPropertyName(Method method) {
        String name = method.getName();
        if (name.startsWith(GET)) {
            name = name.substring(3);
        } else if (name.startsWith(IS)) {
            name = name.substring(2);
        }
        // Convert first character to lowercase
        if (name.length() > 0) {
            name = Character.toLowerCase(name.charAt(0)) + (name.length() > 1 ? name.substring(1) : "");
        }
        return name;
    }

    private static String getPropertyType(Class<?> type) {
        if (type == String.class) return STRING;
        if (type == Integer.class || type == int.class) return INTEGER;
        if (type == Boolean.class || type == boolean.class) return BOOLEAN;
        if (type == Double.class || type == double.class) return NUMBER;
        return STRING; // default to string for unknown types
    }

    public static String getAnnotatedName(Class<?> clazz) {
        FunctionDefinition annotation = clazz.getAnnotation(FunctionDefinition.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " must be annotated with @FunctionDefinition");
        }
        return annotation.name();
    }

    public static String getAnnotatedDescription(Class<?> clazz) {
        FunctionDefinition annotation = clazz.getAnnotation(FunctionDefinition.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " must be annotated with @FunctionDefinition");
        }
        return annotation.description();
    }
}
