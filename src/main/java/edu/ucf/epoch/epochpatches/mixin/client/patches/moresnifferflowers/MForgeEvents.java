package edu.ucf.epoch.epochpatches.mixin.client.patches.moresnifferflowers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.abraxator.moresnifferflowers.events.ForgeEvents;
import net.minecraft.core.Holder;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ForgeEvents.class)
abstract class MForgeEvents {
	@WrapOperation(
			method = "onEffectAdded",
			at = @At(
					target = "Lnet/minecraft/core/Holder;equals(Ljava/lang/Object;)Z",
					value = "INVOKE"
			)
	)
	private static boolean onlySendOnServer(Holder<?> instance, Object o, Operation<Boolean> original,
	                                       MobEffectEvent.Added evt)
	{
		return !evt.getEntity().level().isClientSide() && original.call(instance, o);
	}
}

