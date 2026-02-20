package edu.ucf.epoch.epochpatches.mixin.server.optimizations.minecraft;

import edu.ucf.epoch.epochpatches.util.documentation.NoOp;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FlyingPathNavigation.class)
abstract class MFlyingPathNavigation {
	/**
	 * Stop it from sending the debug pathfinding packet. No one needs it, and it's using 0.13% of tick time
	 */
	@Redirect(
			method = "tick",
			at = @At(
					target = "Lnet/minecraft/network/protocol/game/DebugPackets;sendPathFindingPacket(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Mob;Lnet/minecraft/world/level/pathfinder/Path;F)V",
					value = "INVOKE"
			)
	) @NoOp
	private void noSendDebugPacket(Level level, Mob mob, Path path, float maxDistanceToWaypoint) {}
}

