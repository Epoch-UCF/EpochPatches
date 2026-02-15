package edu.ucf.epoch.epochpatches.mixin.server.disabling.shineals_prehistoric;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Stop infinite biome_rules recursion
 */
@Mixin(targets = "net.mcreator.shinealsprehistoricexpansion.init.ShinealsPrehistoricExpansionModBiomes", remap = false)
public abstract class MModBiomes {
	@Inject(
			method="onServerAboutToStart",
			at=@At("HEAD"),
			cancellable = true
	)
	private static void disable(CallbackInfo ci) {
		ci.cancel();
	}
}

