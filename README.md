# LLMTools
Changelog
| Version | Changes |
|---------|---------|
| 0.0.1 | Java Ollama Client for interacting with Ollama API|

## Ollama Client and Controller
Java client for interacting with Ollama API.

### Usage
Reference https://github.com/InnoBridge/LLMToolsDemo

Add the dependency in pom.xml
```
<dependency>
    <groupId>io.github.innobridge</groupId>
    <artifactId>llmtools</artifactId>
    <version>{llmtools version}</version>
</dependency>
```

Add base URL to resources/application.properties
```
ollama.baseurl=http://localhost:11434
```

Configure Spring beans
```

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

    @Bean
    public OllamaController ollamaController(OllamaClient ollamaClient) {
        return new OllamaController(ollamaClient);
    }
}
```

API Documentation
Once configured, access the Swagger UI documentation at:

Local development: http://localhost:8080/swagger-ui/index.html
Or your configured server URL: http://<your-server>/swagger-ui/index.html

### Create Model `POST /ollama/create`
Create a model from a Modelfile. It is recommended to set `modelfile` to the content of the Modelfile rather tha njust set `path`.
This is a requirement for remote create. Remote model creation must alse create any file blobs, fields such as `FROM` and `ADAPTER`, explicitly with the server using Create a Blob and the value to the path indicated in the response.

This endpoint support creating and quantizing models.
When creating a model from an exisging model, you are essentially cloning it with additional parameters and customization.
These customization includes:
- Setting system messages and prompts
- Ajusting model parameters like temperature, top_k, top_p, ...
- Adding specific template and format options.
- Customizing advance options such as context length, batch size and GPU usage.
- Quanitization

Quantization, is the process of creating a smaller model from a larger model by reducing the precision of the numbers used to represent a model's parameters.
This reduces the size of the model and computation requirements, alloging for faster inference and memory usage at the cost of lost of acuracy.

Parameters:
- `model`: name of the model to create
- `modelfile`(optional): contents of the Modelfile 
- `stream`(optional): if `false` the response will be returned as a single response object, rather than a stream of objects
- `path`(optional): path to the Modelfile
- `quantize`(optional): quantize a non-quantized (eg., float16) model

Quantization types
`q2_K`, `q3_K_L`, `q3_K_M`, `q3_K_S`, `q4_0`, `q4_1`, 
`q4_K_M`, `q4_K_S`, `q5_0`, `q5_1`, `q5_K_M`, `q5_K_S`,
`q6_K`, `q8_0`

Creating a new model
Request
```bash
curl http://localhost:8080/ollama/create -d '{
  "model": "mario",
  "modelfile": "FROM llama3\nSYSTEM You are mario from Super Mario Bros."
}'
```

Response
```json
{"status":"reading model metadata"}
{"status":"creating system layer"}
{"status":"using already created layer sha256:22f7f8ef5f4c791c1b03d7eb414399294764d7cc82c7e94aa81a1feb80a983a2"}
{"status":"using already created layer sha256:8c17c2ebb0ea011be9981cc3922db8ca8fa61e828c5d3f44cb6ae342bf80460b"}
{"status":"using already created layer sha256:7c23fb36d80141c4ab8cdbb61ee4790102ebd2bf7aeff414453177d4f2110e5d"}
{"status":"using already created layer sha256:2e0493f67d0c8c9c68a8aeacdf6a38a2151cb3c4c1d42accf296e19810527988"}
{"status":"using already created layer sha256:2759286baa875dc22de5394b4a925701b1896a7e3f8e53275c36f75a877a82c9"}
{"status":"writing layer sha256:df30045fe90f0d750db82a058109cecd6d4de9c90a3d75b19c09e5f64580bb42"}
{"status":"writing layer sha256:f18a68eb09bf925bb1b669490407c1b1251c5db98dc4d3d81f3088498ea55690"}
{"status":"writing manifest"}
{"status":"success"}
```

