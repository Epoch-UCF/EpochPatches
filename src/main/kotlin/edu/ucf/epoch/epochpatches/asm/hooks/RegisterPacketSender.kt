package edu.ucf.epoch.epochpatches.asm.hooks

import edu.ucf.epoch.epochpatches.EpochPatchesMod
import edu.ucf.epoch.epochpatches.impl.mcreatorshit.WorldDataPacketSender
import edu.ucf.epoch.epochpatches.impl.mcreatorshit.WorldDataSync
import edu.ucf.epoch.epochpatches.util.documentation.UsedViaReflection
import net.minecraft.server.TickTask
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.saveddata.SavedData
import net.neoforged.fml.util.thread.SidedThreadGroups

object RegisterPacketSender {
	@UsedViaReflection
	@JvmStatic
	fun schedulePacketInsteadOfSyncingData(data: SavedData, world: LevelAccessor) {
		data.setDirty()
		if (world is ServerLevel)
			WorldDataPacketSender.schedule(data.javaClass, world.dimension())
	}
	
	@UsedViaReflection
	@JvmStatic
	fun checkIfInWorldDataPacketSender(clazz: Class<*>, world: LevelAccessor, dataKey: String) {
		if (world !is ServerLevel)
			return;
		
		if (WorldDataPacketSender.isRegistered(clazz, world.dimension()))
			return;
		
		WorldDataPacketSender.registerWorldData(clazz, world.dimension(), WorldDataSync(clazz.asSubclass(SavedData::class.java), dataKey))
	}
	
	@UsedViaReflection
	@JvmStatic
	fun useEpochScheduler(tick: Int, task: Runnable) {
		if (Thread.currentThread().threadGroup === SidedThreadGroups.SERVER) {
			EpochPatchesMod.scheduler.addTask(TickTask(tick, task))
		}
	}
}