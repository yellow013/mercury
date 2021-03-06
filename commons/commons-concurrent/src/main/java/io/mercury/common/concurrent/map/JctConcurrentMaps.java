package io.mercury.common.concurrent.map;

import java.util.concurrent.ConcurrentMap;

import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashMapLong;

public final class JctConcurrentMaps {

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> ConcurrentMap<K, V> newNonBlockingHashMap() {
		return new NonBlockingHashMap<>();
	}

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param initCapacity
	 * @return
	 */
	public static <K, V> ConcurrentMap<K, V> newNonBlockingHashMap(final int initCapacity) {
		return new NonBlockingHashMap<>(initCapacity);
	}

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public static <V> ConcurrentMap<Long, V> newNonBlockingLongMap() {
		return new NonBlockingHashMapLong<>();
	}

	/**
	 * 
	 * @param <V>
	 * @param capacity
	 * @return
	 */
	public static <V> ConcurrentMap<Long, V> newNonBlockingLongMap(final int initCapacity) {
		return new NonBlockingHashMapLong<>(initCapacity);
	}

	/**
	 * 
	 * @param <V>
	 * @param spaceOptimization
	 * @return
	 */
	public static <V> ConcurrentMap<Long, V> newNonBlockingLongMap(final boolean spaceOptimization) {
		return new NonBlockingHashMapLong<>(spaceOptimization);
	}

	/**
	 * 
	 * @param <V>
	 * @param initCapacity
	 * @param spaceOptimization
	 * @return
	 */
	public static <V> ConcurrentMap<Long, V> newNonBlockingLongMap(final int initCapacity,
			final boolean spaceOptimization) {
		return new NonBlockingHashMapLong<>(initCapacity, spaceOptimization);
	}

}
