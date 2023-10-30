package mki.math;

/**
 * A functional interface which takes in three arguments of any 
 * type and performs an arbitrary operation with them, returning nothing.
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <V> the type of the third argument to the operation
 * 
 * @author William Kilty
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param t the first input argument
   * @param u the second input argument
   * @param v the third input argument
   */
  void accept(T t, U u, V v);
}
