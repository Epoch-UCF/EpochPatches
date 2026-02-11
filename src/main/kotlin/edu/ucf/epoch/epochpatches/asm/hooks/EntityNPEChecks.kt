package edu.ucf.epoch.epochpatches.asm.hooks

import edu.ucf.epoch.epochpatches.util.documentation.UsedViaReflection
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level

object EntityNPEChecks {
	/**
	 * Prevent a NPE by passing along the null to the next catcher method
	 */
	@JvmStatic @UsedViaReflection
	fun levelOrNull(entity: Entity?): Level? {
		return entity?.level()
	}
	
	@JvmStatic @UsedViaReflection
	fun isClientSide_nullTrue(level: Level?): Boolean {
		return level?.isClientSide ?: true
	}
}