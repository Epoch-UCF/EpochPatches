package edu.ucf.epoch.epochpatches.registry;

import edu.ucf.epoch.epochpatches.EpochPatchesMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public final class EpochPoiTypes {
	private static final DeferredRegister<PoiType> REGISTRY = DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, EpochPatchesMod.MODID);
	
	public static void register(IEventBus modBus) {
		REGISTRY.register(modBus);
	}
	
	public static final Holder<PoiType> SABEAST_REPELLANT;
	
	static {
		if (ModList.get().isLoaded("bygone")) {
			SABEAST_REPELLANT = REGISTRY.register("bygone_sabeast_repellant", () -> new PoiType(Set.of(), 99, 16));
		} else {
			SABEAST_REPELLANT = null;
		}
	}
}
