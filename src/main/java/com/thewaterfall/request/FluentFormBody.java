package com.thewaterfall.request;

import okhttp3.FormBody;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>The FluentFormBody class provides a fluent interface for building form-urlencoded request bodies
 * to be used with the FluentRequest.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code FluentRequest.request("https://api.example.com")
 *     .form()
 *         .add("key1", "value1")
 *         .add("key2", "value2")
 *     .build()
 *     .post();}</pre>
 *
 * @param <T> The type of the response body expected.
 * @see FluentRequest
 */
public class FluentFormBody<T> {
  private final FluentRequest.Builder<T> fluentRequest;
  private final Map<String, String> body = new HashMap<>();

  /**
   * Constructs a FluentFormBody instance associated with a given FluentRequest.
   *
   * @param fluentRequest The FluentRequest instance to associate with the form builder.
   */
  public FluentFormBody(FluentRequest.Builder<T> fluentRequest) {
    this.fluentRequest = fluentRequest;
  }

  /**
   * Adds a key-value pair to the form body.
   *
   * @param key   The key of the form field.
   * @param value The value of the form field.
   * @return The FluentFormBody instance for method chaining.
   */
  public FluentFormBody<T> add(String key, String value) {
    this.body.put(key, value);
    return this;
  }

  /**
   * Adds multiple key-value pairs to the form body.
   *
   * @param values A Map containing key-value pairs to be added to the form body.
   * @return The FluentFormBody instance for method chaining.
   */
  public FluentFormBody<T> add(Map<String, String> values) {
    this.body.putAll(values);
    return this;
  }

  /**
   * Builds the form body and associates it with the original FluentRequest.
   *
   * @return The original FluentRequest with the form body configured.
   */
  public FluentRequest.Builder<T> build() {
    FormBody.Builder builder = new FormBody.Builder();

    body.forEach(builder::add);
    fluentRequest.body(builder.build());

    return fluentRequest;
  }
}
