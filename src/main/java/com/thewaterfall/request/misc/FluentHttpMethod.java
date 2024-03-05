package com.thewaterfall.request.misc;

/**
 * The FluentHttpMethod enum represents the HTTP methods.
 */
public enum FluentHttpMethod {
  GET,
  HEAD,
  POST,
  PUT,
  PATCH,
  DELETE,
  OPTIONS,
  TRACE;

  public static FluentHttpMethod of(String method) {
    if (method == null) {
      throw new NullPointerException("Method is null");
    }

    for (FluentHttpMethod b : FluentHttpMethod.values()) {
      if (method.equalsIgnoreCase(b.toString())) {
        return b;
      }
    }

    throw new IllegalArgumentException("Invalid method: " + method);
  }
}
