package edu.ucf.epoch.epochpatches;

import com.mojang.logging.LogUtils;
import edu.ucf.epoch.epochpatches.commands.EpochCommands;
import edu.ucf.epoch.epochpatches.impl.mcreatorshit.WorldDataPacketSender;
import edu.ucf.epoch.epochpatches.util.scheduler.EpochScheduler;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(EpochPatchesMod.MODID)
public class EpochPatchesMod {
	public static final String MODID = "epochpatches";
	public static final Logger LOGGER = LogUtils.getLogger();
	
	public static EpochScheduler scheduler;
	
	public static MinecraftServer server;
	
	// The constructor for the mod class is the first code that is run when your mod is loaded.
	// FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
	public EpochPatchesMod() {
		// Register the commonSetup method for modloading
		NeoForge.EVENT_BUS.addListener(this::onServerStarting);
		NeoForge.EVENT_BUS.addListener(this::registerCommands);
		NeoForge.EVENT_BUS.register(EpochEventListeners.class);
	}
	
	private void onServerStarting(final ServerStartingEvent event) {
		server = event.getServer();
		scheduler = new EpochScheduler(server::getTickCount);
		scheduler.scheduleRepeating(40, WorldDataPacketSender.INSTANCE);
	}
	
	private void registerCommands(final RegisterCommandsEvent evt) {
		evt.getDispatcher().register(EpochCommands.make());
	}
}
