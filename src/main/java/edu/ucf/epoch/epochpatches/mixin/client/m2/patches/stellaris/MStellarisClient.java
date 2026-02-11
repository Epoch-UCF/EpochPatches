package edu.ucf.epoch.epochpatches.mixin.client.m2.patches.stellaris;

import edu.ucf.epoch.epochpatches.util.documentation.NoOp;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets="com.st0x0ef.stellaris.client.StellarisClient") @Pseudo
public abstract class MStellarisClient {
	@Redirect(
			method = "initClient",
			at = @At(
					target = "Lnet/minecraft/client/Minecraft;execute(Ljava/lang/Runnable;)V",
					value = "INVOKE"
			)
	) @NoOp
	private static void dontDoLogging(Minecraft instance, Runnable runnable) {}
}