### PushHandler POST /ollama/push
Upload a model to a model library. Requires registering for ollama.ai and adding a public key first.

Parameters:
- `model`: name of the model to push in the form of `<namespace>/<model>:<tag:>`
- `insecure`(optional): allow insecure connections to the library. Only use if you are pushing to your library during development.
- `stream`(optional): if `false` the response will be returned as a single response obejct, rather than a stream of objects.

Request
```bash
curl http://localhost:8080/ollam/push -d '{
  "model": "mattw/pygmalion:latest"
}'
```

Response
If stream is not specified, or set to true, a stream of JSON objects is returned:

```json
{ "status": "retrieving manifest" }
```

and then:

```json
{
  "status": "starting upload",
  "digest": "sha256:bc07c81de745696fdf5afca05e065818a8149fb0c77266fb584d9b2cba3711ab",
  "total": 1928429856
}
```

Then there is a series of uploading responses:

```json
{
  "status": "starting upload",
  "digest": "sha256:bc07c81de745696fdf5afca05e065818a8149fb0c77266fb584d9b2cba3711ab",
  "total": 1928429856
}
```

Finally, when the upload is complete:

```json
{"status":"pushing manifest"}
{"status":"success"}
```

If stream is set to false, then the response is a single JSON object:

```json
{ "status": "success" }
```

### CopyHandler POST /ollama/copy
Copy a model. Create a model with another name from an existing model.

Parameters:
- `from`: name of the model to copy
- `to`: name of the new model

Request:
```bash
curl http://localhost:8080/ollama/copy -d '{
  "from": "llama3.2:3b-instruct-q4_0",
  "to": "llama3.2:3b-instruct-q4_0-backup"
}'
```

Response:
- 200 OK: if successful
- 404 Not Found: if the model does not exist

### DeleteHandler POST /ollama/delete
Delete a model and its data.

Parameters:
- `model`: name of the model to delete

Request:
```bash
curl http://localhost:8080/ollama/delete -d '{
  "model": "llama3.2:3b-instruct-q4_0"
}'
```

Response:
- 200 OK: if successful
- 404 Not Found: if the model does not exist

### POST /ollama/show
Show information about a model including details, modelfile, template, parameters, license, system prompt

Parameters:
- `model`: name of the model to show
- `verbose`(optional): if set to `true`, returns full data for verbose response fields

Request
```bash
curl http://localhost:8080/ollama/show -d '{
  "model": "llama3.2:3b-instruct-q4_0"
}'
```

Response
```json
{
  "license": "Tongyi Qianwen LICENSE AGREEMENT\n...", // License text truncated for brevity
  "modelfile": "# Modelfile generated by \"ollama show\"\n# To build a new Modelfile based on this, replace FROM with:\n# FROM qwen:32b\n\nFROM /home/yi/.ollama/models/blobs/sha256-936798ec22850321911e13acca1a4e937c2e9776f03509ac244effd94019d3ec\nTEMPLATE \"{{ if .System }}<|im_start|>system\n{{ .System }}<|im_end|>\n{{ end }}{{ if .Prompt }}<|im_start|>user\n{{ .Prompt }}<|im_end|>\n{{ end }}<|im_start|>assistant\n{{ .Response }}<|im_end|>\n\"\nPARAMETER stop <|im_start|>\nPARAMETER stop <|im_end|>\n",
  "parameters": "stop                           \"<|im_start|>\"\nstop                           \"<|im_end|>\"",
  "template": "{{ if .System }}<|im_start|>system\n{{ .System }}<|im_end|>\n{{ end }}{{ if .Prompt }}<|im_start|>user\n{{ .Prompt }}<|im_end|>\n{{ end }}<|im_start|>assistant\n{{ .Response }}<|im_end|>\n",
  "details": {
    "parent_model": "",
    "format": "gguf",
    "family": "qwen2",
    "families": ["qwen2"],
    "parameter_size": "33B",
    "quantization_level": "Q4_0"
  },
  "model_info": {
    "general.architecture": "qwen2",
    "general.file_type": 2,
    "general.parameter_count": 32512218112,
    "general.quantization_version": 2,
    "qwen2.attention.head_count": 40,
    "qwen2.attention.head_count_kv": 8,
    "qwen2.attention.layer_norm_rms_epsilon": 0.000001,
    "qwen2.block_count": 64,
    "qwen2.context_length": 32768,
    "qwen2.embedding_length": 5120,
    "qwen2.feed_forward_length": 27392,
    "qwen2.rope.freq_base": 1000000,
    "tokenizer.ggml.bos_token_id": 151643,
    "tokenizer.ggml.eos_token_id": 151645,
    "tokenizer.ggml.merges": null,
    "tokenizer.ggml.model": "gpt2",
    "tokenizer.ggml.padding_token_id": 151643,
    "tokenizer.ggml.token_type": null,
    "tokenizer.ggml.tokens": null
  },
  "modified_at": "2024-11-28T20:23:06.70482013-08:00"
}
```

