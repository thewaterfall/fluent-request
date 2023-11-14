package com.thewaterfall.request;

import com.thewaterfall.request.misc.FluentPair;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>The FluentMultipartBody class provides a fluent interface for building multipart form data request bodies
 * to be used with the FluentRequest.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code FluentRequest.request("https://api.example.com")
 *     .multipart()
 *         .add("key1", "value1")
 *         .add("key2", "value2")
 *         .add("fileKey", "filename.txt", new File("path/to/file.txt"))
 *     .build()
 *     .post();}</pre>
*
 * @param <T> The type of the response body expected.
 * @see FluentRequest
 */
public class FluentMultipartBody<T> {
  private final FluentRequest.Builder<T> fluentRequest;

  private final Map<String, String> parts = new HashMap<>();
  private final Map<String, FluentPair<String, File>> fileParts = new HashMap<>();

  /**
   * Constructs a FluentMultipartBody instance associated with a given FluentRequest.
   *
   * @param fluentRequest The FluentRequest instance to associate with the multipart builder.
   */
  public FluentMultipartBody(FluentRequest.Builder<T> fluentRequest) {
    this.fluentRequest = fluentRequest;
  }

  /**
   * Adds a key-value pair to the multipart form data.
   *
   * @param key   The key of the form field.
   * @param value The value of the form field.
   * @return The FluentMultipartBody instance for method chaining.
   */
  public FluentMultipartBody<T> add(String key, String value) {
    this.parts.put(key, value);
    return this;
  }

  /**
   * Adds multiple key-value pairs to the multipart form data.
   *
   * @param values A Map containing key-value pairs to be added to the form data.
   * @return The FluentMultipartBody instance for method chaining.
   */
  public FluentMultipartBody<T> add(Map<String, String> values) {
    this.parts.putAll(values);
    return this;
  }

  /**
   * Adds a file to the multipart form data.
   *
   * @param key      The key of the form field.
   * @param filename The filename of the file part.
   * @param file     The File object representing the file to be included.
   * @return The FluentMultipartBody instance for method chaining.
   */
  public FluentMultipartBody<T> add(String key, String filename, File file) {
    this.fileParts.put(key, FluentPair.of(filename, file));
    return this;
  }

  /**
   * Builds the multipart form data body and associates it with the original FluentRequest.
   *
   * @return The original FluentRequest with the multipart form data body configured.
   */
  public FluentRequest.Builder<T> build() {
    MultipartBody.Builder builder = new MultipartBody.Builder()
        .setType(MultipartBody.FORM);

    parts.forEach((k, v) -> builder.addFormDataPart(k, v));
    fileParts.forEach((k, v) -> builder.addFormDataPart(k, v.getKey(),
        RequestBody.create(v.getValue(), MediaType.parse("application/octet-stream"))));

    fluentRequest.body(builder.build());
    return fluentRequest;
  }
}
