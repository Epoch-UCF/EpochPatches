package edu.ucf.epoch.epochpatches.mixin.server.patches.the_catamount;

import edu.ucf.epoch.epochpatches.util.documentation.DisabledMixin;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings({"UnresolvedMixinReference", "UnusedMixin"})
@Mixin(targets = "dev.sterner.the_catamount.neoforge.TheCatamountNeoForge$GameEvents", remap = false) @Pseudo
@DisabledMixin(reason="No longer included in the modpack.")
abstract class MTheCatamountNeoForge {
	@Redirect(
			method="onLivingHurt",
			at=@At(
					target="Lnet/neoforged/neoforge/event/entity/living/LivingDamageEvent$Pre;getOriginalDamage()F",
					value="INVOKE"
			)
	)
	private static float useNormalDamageInsteadOfOriginal(Pre instance) {
		return instance.getNewDamage();
	}
}
