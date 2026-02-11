package edu.ucf.epoch.epochpatches.mixinsupport;

import edu.ucf.epoch.epochpatches.util.documentation.UsedViaReflection;
import net.minecraft.world.level.LevelAccessor;

@UsedViaReflection
public interface MCreatorSavedDataDuck {
	void epoch_syncData(LevelAccessor world);
}
