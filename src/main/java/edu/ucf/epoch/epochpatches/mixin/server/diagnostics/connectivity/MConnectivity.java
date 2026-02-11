//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package edu.ucf.epoch.epochpatches.mixin.server.diagnostics.connectivity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Make `connectivity packetsSummary` use the *fully-qualified* class name for the packet
 * instead of either just the class name or the "packet name"
 */
@Mixin(targets="com.connectivity.networkstats.NetworkStatGatherer", remap = false) @Pseudo
abstract class MConnectivity {
	/**
	 * Use fully-qualified class name
	 */
	@Redirect(
			method = "add",
			at = @At(
					target = "Ljava/lang/Class;getSimpleName()Ljava/lang/String;",
					value = "INVOKE"
			)
	)
	private static String recordFullName(Class<?> instance) {
		return instance.getName();
	}
	
	/**
	 * Don't allow it to use the user-defined name of the packet
	 */
	@Redirect(
			method = "add",
			at = @At(
					target="Ljava/lang/String;isEmpty()Z",
					value="INVOKE"
			)
	)
	private static boolean noUseNamedPacketName(String instance) {
		return true;
	}
}
