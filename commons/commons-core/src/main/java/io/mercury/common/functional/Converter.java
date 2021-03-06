package io.mercury.common.functional;

import java.util.function.BiFunction;

import javax.annotation.Nonnull;

/**
 * 
 * @author yellow013
 *
 * @param <F>
 * @param <T>
 */
@FunctionalInterface
public interface Converter<F, T> extends BiFunction<F, T, T> {

	/**
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	@Nonnull
	T convert(@Nonnull F from, @Nonnull T to);

	@Override
	default T apply(F from, T to) {
		return convert(from, to);
	}

}
