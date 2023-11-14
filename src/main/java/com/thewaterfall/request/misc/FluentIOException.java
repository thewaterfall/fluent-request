package com.thewaterfall.request.misc;

/**
 * The FluentIOException class is a custom exception used to indicate IO errors during the
 * work with request and response in the FluentRequest.
 *
 */
public class FluentIOException extends RuntimeException {
  /**
   * Constructs a FluentIOException with no specified detail message.
   */
  public FluentIOException() {
  }

  /**
   * Constructs a FluentIOException with the specified detail message.
   *
   * @param message The detail message.
   */
  public FluentIOException(String message) {
    super(message);
  }

  /**
   * Constructs a FluentIOException with the specified detail message and cause.
   *
   * @param message The detail message.
   * @param cause   The cause of the exception.
   */
  public FluentIOException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a FluentIOException with the specified cause.
   *
   * @param cause The cause of the exception.
   */
  public FluentIOException(Throwable cause) {
    super(cause);
  }
}
