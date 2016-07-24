package edu.hm.cs.vadere.seating.datacollection;

/**
 * This is a Java 8 functional interface. In future, it can be replaced with java.util.function.Consumer
 */
public interface Consumer<T> {
    void accept(T arg);
}
