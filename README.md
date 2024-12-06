# LLMTools

# Ollama Client
Documentation for [Ollama API](https://github.com/ollama/ollama/blob/main/docs/api.md#generate-a-completion)

[routes.go](https://github.com/ollama/ollama/blob/main/docs/api.md#generate-a-completion) serves the endpoint for Ollama's API.

## [CreateHander](https://github.com/ollama/ollama/blob/acd7d03266e1b2b1df07c608ba225eb46a57d4cf/server/routes.go#L639)
### POST /api/create


## [PushHandler](https://github.com/ollama/ollama/blob/acd7d03266e1b2b1df07c608ba225eb46a57d4cf/server/routes.go#L574)
### POST /api/push

## [CopyHandler](https://github.com/ollama/ollama/blob/acd7d03266e1b2b1df07c608ba225eb46a57d4cf/server/routes.go#L947)
### POST /api/copy

## [DeleteHandler]
### POST /api/delete

## [ShowHandler]
### POST /api/show
### GET /v1/models/:model

## [CreateBlobHandler]
### POST /api/blob:digest

## [HeadBlobHandler]
### HEAD /api/blob:digest

## [PsHander]
### GET /api/ps

## [PullHandler]
### POST /api/pull

## [GenerateHandler]
### POST /api/generate
### POST /v1/completions

## [ChatHandler]
### POST /api/chat
### POST /v1/chat/completions

## [EmbedHandler]
### POST /api/embed

## [EmbeddingsHander]
### POST /api/embeddings
### POST /v1/embeddings

## [ListHandler]
### GET /v1/models
### GET /v1/tags
### GET /v1/version