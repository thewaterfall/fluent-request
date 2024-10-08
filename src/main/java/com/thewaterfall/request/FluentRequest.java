package com.thewaterfall.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewaterfall.request.misc.*;
import okhttp3.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>The FluentRequest class is a versatile HTTP request builder that provides a fluent interface
 * for constructing and sending HTTP requests using the OkHttp library. The builder supports various HTTP methods,
 * request body types, headers, and authentication methods.</p>
 *
 * <p>It uses a predefined OkHttpClient and if it needs to be customized and configured,
 * use {@link FluentRequest#overrideClient(OkHttpClient)}. Same for Jackson ObjectMapper,
 * use {@link FluentRequest#overrideMapper(ObjectMapper)}</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code FluentRequest.request("https://api.example.com", Example.class)
 *     .bearer(EXAMPLE_TOKEN)
 *     .body(body)
 *     .post();}</pre>
 */
public class FluentRequest {
  private static OkHttpClient client = new OkHttpClient.Builder()
      .connectTimeout(10, TimeUnit.SECONDS)
      .writeTimeout(10, TimeUnit.SECONDS)
      .readTimeout(30,TimeUnit.SECONDS)
      .build();

  private static ObjectMapper mapper = new ObjectMapper();

  /**
   * <p>Initiates a new HTTP request builder with the specified URL and no response type (no body deserialization will
   * happen).</p>
   *
   * <p>Example:</p>
   * <pre>{@code
   * FluentRequest.request("https://example.com/articles/1")
   *     .get();
   * }</pre>
   *
   * @param url The URL for the HTTP request.
   * @return A Builder instance for configuring the request.
   */
  public static Builder<byte[]> request(String url) {
    return new Builder<>(url, byte[].class, client);
  }

  /**
   * <p>Initiates a new HTTP request builder with the specified URL and no response type (no body deserialization will
   * happen).</p>
   *
   * <p>Example:</p>
   * <pre>{@code
   * FluentRequest.request("https://example.com/articles/1", client)
   *     .get();
   * }</pre>
   *
   * @param url    The URL for the HTTP request.
   * @param client The OkHttpClient to use for this specific request.
   * @return A Builder instance for configuring the request.
   */
  public static Builder<byte[]> request(String url, OkHttpClient client) {
    return new Builder<>(url, byte[].class, client);
  }

  /**
   * <p>Initiates a new HTTP request builder with the specified URL and response type,
   * using the default OkHttpClient.</p>
   *
   * <p>Example:</p>
   * <pre>{@code
   * FluentRequest.request("https://example.com/articles/1", Article.class)
   *     .get();
   * }</pre>
   *
   * @param url          The URL for the HTTP request.
   * @param responseType The class type of the expected response.
   * @param <T>          The type of the expected response.
   * @return A Builder instance for configuring the request.
   */
  public static <T> Builder<T> request(String url, Class<T> responseType) {
    return new Builder<>(url, responseType, client);
  }

  /**
   * <p>Initiates a new HTTP request builder with the specified URL and response type reference,
   * using the default OkHttpClient. Allows to use a generic type as a response body type.</p>
   *
   * <p>Example:</p>
   * <pre>{@code
   * FluentRequest.request("https://example.com/articles", new TypeReference<List<Article>>() {})
   *     .get();
   * }</pre>>
   *
   * @param url          The URL for the HTTP request.
   * @param responseType The type reference of the expected response.
   * @param <T>          The type of the expected response.
   * @return A Builder instance for configuring the request.
   */
  public static <T> Builder<T> request(String url, TypeReference<T> responseType) {
    return new Builder<>(url, responseType, client);
  }

  /**
   * <p>Initiates a new HTTP request builder with the specified URL and response type.</p>
   *
   * <p>Example: </p>
   * <pre>{@code
   * FluentRequest.request("https://example.com/articles/1", Article.class, client)
   *     .get();
   * }</pre>
   *
   * @param url          The URL for the HTTP request.
   * @param responseType The class type of the expected response.
   * @param client       The OkHttpClient to use for this specific request.
   * @param <T>          The type of the expected response.
   * @return A Builder instance for configuring the request.
   */
  public static <T> Builder<T> request(String url, Class<T> responseType, OkHttpClient client) {
    return new Builder<>(url, responseType, client);
  }

  /**
   * <p>Initiates a new HTTP request builder with the specified URL and response type reference.
   * Allows to use a generic type as a response body type.</p>
   *
   * <p>Example:</p>
   * <pre>{@code
   * FluentRequest.request("https://example.com/articles", new TypeReference<List<Article>>() {}, client)
   *     .get();
   * }</pre>>
   *
   * @param url          The URL for the HTTP request.
   * @param responseType The type reference of the expected response.
   * @param client       The OkHttpClient to use for this specific request.
   * @param <T>          The type of the expected response.
   * @return A Builder instance for configuring the request.
   */
  public static <T> Builder<T> request(String url, TypeReference<T> responseType, OkHttpClient client) {
    return new Builder<>(url, responseType, client);
  }

  /**
   * Overrides the default OkHttpClient used for making HTTP requests.
   *
   * @param newClient The OkHttpClient to use for HTTP requests.
   */
  public static void overrideClient(OkHttpClient newClient) {
    client = newClient;
  }

  /**
   * Overrides the default ObjectMapper used for JSON serialization and deserialization.
   *
   * @param newMapper The ObjectMapper to use for JSON processing.
   */
  public static void overrideMapper(ObjectMapper newMapper) {
    mapper = newMapper;
  }

  /**
   * The Builder class is an inner class of FluentRequest and represents the actual builder
   * for constructing FluentRequest instances with specific configurations.
   *
   * @param <T> The type of the expected response.
   */
  public static class Builder<T> {
    private OkHttpClient client;

    private final String url;
    private final Class<T> responseType;
    private final TypeReference<T> responseReference;

    private final List<FluentPair<String, String>> headers;
    private final Map<String, Object> urlVariables;
    private final Map<String, Object> queryParameters;
    private RequestBody body;

    /**
     * Constructs a new Builder instance with the specified URL, response type, and OkHttpClient.
     *
     * @param url          The URL for the HTTP request.
     * @param responseType The type reference of the expected response.
     * @param client       The OkHttpClient to use for this specific request.
     */
    public Builder(String url, Class<T> responseType, OkHttpClient client) {
      this.client = client;
      this.url = url;

      this.responseType = responseType;
      this.responseReference = null;

      this.urlVariables = new HashMap<>();
      this.queryParameters = new HashMap<>();
      this.headers = new ArrayList<>();
    }

    /**
     * Constructs a new Builder instance with the specified URL, response type reference, and OkHttpClient.
     *
     * @param url          The URL for the HTTP request.
     * @param responseRef  The class type of the expected response.
     * @param client       The OkHttpClient to use for this specific request.
     */
    public Builder(String url, TypeReference<T> responseRef, OkHttpClient client) {
      this.client = client;
      this.url = url;

      this.responseType = null;
      this.responseReference = responseRef;

      this.urlVariables = new HashMap<>();
      this.queryParameters = new HashMap<>();
      this.headers = new ArrayList<>();
    }

    /**
     * Sets the request body for the HTTP request.
     *
     * @param body The request body object.
     * @return The Builder instance for method chaining.
     * @throws FluentMappingException If there is an issue with mapping the body object to JSON.
     */
    public Builder<T> body(Object body) throws FluentMappingException {
      this.body = RequestBody.create(getBodyAsString(body), MediaType.get("application/json"));
      return this;
    }

    /**
     * Sets the request body for the HTTP request using key-value pairs.
     *
     * @param body The map representing the request body.
     * @return The Builder instance for method chaining.
     * @throws FluentMappingException If there is an issue with mapping the body map to JSON.
     */
    public Builder<T> body(Map<String, String> body) throws FluentMappingException {
      this.body = RequestBody.create(getBodyAsString(body), MediaType.get("application/json"));
      return this;
    }

    /**
     * Sets the request body for the HTTP request using a custom RequestBody. Use
     * {@link FluentRequest.Builder#multipart()} to build multipart body and
     * {@link FluentRequest.Builder#form()} to build form body.
     *
     * @param body The custom RequestBody.
     * @return The Builder instance for method chaining.
     * @see FluentRequest.Builder#multipart()
     * @see FluentRequest.Builder#form()
     */
    public Builder<T> body(RequestBody body) {
      this.body = body;
      return this;
    }

    /**
     * Adds a URL variable to the request.
     *
     * @param name  The name of the URL variable.
     * @param value The value of the URL variable.
     * @return The Builder instance for method chaining.
     */
    public Builder<T> variable(String name, Object value) {
      if (Objects.nonNull(value)) {
        this.urlVariables.put(name, String.valueOf(value));
      }

      return this;
    }

    /**
     * Adds multiple URL variables to the request.
     *
     * @param variables The map of URL variables.
     * @return The Builder instance for method chaining.
     */
    public Builder<T> variables(Map<String, Object> variables) {
      this.urlVariables.putAll(variables);
      return this;
    }

    /**
     * Adds a query parameter to the request.
     *
     * @param name  The name of the query parameter.
     * @param value The value of the query parameter.
     * @return The Builder instance for method chaining.
     */
    public Builder<T> parameter(String name, Object value) {
      if (Objects.nonNull(value)) {
        this.queryParameters.put(name, String.valueOf(value));
      }

      return this;
    }

    /**
     * Adds multiple query parameters to the request.
     *
     * @param parameters The map of query parameters.
     * @return The Builder instance for method chaining.
     */
    public Builder<T> parameters(Map<String, Object> parameters) {
      this.queryParameters.putAll(parameters);
      return this;
    }

    /**
     * Adds a header to the request.
     *
     * @param name  The name of the header.
     * @param value The value of the header.
     * @return The Builder instance for method chaining.
     */
    public Builder<T> header(String name, String value) {
      this.headers.add(FluentPair.of(name, value));
      return this;
    }

    public Builder<T> headers(Map<String, String> headers) {
      for (String name : headers.keySet()) {
        this.headers.add(FluentPair.of(name, headers.get(name)));
      }

      return this;
    }

    public Builder<T> headers(List<FluentPair<String, String>> headers) {
      this.headers.addAll(headers);
      return this;
    }

    /**
     * Adds a bearer token to the request for bearer authentication.
     *
     * @param token The bearer token.
     * @return The Builder instance for method chaining.
     */
    public Builder<T> bearer(String token) {
      this.headers.add(FluentPair.of("Authorization", "Bearer " + token));
      return this;
    }

    /**
     * Adds basic authentication to the request.
     *
     * @param name     The username for basic authentication.
     * @param password The password for basic authentication.
     * @return The Builder instance for method chaining.
     */
    public Builder<T> basic(String name, String password) {
      this.headers.add(FluentPair.of("Authorization", Credentials.basic(name, password)));
      return this;
    }

    /**
     * Initiates a multipart form data request.
     *
     * @return A FluentMultipartBody instance for configuring multipart form data.
     * @see FluentMultipartBody
     */
    public FluentMultipartBody<T> multipart() {
      return new FluentMultipartBody<>(this);
    }

    /**
     * Initiates a form-urlencoded request.
     *
     * @return A FluentFormBody instance for configuring form-urlencoded parameters.
     * @see FluentFormBody
     */
    public FluentFormBody<T> form() {
      return new FluentFormBody<>(this);
    }

    /**
     * Send the HTTP request synchronously with a specified method.
     *
     * @param method The HTTP method to be used for the request.
     * @return The FluentResponse containing the response body and HTTP response details.
     */
    public FluentResponse<T> execute(FluentHttpMethod method) {
      return doSend(method);
    }

    /**
     * Send the HTTP request asynchronously with a specified method and a callback.
     *
     * @param method The FluentHttpMethod to execute.
     * @param callback The Callback to be called after the execution.
     */
    public void execute(FluentHttpMethod method, Callback callback) {
      doSend(method, callback);
    }

    /**
     * Sends a GET request synchronously and returns the response.
     *
     * @return The FluentResponse containing the response body and HTTP response details.
     */
    public FluentResponse<T> get() throws FluentIOException {
      return doSend(FluentHttpMethod.GET);
    }

    /**
     * Sends a GET request asynchronously with a callback.
     *
     * @param callback The callback to handle the asynchronous response.
     */
    public void get(Callback callback) {
      doSend(FluentHttpMethod.GET, callback);
    }

    /**
     * Sends a POST request synchronously and returns the response.
     *
     * @return The FluentResponse containing the response body and HTTP response details.
     */
    public FluentResponse<T> post() throws FluentIOException {
      return doSend(FluentHttpMethod.POST);
    }

    /**
     * Sends a POST request asynchronously with a callback.
     *
     * @param callback The callback to handle the asynchronous response.
     */
    public void post(Callback callback) {
      doSend(FluentHttpMethod.POST, callback);
    }

    /**
     * Sends a PUT request synchronously and returns the response.
     *
     * @return The FluentResponse containing the response body and HTTP response details.
     */
    public FluentResponse<T> put() throws FluentIOException {
      return doSend(FluentHttpMethod.PUT);
    }

    /**
     * Sends a PUT request asynchronously with a callback.
     *
     * @param callback The callback to handle the asynchronous response.
     */
    public void put(Callback callback) {
      doSend(FluentHttpMethod.PUT, callback);
    }

    /**
     * Sends a PATCH request synchronously and returns the response.
     *
     * @return The FluentResponse containing the response body and HTTP response details.
     */
    public FluentResponse<T> patch() throws FluentIOException {
      return doSend(FluentHttpMethod.PATCH);
    }

    /**
     * Sends a PATCH request asynchronously with a callback.
     *
     * @param callback The callback to handle the asynchronous response.
     */
    public void patch(Callback callback) {
      doSend(FluentHttpMethod.PATCH, callback);
    }

    /**
     * Sends a HEAD request synchronously and returns the response.
     *
     * @return The FluentResponse containing the response body and HTTP response details.
     */
    public FluentResponse<T> head() throws FluentIOException {
      return doSend(FluentHttpMethod.HEAD);
    }

    /**
     * Sends a HEAD request asynchronously with a callback.
     *
     * @param callback The callback to handle the asynchronous response.
     */
    public void head(Callback callback) {
      doSend(FluentHttpMethod.HEAD, callback);
    }

    /**
     * Sends an OPTIONS request synchronously and returns the response.
     *
     * @return The FluentResponse containing the response body and HTTP response details.
     */
    public FluentResponse<T> options() throws FluentIOException {
      return doSend(FluentHttpMethod.OPTIONS);
    }

    /**
     * Sends an OPTIONS request asynchronously with a callback.
     *
     * @param callback The callback to handle the asynchronous response.
     */
    public void options(Callback callback) {
      doSend(FluentHttpMethod.OPTIONS, callback);
    }

    /**
     * Sends a TRACE request synchronously and returns the response.
     *
     * @return The FluentResponse containing the response body and HTTP response details.
     */
    public FluentResponse<T> trace() throws FluentIOException {
      return doSend(FluentHttpMethod.TRACE);
    }

    /**
     * Sends a TRACE request asynchronously with a callback.
     *
     * @param callback The callback to handle the asynchronous response.
     */
    public void trace(Callback callback) {
      doSend(FluentHttpMethod.TRACE, callback);
    }

    /**
     * Sends a DELETE request synchronously.
     */
    public FluentResponse<T> delete() throws FluentIOException {
      return doSend(FluentHttpMethod.DELETE);
    }

    /**
     * Sends a DELETE request asynchronously with a callback.
     *
     * @param callback The callback to handle the asynchronous response.
     */
    public void delete(Callback callback) {
      doSend(FluentHttpMethod.DELETE, callback);
    }

    /**
     * Sends the HTTP request asynchronously with the specified method and callback.
     *
     * @param method   The HTTP method for the request.
     * @param callback The callback to handle the asynchronous response.
     */
    private void doSend(FluentHttpMethod method, Callback callback) {
      client.newCall(buildRequest(method)).enqueue(callback);
    }

    /**
     * Sends the HTTP request synchronously with the specified method and returns the response.
     *
     * @param method The HTTP method for the request.
     * @return The FluentResponse containing the response body and HTTP response details.
     */
    private FluentResponse<T> doSend(FluentHttpMethod method) throws FluentIOException {
      Request request = buildRequest(method);

      try (Response response = client.newCall(request).execute()) {
        return new FluentResponse<>(deserializeAsJson(response), response);
      } catch (IOException e) {
        throw new FluentIOException(e);
      }
    }

    /**
     * Builds the OkHttp Request object based on the configured parameters.
     *
     * @param method The HTTP method for the request.
     * @return The constructed OkHttp Request object.
     */
    private Request buildRequest(FluentHttpMethod method) {
      return new Request.Builder()
          .url(buildUrl(url, urlVariables, queryParameters))
          .headers(buildHeaders())
          .method(method.name(), buildBody())
          .build();
    }

    /**
     * Retrieves the JSON body from the provided response object. 
     *
     * @param response the response object from which to retrieve the JSON body
     * @return the deserialized JSON body as an object of type T
     * @throws IOException if an I/O error occurs during the retrieval or deserialization of the JSON body
     */
    private T deserializeAsJson(Response response) throws IOException {
      if (Objects.isNull(response.body())) {
        return null;
      }

      if (byte[].class.equals(responseType)) {
        return (T) response.body().bytes();
      }

      if (String.class.equals(responseType)) {
        return (T) response.body().string();
      }

      if (Objects.nonNull(responseType)) {
        return mapper.readValue(response.body().bytes(), this.responseType);
      } else {
        return mapper.readValue(response.body().bytes(), this.responseReference);
      }
    }

    /**
     * Builds the request body based on the configured parameters.
     *
     * @return The constructed RequestBody or null if no body is specified.
     */
    private RequestBody buildBody() {
      if (Objects.nonNull(this.body)) {
        return this.body;
      } else {
        return null;
      }
    }

    /**
     * Converts the request body object to a JSON string.
     *
     * @param body The request body object.
     * @return The JSON representation of the request body.
     * @throws FluentMappingException If there is an issue with mapping the body object to JSON.
     */
    private String getBodyAsString(Object body) throws FluentMappingException {
      try {
        return mapper.writeValueAsString(body);
      } catch (JsonProcessingException e) {
        throw new FluentMappingException(e);
      }
    }

    /**
     * Builds the final URL by combining the base URL, URL variables, and query parameters.
     *
     * @param url             The base URL.
     * @param urlVariables    The map of URL variables.
     * @param queryParameters The map of query parameters.
     * @return The final URL with variables and parameters.
     */
    private String buildUrl(String url,
                            Map<String, Object> urlVariables,
                            Map<String, Object> queryParameters) {
      return FluentUrl.fromString(url)
          .variables(urlVariables)
          .parameters(queryParameters)
          .build();
    }

    /**
     * Builds the headers for the request.
     *
     * @return the built Headers object
     */
    private Headers buildHeaders() {
      if (this.headers.isEmpty()) {
        return Headers.of(Collections.emptyMap());
      }

      List<String> headerNameAndValues = new ArrayList<>();

      this.headers.forEach(header -> {
        headerNameAndValues.add(header.getKey());
        headerNameAndValues.add(header.getValue());
      });

      return Headers.of(headerNameAndValues.toArray(new String[0]));
    }
  }
}