### HeadBlobHandler HEAD /ollama/blob:digest
Checks if a blob exists. Ensures that the file blob used for a FROM or ADAPTER field exists on the server.
This is checking yoru Ollama server not ollama.com.

### PsHander GET /ollama/ps

### PullHandler POST /ollama/pull
Download a model from the ollama library. Cancelled pulls are resumed where they left off, and multiple calls will share the same download progress.

Parameters:
- `model`: name of the model to pull
- `insecure`(optional): allow insecure connections to the library. Only use this if you are pulling from your own library during development
- `stream`(optional): if `false` the response will be returned as a single response object, rather than a stream of objects.

Request
```bash
curl http://localhost:8080/ollama/pull -d '{
  "model": "llama3.2"
}'
```

If `stream` was not specified, it is set to `true`.

First Object is the manifest:
```json
{
  "status": "pulling manifest"
}
```

Then there is a series of downloading responses.
```json
{
  "status": "pulling digestname",
  "digest": "digestname",
  "total": 2142590208,
  "completed": 241970
}
```
Until the `completed` key is received.

After all files are downloaded, the final responses are:
```json
{
    "status": "verifying sha256 digest"
}
{
    "status": "writing manifest"
}
{
    "status": "removing any unused layers"
}
{
    "status": "success"
}
```

If `stream` is set to false, then the response is a single JSON object:
```json
{
  "status": "success"
}
```

### GenerateHandler POST /ollama/generate
Generate a response for a given prompt with a provided model. This is a streaming endpint, so there will be a series of responese. The final Response object will include statistics and additional data from the request.

Paramaters:
- `model`(required): the model name
- `prompt`: the prompt to generate a rresponse for
- `suffix`: the text after the model response
- `images`(optional): a list of base64-encoded images (for multimodel modls like `llava`)

Advance Parameters(optional):
- `format`: the formate to return a response iin. Format can be `json` or a JSON schema
- `options`: additional model parameters listed in the documentation for the Modelfile such as `temperature`
- `system`: system message (overrides what is defined in the `Modelfile`)
- `template`: the prompt template to use (overrides what is defined in the `Modelfile`)
- `stream`:(default: `true`) if `false` the response will be returned as a single response object, rather than a stream of objects
- `raw`: if `true` no formatting will be applied to the prompt. You may choose to use the `raw` parameter f you are specifying a full emplate prompt in your request to the API
- `keep_alive`: controls how long the model will stay loaded in memory following the request (default: `5m`)
- `context` (deprecated): the context parameter returned from a previous request to `/generate`. this can be used to keep a short conversational memory

Example streaming
```bash
curl http://localhost:8080/ollama/generate -d '{
  "model": "qwen:32b",
  "prompt": "Why is the sky blue?"
}'
```

