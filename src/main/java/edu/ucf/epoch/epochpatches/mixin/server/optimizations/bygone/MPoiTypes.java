package edu.ucf.epoch.epochpatches.mixin.server.optimizations.bygone;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import edu.ucf.epoch.epochpatches.classdeferrals.CBygone;
import edu.ucf.epoch.epochpatches.registry.EpochPoiTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Mixin(PoiTypes.class)
abstract class MPoiTypes {
	/**
	 * Add PoiTypes that use tags, since you can't do that normally
	 */
	@ModifyReturnValue(
			method = "forState",
			at = @At("RETURN")
	)
	private static Optional<Holder<PoiType>> addTagOnlyPoiTypes(Optional<Holder<PoiType>> original, BlockState state) {
		if (EpochPoiTypes.SABEAST_REPELLANT == null)
			return original; // JITs into disabling the mixin
		return CBygone.operation(original, state); // defers body resolution so it doesn't try to load a class that doesn't exist
	}
}