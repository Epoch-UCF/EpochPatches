package edu.ucf.epoch.epochpatches.mixin.server.m2.patches.hardcore_revival;

import com.llamalad7.mixinextras.sugar.Local;
import net.blay09.mods.hardcorerevival.MixinHooks;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MixinHooks.class)
public abstract class MMixinHooks {
	
	/**
	 * Schedule the "move player" task on the main thread instead of doing it on the netty thread,
	 * so it doesn't block for the server thread and deadlock.
	 */
	@Redirect(
			method = "handleProcessPlayerRotation",
			at = @At(
					target = "Lnet/minecraft/server/level/ServerPlayer;absMoveTo(DDDFF)V",
					value = "INVOKE"
			)
	)
	private static void noDeadlock(ServerPlayer player, double x, double y, double z, float yaw, float pitch) {
		player.getServer().execute(() -> player.absMoveTo(x, y, z, yaw, pitch));
	}
}

