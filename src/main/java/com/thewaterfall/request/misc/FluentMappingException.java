package com.thewaterfall.request.misc;

/**
 * The FluentMappingException class is a custom exception used to indicate mapping errors during the
 * conversion of data in the FluentRequest.
 *
 */
public class FluentMappingException extends RuntimeException {
  /**
   * Constructs a FluentMappingException with no specified detail message.
   */
  public FluentMappingException() {
  }

  /**
   * Constructs a FluentMappingException with the specified detail message.
   *
   * @param message The detail message.
   */
  public FluentMappingException(String message) {
    super(message);
  }

  /**
   * Constructs a FluentMappingException with the specified detail message and cause.
   *
   * @param message The detail message.
   * @param cause   The cause of the exception.
   */
  public FluentMappingException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a FluentMappingException with the specified cause.
   *
   * @param cause The cause of the exception.
   */
  public FluentMappingException(Throwable cause) {
    super(cause);
  }
}
