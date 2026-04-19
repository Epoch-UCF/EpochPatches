package edu.ucf.epoch.epochpatches.mixin;

import java.lang.invoke.MethodHandles;

/**
 * The JVM won't let you define classes in a package without being in that same package.
 *
 * This just provides a lookup so we can define classes here.
 */
public final class MixinPackageAccess {
	public static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
	
	private MixinPackageAccess() {}
}
