package edu.ucf.epoch.epochpatches.mixinsupport;

import oshi.PlatformEnum;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor.PhysicalProcessor;
import oshi.software.os.OperatingSystem;

public final class MixinData {
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