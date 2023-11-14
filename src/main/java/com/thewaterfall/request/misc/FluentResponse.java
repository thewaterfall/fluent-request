package com.thewaterfall.request.misc;

import okhttp3.Response;

/**
 * The FluentResponse class represents the response of an HTTP request with a typed body.
 *
 * @param <T> The type of the response body.
 */
public class FluentResponse<T> {
  private final T body;
  private final Response response;

  /**
   * Constructs a FluentResponse with the specified body and response.
   *
   * @param body     The typed body of the response.
   * @param response The raw HTTP response.
   */
  public FluentResponse(T body, Response response) {
    this.body = body;
    this.response = response;
  }

  /**
   * Gets the typed body of the response.
   *
   * @return The typed body of the response.
   */
  public T getBody() {
    return body;
  }

  /**
   * Gets the raw OkHttp response.
   *
   * @return The raw HTTP response.
   */
  public Response getResponse() {
    return response;
  }
}
