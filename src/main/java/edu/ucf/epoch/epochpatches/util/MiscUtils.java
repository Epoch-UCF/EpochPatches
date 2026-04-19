package edu.ucf.epoch.epochpatches.util;

import java.util.Optional;

public final class MiscUtils {
	/**
	 * Returns an empty Optional if `condition` is false, else returns an optional containing `value`.
	 */
	public static <T> Optional<T> optWithCond(boolean condition, T value) {
		return Optional.ofNullable(condition ? value : null);
	}
}
