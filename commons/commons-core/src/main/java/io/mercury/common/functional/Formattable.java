package io.mercury.common.functional;

import java.util.function.Supplier;

/**
 * 
 * @author yellow013
 *
 * @param <T>
 */
@FunctionalInterface
public interface Formattable<T> extends Supplier<T> {

	T format();

	@Override
	default T get() {
		return format();
	}

}