The final response in the stream also includes additional data about the generation:
- `total_duration`: time spent generating the response
- `load_duration`: time spend in nanoseconds loading the model
- `prompt_eval_count`: number of tokens in the prompt
- `prompt_eval_duration`: time spent in nanoseconds evaluating the prompt
- `eval_count`: number of tokens in the response
- `eval_duration`: time in nanoseconds spent generating the response
- `context`: an encoding of the conversation used in this response, this can be sent in the next request to keep a conversational memory
- `response`: empty if response is streamed, if not streamed this will contain the full response

#### Load a Model
If an empty prompt is provided, the model will be loaded into memory.
Request
```bash
curl http://localhost:8080/ollama/generate -d '{
  "model": "qwen:32b",
  "prompt": ""
}'
```

Response
```json
{
    "model": "qwen:32b",
    "created_at": "2024-12-07T03:38:49.756672446Z",
    "response": "",
    "done": true,
    "done_reason": "load"
}
```

#### Unload a Model
If an empty prompt is provided and `keep_alive` prameter is set to `0`, a model will be unload from memory.
Request
```bash
curl http://localhost:8080/ollama/generate -d '{
  "model": "qwen:32b",
  "keep_alive": 0
}'
```

Response
```json
{
    "model": "qwen:32b",
    "created_at": "2024-12-07T03:38:49.756672446Z",
    "response": "",
    "done": true,
    "done_reason": "unload"
}
```

### ChatHandler POST /ollama/chat
Generate the next message in a chat with a provided model. This is a streaming endpoint, so there will be a series of responses. Streaming can be disabled using `"stream": false`. The final response object will include statistics and additional data from the request.

Parameters
- `model`(required): the model name
- `messages`: the messages of the chat, this can be sued to keep a chat memory
- `tools`: tools for the model to use if supported. Requires `stream` to be set to `false`

The `message` object has the following fields:
- `role`: the roles of the message, either `system`, `user`, `assistant`, or `tool`
- `content`: the content of the message
- `images`(optional): a list of images to include in the message (for multimodel messages such as `llava`)
- `tool_calls`(optional): a list of tools the model wants to use

Advance parameters (optional):
- `format`: the format to return a response in. Format can be `json` or a JSON schema.
- `options`: additional model parameters listed in the documentation for the Modelfile such as `temperature`
- `stream`: if `false` the response will be returned as a singel response object, rather than as stream of objects
- `keep_alive`: constrols how long the model will stay loaded into memory following the request  (default: `5m`)

#### Difference and Similarities between /ollama/generate and /ollama/chat
Differences:
1. Purpose:
- `/ollama/generate`: Generates a response for a given pompt using a specified model.
- `/ollama/chat`: Generates the next message in a chat with a provided model, maintaining conversational context and memory.
2. Parameters:
- `/ollama/generate`:
    - `prompt`: The prompt to generate a response for.
    - `suffix`: Text to append after the models's response.
    - `images`: List of base64-encoded imates for multimodal models.
- `/ollama/chat`:
    - `messages`: List of messages in the chat to maintain context.
    - `tools`: Tools for the model to use, requires `stream` to be `false`.
3. Streaming:
- Both endpoints support streaming responses when `stream` is set to `true`. However, for `ollama/chat`, streaming is more commonly used to handle ongoing conversations.
4. Contextual Memory:
- `/ollama/generate`: Can use a `context` parameter to maintain short conversational memory.
- `/ollama/chat`: Maintains chat memory through the `messages` parameter, allowing for multi-turn conversations.

Similarities:
1. Response Structure:
- Both endpoints can return streaming or non-streaming responses based on the `stream` parameter.
- Both can return structured outputs using a JSON schema specified in the `format` parameter.
2. Advanced Parameters:
- Both endpoints require specifying a model to use for genrating responses.

#### Where is the conversation context stored in memory?
The memory for the `/ollama/chat` endpoint is managed and allocated within the GPU and system memory, as indicated by various references to memory management in the repository. Specific handling of memory for devices is detailed in files like `ggml-cuda.cu` and other GU-related files.
In a distributed system, if the memory used for maintaining chat context isn't persisted or shared across nodes, it can indeed be lost if the system state isn't properly synchronized or save.

