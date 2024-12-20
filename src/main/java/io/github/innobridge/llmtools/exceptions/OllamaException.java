package io.github.innobridge.llmtools.exceptions;

public class OllamaException extends RuntimeException {
    private final Integer statusCode;
    private final String errorMessage;
  
    public OllamaException(Integer statusCode, String errorMessage) {
      super(errorMessage);
      this.statusCode = statusCode;
      this.errorMessage = errorMessage;
    }
  
    public Integer getStatusCode() {
      return statusCode;
    }
  
    public String getErrorMessage() {
      return errorMessage;
    } 
}
