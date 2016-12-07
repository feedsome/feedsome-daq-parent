package com.feedsome.daq.test.route.component;

import java.util.function.Supplier;

/**
 * Marker interface that defines behaviour for components, capable of generating instances
 * of a specified type.
 * @param <T> the class type of the generated instance
 */
@FunctionalInterface
public interface Generator<T> extends Supplier<T> {
}
