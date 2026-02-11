package edu.ucf.epoch.epochpatches.util.scheduler

import net.minecraft.server.TickTask
import java.util.PriorityQueue
import java.util.function.IntSupplier


/**
 * General purpose tick-by-tick scheduler, because FML doesn't have one for some reason.
 */
class EpochScheduler(private val tickCountGetter: IntSupplier) : IEpochScheduler {
	private val QUEUE = PriorityQueue(Comparator.comparingInt(TickTask::getTick))
	
	override val tickCount: Int
		get() = tickCountGetter.asInt
	
	override fun addTask(task: TickTask) {
		QUEUE.add(task)
	}
	
	override fun peek(): TickTask? {
		return QUEUE.peek()
	}
	
	override fun poll() {
		while (QUEUE.isNotEmpty() && tickCount >= QUEUE.peek().tick) {
			QUEUE.poll().run()
		}
	}
	
	override fun clear() {
		QUEUE.clear()
	}
}