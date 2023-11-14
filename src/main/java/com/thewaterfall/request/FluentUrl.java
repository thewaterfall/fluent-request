package com.thewaterfall.request;

import com.thewaterfall.request.misc.FluentUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>The FluentUrl class provides a builder pattern for constructing URLs with variables and query parameters.
 * It is designed to enhance the flexibility of creating dynamic URLs for use in HTTP requests.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code FluentUrl.fromString("https://api.example.com/endpoint/{id}")
 *     .variable("id", "1")
 *     .parameter("param1", "value1")
 *     .parameter("param2", "value2")
 *     .build();}</pre>
 */
public class FluentUrl {
  private static final Pattern QUERY_PARAM_PATTERN = Pattern.compile("([?&])([^=]+)=([^&]+)");

  private final String url;
  private final Map<String, Object> urlVariables = new HashMap<>();
  private final Map<String, Object> queryParameters = new HashMap<>();

  private FluentUrl(String url) {
    this.url = url;
  }

  private FluentUrl(String url, Map<String, String> queryParams) {
    this.url = url;
    this.queryParameters.putAll(queryParams);
  }

  /**
   * Creates a FluentUrl instance with the provided base URL.
   *
   * @param url The base URL to start building the dynamic URL.
   * @return A FluentUrl instance for constructing URLs with variables and query parameters.
   */
  public static FluentUrl fromString(String url) {
    return new FluentUrl(url, extractQueryParams(url));
  }

  /**
   * Adds a variable to the URL. Variable example: {@code ../posts/{postId}}.
   *
   * @param key   The key of the URL variable.
   * @param value The value of the URL variable.
   * @return The FluentUrl instance for method chaining.
   */
  public FluentUrl variable(String key, Object value) {
    this.urlVariables.put(key, value);
    return this;
  }

  /**
   * Adds multiple variables to the URL. Variable example: {@code ../posts/{postId}}.
   *
   * @param urlVariables A Map containing key-value pairs representing URL variables.
   * @return The FluentUrl instance for method chaining.
   */
  public FluentUrl variables(Map<String, Object> urlVariables) {
    this.urlVariables.putAll(urlVariables);
    return this;
  }

  /**
   * Adds a query parameter to the URL.
   *
   * @param key   The key of the query parameter.
   * @param value The value of the query parameter.
   * @return The FluentUrl instance for method chaining.
   */
  public FluentUrl parameter(String key, Object value) {
    this.queryParameters.put(key, value);
    return this;
  }

  /**
   * Adds multiple query parameters to the URL.
   *
   * @param queryParameters A Map containing key-value pairs representing query parameters.
   * @return The FluentUrl instance for method chaining.
   */
  public FluentUrl parameters(Map<String, Object> queryParameters) {
    this.queryParameters.putAll(queryParameters);
    return this;
  }

  /**
   * Builds the final URL with variables and query parameters.
   *
   * @return The formatted URL with variables and query parameters.
   */
  public String build() {
    return buildParameters(buildVariables(url, urlVariables), queryParameters);
  }

  /**
   * Builds a URI from the final URL with variables and query parameters.
   *
   * @return The URI representation of the final URL.
   */
  public URI buildUri() {
    return URI.create(build());
  }

  /**
   * Builds the URL parameters from the given URL and query parameters.
   *
   * @param url            The base URL.
   * @param queryParameters The map of query parameters.
   * @return The formatted URL parameters.
   */
  private String buildParameters(String url, Map<String, Object> queryParameters) {
    StringBuilder parameters = new StringBuilder();

    for (Map.Entry<String, Object> entry : queryParameters.entrySet()) {
      if (parameters.length() != 0) {
        parameters.append("&");
      }

      parameters.append(entry.getKey()).append("=").append(entry.getValue());
    }

    if (parameters.length() != 0) {
      return url + "?" + parameters;
    } else {
      return url;
    }
  }

  /**
   * Builds the URL with variables replaced by their corresponding values.
   *
   * @param url        The base URL.
   * @param urlVariables The map of URL variables.
   * @return The formatted URL with variables replaced by their values.
   */
  private String buildVariables(String url, Map<String, Object> urlVariables) {
    String formattedUrl = url;

    for (Map.Entry<String, Object> entry : urlVariables.entrySet()) {
      String variable = "{" + entry.getKey() + "}";
      Object value = entry.getValue();

      formattedUrl = formattedUrl.replace(variable, String.valueOf(value));
    }
    return formattedUrl;
  }

  /**
   * Extracts all query parameters from the given URL and returns them as a map.
   *
   * @param url The URL to extract the query parameters from.
   * @return A map of all query parameters in the URL.
   */
  private static Map<String, String> extractQueryParams(String url) {
    Matcher matcher = QUERY_PARAM_PATTERN.matcher(url);
    Map<String, String> queryParams = new HashMap<>();

    while (matcher.find()) {
      String key = matcher.group(2);
      String value = matcher.group(3);

      if (FluentUtils.isNotEmptyString(key) && FluentUtils.isNotEmptyString(value)) {
        queryParams.put(key, value);
      }
    }

    return queryParams;
  }
}
