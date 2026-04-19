package edu.ucf.epoch.epochpatches.mixin.server.optimizations.bygone;

import com.jamiedev.bygone.common.entity.SabeastEntity;
import com.jamiedev.bygone.common.entity.ai.AvoidBlockGoal;
import edu.ucf.epoch.epochpatches.impl.goals.AvoidBlockGoal_POI;
import edu.ucf.epoch.epochpatches.registry.EpochPoiTypes;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

@Mixin(SabeastEntity.class)
abstract class MSabeastEntity {
	/**
	 * Replace the laggy "check every block in range to see if it has a tag" with one using a point of interest.
	 */
	@Redirect(
			method="registerGoals",
			at=@At(
					target="com/jamiedev/bygone/common/entity/ai/AvoidBlockGoal",
					value="NEW"
			)
	)
	private @Coerce Goal usePoiInstead(PathfinderMob pMob, float pMaxDistance, double pWalkSpeedModifier, double pSprintSpeedModifier, Predicate<?> posFilter) {
		return new AvoidBlockGoal_POI(pMob, ((int) pMaxDistance), pWalkSpeedModifier, pSprintSpeedModifier, EpochPoiTypes.SABEAST_REPELLANT.value());
	}
}

