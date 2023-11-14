package com.thewaterfall.request.misc;

/**
 * The FluentPair class represents a simple key-value pair.
 *
 * @param <K> The type of the key.
 * @param <V> The type of the value.
 */
public class FluentPair<K, V> {
  private final K key;
  private final V value;

  /**
   * Constructs a FluentPair with the specified key and value.
   *
   * @param key   The key of the pair.
   * @param value The value of the pair.
   */
  public FluentPair(K key, V value) {
    this.key = key;
    this.value = value;
  }

  /**
   * Creates a new FluentPair instance with the specified key and value.
   *
   * @param <K>   The type of the key.
   * @param <V>   The type of the value.
   * @param key   The key of the pair.
   * @param value The value of the pair.
   * @return A new FluentPair instance with the provided key and value.
   */
  public static <K, V> FluentPair<K, V> of(K key, V value) {
    return new FluentPair<>(key, value);
  }

  /**
   * Gets the key of the pair.
   *
   * @return The key of the pair.
   */
  public K getKey() {
    return key;
  }

  /**
   * Gets the value of the pair.
   *
   * @return The value of the pair.
   */
  public V getValue() {
    return value;
  }
}
