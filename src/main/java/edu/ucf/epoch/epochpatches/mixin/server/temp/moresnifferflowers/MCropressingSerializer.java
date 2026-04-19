package edu.ucf.epoch.epochpatches.mixin.server.temp.moresnifferflowers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import edu.ucf.epoch.epochpatches.EpochPatchesMod;
import edu.ucf.epoch.epochpatches.util.NameGetUtils;
import net.abraxator.moresnifferflowers.recipes.CropressingRecipe;
import net.abraxator.moresnifferflowers.recipes.serializers.CropressingSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Attempt to find the name of the bugged-out recipe
 */
@Mixin(value = CropressingSerializer.class, remap = false)
abstract class MCropressingSerializer {
	@WrapOperation(
			method = "toNetwork",
			at = @At(
					target = "Lnet/minecraft/network/codec/StreamCodec;encode(Ljava/lang/Object;Ljava/lang/Object;)V",
					value = "INVOKE"
			)
	)
	private void foo(StreamCodec<?, ?> instance, Object o, Object o2, Operation<Void> original,
	                 RegistryFriendlyByteBuf buf, CropressingRecipe recipe
	) {
		try {
			original.call(instance, o, o2);
		} catch (Exception e) {
			EpochPatchesMod.LOGGER.error("[EPOCH DEBUG] Error serializing recipe: {}", NameGetUtils.getNameForRecipe(recipe));
			throw e;
		}
	}
}

