package edu.ucf.epoch.epochpatches.mixin.client.m2.spaceploitation;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"InvalidMemberReference", "MixinAnnotationTarget"})
@Mixin(LevelRenderer.class)
public class MBlackHoleShader {
	@TargetHandler(
			mixin="com.portingdeadmods.spaceploitation.mixin.LevelRendererMixin",
			name = "tryProcessBlackHoleShader"
	)
	@Inject(
		method = "@MixinSquared:Handler",
		at=@At("HEAD"),
		cancellable = true
	)
	private void inject(CallbackInfo ci) {
		ci.cancel();
	}
}
