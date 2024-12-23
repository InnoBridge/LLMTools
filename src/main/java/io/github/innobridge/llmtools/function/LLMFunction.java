package io.github.innobridge.llmtools.function;

import java.util.Map;
import java.util.function.Function;

public interface LLMFunction<T, R> extends Function<T, R> {
    T fromArguments(Map<String, Object> arguments);

    default R apply(Map<String, Object> arguments) {
        return apply(fromArguments(arguments));
    }
}
