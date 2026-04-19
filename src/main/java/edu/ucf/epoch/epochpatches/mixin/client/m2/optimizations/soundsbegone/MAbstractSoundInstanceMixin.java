package edu.ucf.epoch.epochpatches.mixin.client.m2.optimizations.soundsbegone;

import com.bawnorton.mixinsquared.TargetHandler;
import edu.ucf.epoch.epochpatches.util.documentation.NoOp;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractSoundInstance.class)
public class MAbstractSoundInstanceMixin {
	@TargetHandler(
			mixin="gg.meza.client.mixin.AbstractSoundInstanceMixin",
			name="getVolume"
	)
	@Redirect(
			method="@MixinSquared:Handler",
			at=@At(
					target="Lorg/slf4j/Logger;debug(Ljava/lang/String;Ljava/lang/Object;)V",
					value="INVOKE"
			)
	)
	@NoOp private void dontLogConstantly(Logger logger, String message, Object object) {}
}
