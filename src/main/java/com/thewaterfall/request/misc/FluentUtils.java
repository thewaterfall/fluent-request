package com.thewaterfall.request.misc;

/**
 * Utility class providing common functionalities.
 */
public class FluentUtils {
  /**
   * Checks if the given string is empty or null.
   *
   * @param string The string to check.
   * @return true if the string is empty or null, false otherwise.
   */
  public static boolean isEmptyString(String string) {
    return string == null || string.isEmpty();
  }

  /**
   * Checks if the given string is not empty and not null.
   *
   * @param string The string to check.
   * @return true if the string is empty or null, false otherwise.
   */
  public static boolean isNotEmptyString(String string) {
    return !isEmptyString(string);
  }
}