#### Function Calleing with `/ollama/chat` and `/ollama/generate`
`/ollama/chat`:
- Supports function calling using the `tools` parameter.
- The `tools` parameter allows specifying tools for the model to use if supported. This requires `stream` to be set to `false`.
- Example usage includes definign functions that the model can call during the chat session.

`/ollama/generate`:
- Does not natively support function calling throught a dedicated `tools` parameter.
- Primarily used for generating responses based on provided prompts, with options for advanced parameters and no direct function calling capability.

#### Tools in `/ollama/chat`
The `tools` parameter in `/ollama/chat` is indeed for function calling.
It allows the model to utilize defined tools, enabling more complex interactions and integrations within the chat context.

#### Chat With History
Send a chat message with a conversation history. You can use the same approacht to start the conversation using multi-shot or chain-of-thought prompting.

Request
```bash
curl http://localhost:8080/ollama/chat -d '{
  "model": "qwen:32b",
  "stream": false,
  "messages": [
    {
      "role": "user",
      "content": "why is the sky blue?"
    },
    {
      "role": "assistant",
      "content": "due to rayleigh scattering."
    },
    {
      "role": "user",
      "content": "how is that different than mie scattering?"
    }
  ]
}'
```

#### Chat With Tools
Request
```bash
curl http://localhost:8080/ollama/chat -d '{
  "model": "llama3.1:8b-instruct-q4_0",
  "messages": [
    {
      "role": "user",
      "content": "What is the weather today in Paris?"
    }
  ],
  "stream": false,
  "tools": [
    {
      "type": "function",
      "function": {
        "name": "get_current_weather",
        "description": "Get the current weather for a location",
        "parameters": {
          "type": "object",
          "properties": {
            "location": {
              "type": "string",
              "description": "The location to get the weather for, e.g. San Francisco, CA"
            },
            "format": {
              "type": "string",
              "description": "The format to return the weather in, e.g. 'celsius' or 'fahrenheit'",
              "enum": ["celsius", "fahrenheit"]
            }
          },
          "required": ["location", "format"]
        }
      }
    }
  ]
}'
```

Response
```json
{
  "model": "llama3.1:8b-instruct-q4_0",
  "created_at": "2024-12-07T06:00:07.173239545Z",
  "message": {
    "role": "assistant",
    "content": "",
    "tool_calls": [
      {
        "function": {
          "name": "get_current_weather",
          "arguments": {
            "format": "celsius",
            "location": "Paris"
          }
        }
      }
    ]
  },
  "done_reason": "stop",
  "done": true,
  "total_duration": 1558003033,
  "load_duration": 1248594926,
  "prompt_eval_count": 215,
  "prompt_eval_duration": 82000000,
  "eval_count": 25,
  "eval_duration": 224000000
}
```

Another request
```bash
curl http://localhost:8080/ollama/chat -d '{
  "model": "llama3.2:1b",
  "messages": [
    {
      "role": "user",
      "content": "What is the weather today in Paris?"
    }
  ],
  "stream": false,
  "tools": [
    {
      "type": "function",
      "function": {
        "name": "get_current_weather",
        "description": "Get the current weather for a location",
        "parameters": {
          "type": "object",
          "properties": {
            "location": {
              "type": "string",
              "description": "The location to get the weather for, e.g. San Francisco, CA"
            },
            "format": {
              "type": "string",
              "description": "The format to return the weather in, e.g. 'celsius' or 'fahrenheit'",
              "enum": ["celsius", "fahrenheit"]
            }
          },
          "required": ["location", "format"]
        }
      }
    }
  ]
}'
```

