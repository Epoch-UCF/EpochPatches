package edu.ucf.epoch.epochpatches.impl.goals

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerChunkCache
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.util.DefaultRandomPos
import net.minecraft.world.entity.ai.village.poi.PoiManager
import net.minecraft.world.entity.ai.village.poi.PoiType
import net.minecraft.world.level.pathfinder.Path
import java.util.EnumSet
import kotlin.jvm.optionals.getOrNull

class AvoidBlockGoal_POI(val mob: PathfinderMob, val maxDistance: Int, val walkSpeedModifier: Double, val sprintSpeedModifier: Double, val poiType: PoiType) : Goal() {
	val pathNav = mob.navigation
	var path: Path? = null
	var toAvoid: BlockPos? = null
	
	init {
		flags = EnumSet.of(Flag.MOVE)
	}
	
	override fun canUse(): Boolean {
		val onServer = (mob.level().chunkSource as? ServerChunkCache)
		               ?: return false;
		
		val mobPos = mob.blockPosition()
		val blockPos = onServer.poiManager
			.getInRange({ poiType == it.value() }, mobPos, maxDistance, PoiManager.Occupancy.ANY)
			.min(Comparator.comparingDouble { it.pos.distSqr(mobPos) })
			.getOrNull()
			?.pos
		               ?: return false;
		
		val posAway = DefaultRandomPos.getPosAway(this.mob, 16, 7, blockPos.center)
		              ?: return false;
		
		if (this.mob.distanceToSqr(posAway.x, posAway.y, posAway.z) > this.mob.distanceToSqr(blockPos.center)) {
			this.path = this.pathNav.createPath(posAway.x, posAway.y, posAway.z, 0)
			if (this.path != null) {
				this.toAvoid = blockPos
				return true;
			}
		}
		
		return false;
	}
	
	override fun canContinueToUse(): Boolean {
		return !this.pathNav.isDone
	}
	
	override fun start() {
		this.pathNav.moveTo(this.path, this.walkSpeedModifier)
	}
	
	override fun stop() {
		this.toAvoid = null
	}
	
	override fun tick() {
		val toAvoid1 = this.toAvoid ?: return;
		
		if (this.mob.distanceToSqr(toAvoid1.center) < 49.0) {
			this.mob.getNavigation().setSpeedModifier(this.sprintSpeedModifier)
		} else {
			this.mob.getNavigation().setSpeedModifier(this.walkSpeedModifier)
		}
	}
}