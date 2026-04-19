package edu.ucf.epoch.epochpatches.mixin.server.patches.moresnifferflowers;

import net.abraxator.moresnifferflowers.blockentities.CropressorBlockEntity;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CropressorBlockEntity.class)
abstract class MCropressorEntity {
	@Shadow public ItemStack currentCrop;
	
	@Inject(
			method = "saveAdditional",
			at = @At("HEAD")
	)
	private void foo(CompoundTag tag, Provider reg, CallbackInfo ci) {
		this.currentCrop.limitSize(64);
	}
}

