package edu.ucf.epoch.epochpatches.mixin.client.m2.patches.stellaris;

import com.st0x0ef.stellaris.client.StellarisClient;
import edu.ucf.epoch.epochpatches.util.documentation.NoOp;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StellarisClient.class)
public abstract class MStellarisClient {
	public MStellarisClient() {
		super();// 14
	}
	
	@Redirect(
			method = {"initClient"},
			at = @At(
					target = "Lnet/minecraft/client/Minecraft;execute(Ljava/lang/Runnable;)V",
					value = "INVOKE"
			)
	) @NoOp
	private static void dontDoLogging(Minecraft instance, Runnable runnable) {}
}
