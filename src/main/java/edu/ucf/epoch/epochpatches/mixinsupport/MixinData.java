package edu.ucf.epoch.epochpatches.mixinsupport;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.spongepowered.asm.mixin.Unique;
import oshi.PlatformEnum;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor.PhysicalProcessor;

import java.util.Set;
import java.util.UUID;

public final class MixinData {
	public static final Set<UUID> OPS_WITHOUT_BLAST = new ObjectOpenHashSet<>();
	@Unique
	public static final String RUN_ASYNC_PREFIX = "run_async ";
	
	public static Thread TERMINAL_THREAD = null;
	
	// copied from MacCentralProcessor#isArm
	public static final boolean IS_APPLE_SILICON = SystemInfo.getCurrentPlatform() == PlatformEnum.MACOS
       && new SystemInfo()
	        .getHardware()
	        .getProcessor()
	        .getPhysicalProcessors()
	        .stream()
	        .map(PhysicalProcessor::getIdString)
	        .anyMatch(id -> id.contains("arm"));
	
	
	private MixinData() {
    }
}