Response
```json
{
  "model": "llama3.2:1b",
  "created_at": "2024-12-07T06:12:37.460129755Z",
  "message": {
    "role": "assistant",
    "content": "",
    "tool_calls": [
      {
        "function": {
          "name": "get_current_weather",
          "arguments": {
            "format": "celsius",
            "location": "Paris"
          }
        }
      }
    ]
  },
  "done_reason": "stop",
  "done": true,
  "total_duration": 1140694915,
  "load_duration": 994292041,
  "prompt_eval_count": 213,
  "prompt_eval_duration": 58000000,
  "eval_count": 25,
  "eval_duration": 87000000
}
```

Another request
```bash
curl http://localhost:8080/ollama/chat -d '{
  "model": "llama3.2:3b-instruct-q4_0",
  "messages": [
    {
      "role": "user",
      "content": "What is the weather today in Paris?"
    }
  ],
  "stream": false,
  "tools": [
    {
      "type": "function",
      "function": {
        "name": "get_current_weather",
        "description": "Get the current weather for a location",
        "parameters": {
          "type": "object",
          "properties": {
            "location": {
              "type": "string",
              "description": "The location to get the weather for, e.g. San Francisco, CA"
            },
            "format": {
              "type": "string",
              "description": "The format to return the weather in, e.g. 'celsius' or 'fahrenheit'",
              "enum": ["celsius", "fahrenheit"]
            }
          },
          "required": ["location", "format"]
        }
      }
    }
  ]
}'
```

Response
```json
{
  "model": "llama3.2:3b-instruct-q4_0",
  "created_at": "2024-12-07T06:17:22.139313634Z",
  "message": {
    "role": "assistant",
    "content": "",
    "tool_calls": [
      {
        "function": {
          "name": "get_current_weather",
          "arguments": {
            "format": "celsius",
            "location": "Paris"
          }
        }
      }
    ]
  },
  "done_reason": "stop",
  "done": true,
  "total_duration": 1173673057,
  "load_duration": 986942839,
  "prompt_eval_count": 213,
  "prompt_eval_duration": 58000000,
  "eval_count": 25,
  "eval_duration": 126000000
}
```

### EmbedHandler POST /ollama/embed
Generate embeddings from a model.
Use cases:
- `Semantic Search`: Improve search results by finding semantically similar documents or queries.
- `Recommendation Systems`: Provide relevant recommendations based on user preferences and behaviours.
- `Clustering`: Group similar items or documents together for analysis or organization.
- `Anomaly Detection`: Identify unusual patterns or outliers in data.
- `Sentiment Analysis`: Analyze the sentiment or emotional tone of text.
- `Text Clssification`: Classify text into predefined categories or labels.
- `Image Tagging`: Automatically tag images by comparing their embeddings to predefined tag embeddings.
- `Document Classification`: Assign tags to documents based on their embedding to classify them into categories.

For typical retrieval scenario using embeddings, you can compute distance (such as cosine similarity or Euclidean distance) between the query embedding and the embeddings stored in the vector database.
The closest embeddings, based on the computed distance, are considered the most relevant results.

Request:
```bash
curl http://localhost:8080/ollama/embed -d '{
  "model": "qwen:32b",
  "input": "Why is the sky blue?"
}'
```

### ListHandler GET /ollama/tags
List models that are locally available
Request
```bash
curl http://localhost:8080/ollama/tags
```

Response
```JSON
{
  "models": [
    {
      "name": "llama3.2:3b-instruct-q4_0",
      "model": "llama3.2:3b-instruct-q4_0",
      "modified_at": "2024-12-06T22:17:03.585776218-08:00",
      "size": 1917206179,
      "digest": "9b9453afbdd61f0a345069286517a30d17497cc60d71cfdaef86bf1ef8322dc0",
      "details": {
        "parent_model": "",
        "format": "gguf",
        "family": "llama",
        "families": ["llama"],
        "parameter_size": "3.2B",
        "quantization_level": "Q4_0"
      }
    },
    {
      "name": "qwen:32b",
      "model": "qwen:32b",
      "modified_at": "2024-11-28T20:23:06.70482013-08:00",
      "size": 18498677860,
      "digest": "26e7e8447f5d7fba43b4bc11236fc6f9db4e19ff3184f305b39c7ca76eb896a1",
      "details": {
        "parent_model": "",
        "format": "gguf",
        "family": "qwen2",
        "families": ["qwen2"],
        "parameter_size": "33B",
        "quantization_level": "Q4_0"
      }
    }
  ]
}
```

