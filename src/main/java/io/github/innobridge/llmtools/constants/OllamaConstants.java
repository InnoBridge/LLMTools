package io.github.innobridge.llmtools.constants;

public class OllamaConstants {
    // API Routes
    public static final String API_GENERATE_ROUTE = "/api/generate";
    public static final String API_CREATE_ROUTE = "/api/create";
    public static final String API_PUSH_ROUTE = "/api/push";
    public static final String API_COPY_ROUTE = "/api/copy";
    public static final String API_DELETE_ROUTE = "/api/delete";
    public static final String API_SHOW_ROUTE = "/api/show";
    public static final String API_MODELS_ROUTE = "/v1/models";
    public static final String API_BLOBS_ROUTE = "/api/blobs";
    public static final String API_PS_ROUTE = "/api/ps";
    public static final String API_PULL_ROUTE = "/api/pull";
    public static final String API_COMPLETIONS_ROUTE = "/v1/completions";
    public static final String API_CHAT_ROUTE = "/api/chat";
    public static final String API_CHAT_COMPLETIONS_ROUTE = "/v1/chat/completions";
    public static final String API_EMBED_ROUTE = "/api/embed";
    public static final String API_TAGS_ROUTE = "/api/tags";
    public static final String API_VERSION_ROUTE = "/api/version";

    // Endpoints
    public static final String GENERATE_ENDPOINT = "/ollama/generate";
    public static final String GENERATE_STREAM_ENDPOINT = "/ollama/generate/stream";

    // Field Names
    // Generate
    public static final String MODEL = "model";
    public static final String PROMPT = "prompt";
    public static final String SUFFIX = "suffix";
    public static final String SYSTEM = "system";
    public static final String TEMPLATE = "template";
    public static final String CONTEXT = "context";
    public static final String STREAM = "stream";
    public static final String RAW = "raw";
    public static final String FORMAT = "format";
    public static final String KEEP_ALIVE = "keep_alive";
    public static final String IMAGES = "images";

    // Options
    public static final String NUM_KEEP = "num_keep";
    public static final String SEED = "seed";
    public static final String NUM_PREDICT = "num_predict";
    public static final String TOP_K = "top_k";
    public static final String TOP_P = "top_p";
    public static final String MIN_P = "min_p";
    public static final String TFS_Z = "tfs_z";
    public static final String TYPICAL_P = "typical_p";
    public static final String REPEAT_LAST_N = "repeat_last_n";
    public static final String TEMPERATURE = "temperature";
    public static final String REPEAT_PENALTY = "repeat_penalty";
    public static final String PRESENCE_PENALTY = "presence_penalty";
    public static final String FREQUENCY_PENALTY = "frequency_penalty";
    public static final String MIROSTAT = "mirostat";
    public static final String MIROSTAT_TAU = "mirostat_tau";
    public static final String MIROSTAT_ETA = "mirostat_eta";
    public static final String PENALIZE_NEWLINE = "penalize_newline";
    public static final String STOP = "stop";

}
