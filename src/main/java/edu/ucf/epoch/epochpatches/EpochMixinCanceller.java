package edu.ucf.epoch.epochpatches;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class EpochMixinCanceller implements MixinCanceller {
	@Override
	public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
		return mixinClassName.contains("mcreator") && targetClassNames.contains("net.minecraft.world.level.levelgen.NoiseGeneratorSettings");
	}
}
