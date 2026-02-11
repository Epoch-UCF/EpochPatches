package edu.ucf.epoch.epochpatches.mixin.server.optimizations.eternal_starlight;

import edu.ucf.epoch.epochpatches.mixinsupport.IFirmamentExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnresolvedMixinReference")
@Mixin(targets="phanastrae.operation_starcleave.block.StellarFarmlandBlock", remap = false) @Pseudo
abstract class MStellarFarmlandBlock {
	/** Just disable spawns entirely for now cause it's taking too much server time even with the cache */
	@Inject(
			method = "isStarlit",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void fuckitnoitaint(LevelReader worldView, BlockPos pos, @Coerce Object firmament, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(Boolean.FALSE);
	}
	
	@Redirect(
			method = "isStarlit",
			at = @At(
					target = "Lphanastrae/operation_starcleave/world/firmament/Firmament;getDamage(II)I",
					value = "INVOKE"
			)
	)
	private static int epoch$checkCache(@Coerce Object instance, int x, int z) {
		return ((IFirmamentExtensions) instance).epoch$getCachedDamage(x, z);
	}
}
