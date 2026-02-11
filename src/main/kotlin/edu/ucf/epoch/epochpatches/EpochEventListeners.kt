package edu.ucf.epoch.epochpatches

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.tick.ServerTickEvent

object EpochEventListeners {
	@Suppress("unused")
	@SubscribeEvent
	fun onServerTick(evt: ServerTickEvent.Post) {
		EpochPatchesMod.scheduler.poll()
	}
}