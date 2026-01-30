package edu.ucf.epoch.epochpatches;

import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(EpochPatchesMod.MODID)
public class EpochPatchesMod {
	public static final String MODID = "epochpatches";
	public static final Logger LOGGER = LogUtils.getLogger();
	
	/** The name of the world in the server. For season 3 it was "expedition3". */
	public static final String WORLD_NAME = "New World";
	
	public static MinecraftServer server;
	
	// The constructor for the mod class is the first code that is run when your mod is loaded.
	// FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
	public EpochPatchesMod(IEventBus modEventBus)
	{
		// Register the commonSetup method for modloading
		modEventBus.addListener(this::commonSetup);
		NeoForge.EVENT_BUS.addListener(this::onServerStarting);
	}
	
	private void commonSetup(final FMLCommonSetupEvent event)
	{
		LOGGER.info("HELLO FROM COMMON SETUP");
		
	}
	
	private void onServerStarting(ServerStartingEvent event)
	{
		// Do something when the server starts
		LOGGER.info("HELLO from server starting");
		server = event.getServer();
	}
}
