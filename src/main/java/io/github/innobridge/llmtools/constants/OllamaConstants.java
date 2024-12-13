package io.github.innobridge.llmtools.constants;

public class OllamaConstants {
    // Metrics constants
    public static final String TOTAL_DURATION = "total_duration";
    public static final String LOAD_DURATION = "load_duration";
    public static final String PROMPT_EVAL_COUNT = "prompt_eval_count";
    public static final String PROMPT_EVAL_DURATION = "prompt_eval_duration";
    public static final String EVAL_COUNT = "eval_count";
    public static final String EVAL_DURATION = "eval_duration";

    // API Routes
    public static final String API_GENERATE_ROUTE = "/api/generate";
    public static final String API_CREATE_ROUTE = "/api/create";
    public static final String API_PUSH_ROUTE = "/api/push";
    public static final String API_COPY_ROUTE = "/api/copy";
    public static final String API_DELETE_ROUTE = "/api/delete";
    public static final String API_SHOW_ROUTE = "/api/show";
    public static final String V1_MODELS_ROUTE = "/v1/models";
    public static final String API_BLOBS_ROUTE = "/api/blobs";
    public static final String API_PS_ROUTE = "/api/ps";
    public static final String API_PULL_ROUTE = "/api/pull";
    public static final String V1_COMPLETIONS_ROUTE = "/v1/completions";
    public static final String API_CHAT_ROUTE = "/api/chat";
    public static final String API_CHAT_COMPLETIONS_ROUTE = "/v1/chat/completions";
    public static final String API_EMBED_ROUTE = "/api/embed";
    public static final String API_TAGS_ROUTE = "/api/tags";
    public static final String API_VERSION_ROUTE = "/api/version";

    // Endpoints
    public static final String GENERATE_ENDPOINT = "/ollama/generate";
    public static final String GENERATE_STREAM_ENDPOINT = "/ollama/generate/stream";
    public static final String CREATE_ENDPOINT = "/ollama/create";
    public static final String CREATE_STREAM_ENDPOINT = "/ollama/create/stream";
    public static final String EMBED_ENDPOINT = "/ollama/embed";
    public static final String PULL_ENDPOINT = "/ollama/pull";
    public static final String PULL_STREAM_ENDPOINT = "/ollama/pull/stream";
    public static final String COPY_ENDPOINT = "/ollama/copy";
    public static final String PUSH_ENDPOINT = "/ollama/push";
    public static final String PUSH_STREAM_ENDPOINT = "/ollama/push/stream";
    public static final String DELETE_ENDPOINT = "/ollama/delete";
    public static final String SHOW_ENDPOINT = "/ollama/show";

    // Field Names
    public static final String NAME = "name";
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
    public static final String INPUT = "input";
    public static final String TRUNCATE = "truncate";
    public static final String EMBEDDINGS = "embeddings";
    public static final String INSECURE = "insecure";
    public static final String STATUS = "status";
    public static final String DIGEST = "digest";
    public static final String TOTAL = "total";
    public static final String COMPLETED = "completed";
    public static final String SOURCE = "source";
    public static final String DESTINATION = "destination";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String MODELFILE = "modelfile";
    public static final String QUANTIZE = "quantize";
    
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

    // Response Fields
    public static final String CREATED_AT = "created_at";
    public static final String RESPONSE = "response";
    public static final String DONE = "done";
    public static final String DONE_REASON = "done_reason";
    public static final String ROLE = "role";
    public static final String CONTENT = "content";
    public static final String TOOL_CALLS = "tool_calls";
    public static final String FUNCTION = "function";
    public static final String INDEX = "index";
    public static final String ARGUMENTS = "arguments";
    public static final String MESSAGES = "messages";
    public static final String DETAILS = "details";
    public static final String FAMILIES = "families";
    public static final String MODIFIED_AT = "modified_at";
    public static final String PARENT_MODEL = "parent_model";
    public static final String PARAMETER_SIZE = "parameter_size";
    public static final String QUANTIZATION_LEVEL = "quantization_level";
    public static final String MODEL_INFO = "model_info";
    public static final String PROJECTOR_INFO = "projector_info";
    public static final String FAMILY = "family";
    public static final String LICENSE = "license";
    public static final String PARAMETERS = "parameters";
}
