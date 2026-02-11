package edu.ucf.epoch.epochpatches.mixin.client.m2.spaceploitation;

import com.bawnorton.mixinsquared.TargetHandler;
import edu.ucf.epoch.epochpatches.mixinsupport.MixinData;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Black hole shader causes crash on Apple Silicon, so we disable it here.
 */
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
	private void disableOnAppleSilicon(CallbackInfo ci) {
		if (MixinData.IS_APPLE_SILICON)
			ci.cancel();
	}
}
