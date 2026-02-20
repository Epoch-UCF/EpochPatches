package edu.ucf.epoch.epochpatches.mixin.server.optimizations.clockware;

import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(targets="mors.clockware.item.ClockwareItem", remap=false) @Pseudo
abstract class MClockwareItem {
	@Redirect(
			method = {"getDefaultInstance", "ensureHasUUID"},
			at=@At(
					target="java/util/UUID.randomUUID()Ljava/util/UUID;",
					value="INVOKE"
			)
	)
	private static UUID useFasterRNG() {
		return Mth.createInsecureUUID();
	}
}
