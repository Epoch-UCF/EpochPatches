package edu.ucf.epoch.epochpatches.classdeferrals;

import edu.ucf.epoch.epochpatches.registry.EpochPoiTypes;
import edu.ucf.epoch.epochpatches.util.MiscUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

import static com.jamiedev.bygone.core.init.JamiesModTag.SABEAST_REPELLENTS;

public final class CBygone {
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static Optional<Holder<PoiType>> operation(Optional<Holder<PoiType>> original, BlockState state) {
		return original.or(() -> MiscUtils.optWithCond(state.is(SABEAST_REPELLENTS), EpochPoiTypes.SABEAST_REPELLANT));
	}
}
