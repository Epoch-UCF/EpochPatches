package edu.ucf.epoch.epochpatches.mixin.server.m2.patches.vanishmod;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class MServerCommonPacketListenerImpl {
	@TargetHandler(
			mixin = "redstonedubstep.mods.vanishmod.mixin.ServerCommonPacketListenerImplMixin",
			name="vanishmod$onSendPacket*"
	)
	@Redirect(
			method = "@MixinSquared:Handler",
			at = @At(
					target = "Lnet/neoforged/neoforge/common/ModConfigSpec$ConfigValue;get()Ljava/lang/Object;",
					value = "INVOKE"
			)
	)
	private Object wrapWithTryCatch(ModConfigSpec.ConfigValue<?> inst) {
		if (!(inst instanceof BooleanValue bv)) {
			return inst.get();
		}
		
		try {
			return bv.get();
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}
}