### Structured Output
Structured outputs are supported by providing a JSON schema in the `schema` parameter and setting `format` to "json". The model will generate a response that matches the schema.

Example Request:
```bash
curl -X POST http://localhost:8080/ollama/generate -H "Content-Type: application/json" -d '{
  "model": "qwen:32b",
  "prompt": "Ollama is 22 years old and is busy saving the world.",
  "stream": false,
  "format": "json",
  "schema": {
    "type": "object",
    "properties": {
      "age": {
        "type": "integer"
      },
      "available": {
        "type": "boolean"
      }
    },
    "required": [
      "age",
      "available"
    ]
  }
}'
```

Response:
```json
{
  "model": "qwen:32b",
  "created_at": "2024-12-07T02:05:03.11301309Z",
  "response": {
    "name": "Ollama",
    "age": 22,
    "occupation": "World saver"
  },
  "done": true,
  "done_reason": "stop",
  "context": [
    151644, 872, 198, 46, 654, 3029, 374, 220, 17, 17, 1635, 2310, 323, 374,
    13028, 13997, 279, 1879, 13, 151645, 198, 151644, 77091, 198, 4913, 606,
    788, 220, 1, 46, 654, 3029, 497, 220, 1, 424, 788, 220, 17, 17, 11, 220,
    1, 58262, 788, 220, 1, 10134, 60162, 9207
  ],
  "total_duration": 2787609509,
  "load_duration": 23129549,
  "prompt_eval_count": 24,
  "prompt_eval_duration": 307000000,
  "eval_count": 23,
  "eval_duration": 2456000000
}
```

Request (JSON mode)
request
```bash
curl http://localhost:8080/ollama/generate -d '{
  "model": "qwen:32b",
  "prompt": "What color is the sky at different times of the day? Respond using JSON",
  "format": "json",
  "stream": false
}'
```

response
```json
{
    "model": "qwen:32b",
    "created_at": "2024-12-07T02:07:14.015720363Z",
    "response": "{\n  \"dawn\": \"pink/orange/yellow\",\n  \"morning\": \"blue with white clouds\",\n  \"noon\": \"bright blue\",\n  \"afternoon\": \"gray/silver during overcast skies, blue otherwise\",\n  \"sunset\": \"red/orange/purple\",\n  \"evening\": \"dark blue/darkening to black as night falls\"\n}",
    "done": true,
    "done_reason": "stop",
    "context": [151644, 872, 198, 3838, 1894, 374, 279, 12884, 518, 2155, 3039, 315, 279, 1899, 30, 39533, 1667, 4718, 151645, 198, 151644, 77091, 198, 90, 198, 256, 1, 67, 6379, 788, 220, 1, 63249, 14, 34164, 14, 27869, 497, 198, 256, 1, 56802, 1229, 788, 220, 1, 12203, 448, 4158, 29514, 497, 198, 256, 1, 12402, 788, 220, 1, 72116, 6303, 497, 198, 256, 1, 10694, 12402, 788, 220, 1, 11650, 14, 81914, 2337, 916, 3829, 49293, 11, 6303, 5937, 497, 198, 256, 1, 82, 36804, 788, 220, 1, 1151, 14, 34164, 14, 56507, 497, 198, 256, 1, 16788, 287, 788, 220, 1, 22624, 6303, 14, 22624, 6019, 311, 3691, 438, 3729, 17066, 1, 198, 92],
    "total_duration": 9627586938,
    "load_duration": 12232778,
    "prompt_eval_count": 23,
    "prompt_eval_duration": 770000000,
    "eval_count": 80,
    "eval_duration": 8844000000
}
```

