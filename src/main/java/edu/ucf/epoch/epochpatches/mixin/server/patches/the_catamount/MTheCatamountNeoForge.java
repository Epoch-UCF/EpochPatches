package edu.ucf.epoch.epochpatches.mixin.server.patches.the_catamount;

import dev.sterner.the_catamount.neoforge.TheCatamountNeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TheCatamountNeoForge.GameEvents.class, remap = false)
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
