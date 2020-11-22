package io.mercury.common.concurrent.queue.api;

import java.util.function.Predicate;

import javax.annotation.CheckForNull;

public interface Queue<E> {

	boolean enqueue(E e);

	@CheckForNull
	default E poll() {
		throw new UnsupportedOperationException("Interface [Queue] defined method [poll] is not implement");
	}

	default boolean pollAndApply(PollFunction<E> function) {
		throw new UnsupportedOperationException("Interface [Queue] defined method [pollAndApply] is not implement");
	}

	String queueName();

	public static enum WaitingStrategy {
		SpinWaiting, SleepWaiting, Blocking
	}

	@FunctionalInterface
	interface PollFunction<E> extends Predicate<E> {

		boolean apply(E e);

		@Override
		default boolean test(E e) {
			return apply(e);
		}

	}

}