### Reproducible outputs
For repoducible outputs, set `seed` to a number:
Request
```bash
curl http://localhost:8080/ollama/generate -d '{
  "model": "qwen:32b",
  "prompt": "Why is the sky blue?",
  "stream": false,
  "options": {
    "seed": 123
  }
}'
```

Response
```json
{
  "model": "qwen:32b",
  "created_at": "2024-12-07T03:33:41.598549841Z",
  "response": "The sky appears blue to us because of a phenomenon called Rayleigh scattering. As sunlight enters Earth's atmosphere, it encounters tiny gas molecules and other particles like dust and water droplets. These particles scatter the light in all directions.\n\nBlue light has a shorter wavelength and higher energy compared to other colors in the visible spectrum. As a result, it is scattered more easily by the small particles in the air. This causes blue light to be dispersed in all directions, including downward towards the Earth's surface.\n\nWhen we look up at the sky, we see this scattered blue light as the dominant color because our eyes are more sensitive to blue light than other colors. During sunrise and sunset, when sunlight has to travel through more of the Earth's atmosphere, the blue light is scattered away even more, leaving behind longer wavelength colors like red, orange, and yellow, which is why the sky often appears reddish during these times.\n\nSo, in summary, the sky appears blue due to the scattering of sunlight by the tiny particles in the Earth's atmosphere, with blue light being scattered more than other colors.",
  "done": true,
  "done_reason": "stop",
  "context": [
    151644, 872, 198, 10234, 374, 279, 12884, 6303, 30, 151645, 198, 151644,
    77091, 198, 785, 12884, 7952, 6303, 311, 601, 1576, 315, 264, 24844, 2598,
    13255, 62969, 71816, 13, 1634, 39020, 28833, 9237, 6, 82, 16566, 11, 432,
    33906, 13673, 6819, 34615, 323, 1008, 18730, 1075, 15797, 323, 3015, 6973,
    89492, 13, 4220, 18730, 44477, 279, 3100, 304, 678, 17961, 13, 198, 198,
    10331, 3100, 702, 264, 23327, 45306, 323, 5080, 4802, 7707, 311, 1008,
    7987, 304, 279, 9434, 19745, 13, 1634, 264, 1102, 11, 432, 374, 36967,
    803, 6707, 553, 279, 2613, 18730, 304, 279, 3720, 13, 1096, 11137, 6303,
    3100, 311, 387, 76710, 304, 678, 17961, 11, 2670, 44478, 6974, 279, 9237,
    6, 82, 7329, 13, 715, 198, 4498, 582, 1401, 705, 518, 279, 12884, 11, 582,
    1490, 419, 36967, 6303, 3100, 438, 279, 24456, 1894, 1576, 1039, 6414,
    525, 803, 16216, 311, 6303, 3100, 1091, 1008, 7987, 13, 11954, 63819, 323,
    42984, 11, 979, 39020, 702, 311, 5821, 1526, 803, 315, 279, 9237, 6, 82,
    16566, 11, 279, 6303, 3100, 374, 36967, 3123, 1496, 803, 11, 9380, 4815,
    5021, 45306, 7987, 1075, 2518, 11, 18575, 11, 323, 13753, 11, 892, 374,
    3170, 279, 12884, 3545, 7952, 62144, 812, 2337, 1493, 3039, 13, 198, 198,
    4416, 11, 304, 12126, 11, 279, 12884, 7952, 6303, 4152, 311, 279, 71816,
    315, 39020, 553, 279, 13673, 18730, 304, 279, 9237, 6, 82, 16566, 11, 448,
    6303, 3100, 1660, 36967, 803, 1091, 1008, 7987, 13
  ],
  "total_duration": 24211420082,
  "load_duration": 33490617,
  "prompt_eval_count": 14,
  "prompt_eval_duration": 113000000,
  "eval_count": 221,
  "eval_duration": 24063000000
}
```
