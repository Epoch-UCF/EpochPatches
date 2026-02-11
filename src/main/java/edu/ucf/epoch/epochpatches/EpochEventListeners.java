package edu.ucf.epoch.epochpatches;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public final class EpochEventListeners {
	@SubscribeEvent
	public static void onServerTick(ServerTickEvent.Post evt) {
		EpochPatchesMod.scheduler.runTasks();
	}
	
	
	private EpochEventListeners(){}
}
