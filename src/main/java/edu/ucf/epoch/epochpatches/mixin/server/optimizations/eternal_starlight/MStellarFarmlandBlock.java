package edu.ucf.epoch.epochpatches.mixin.server.optimizations.eternal_starlight;

import edu.ucf.epoch.epochpatches.mixinsupport.IFirmamentExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phanastrae.operation_starcleave.block.StellarFarmlandBlock;
import phanastrae.operation_starcleave.world.firmament.Firmament;

@Mixin(value = StellarFarmlandBlock.class, remap = false)
abstract class MStellarFarmlandBlock {
	@Inject(
			method = "isStarlit",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void fuckitnoitaint(LevelReader worldView, BlockPos pos, Firmament firmament, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(Boolean.FALSE);
	}
	
	@Redirect(
			method = "isStarlit",
			at = @At(
					target = "Lphanastrae/operation_starcleave/world/firmament/Firmament;getDamage(II)I",
					value = "INVOKE"
			)
	)
	private static int epoch$checkCache(Firmament instance, int x, int z) {
		return ((IFirmamentExtensions) instance).epoch$getCachedDamage(x, z);
	}
}
