package edu.ucf.epoch.epochpatches.util.scheduler

import net.minecraft.server.TickTask

/**
 * General purpose tick-by-tick scheduler, because FML doesn't have one for some reason.
 */
interface IEpochScheduler {
	/**
	 * The current server tick-count, i.e. what tick the server is currently on.
	 */
	val tickCount: Int
	
	/**
	 * Schedule a task [runnable] to run [ticksAhead] ticks from now.
	 */
	fun scheduleInAdvance(ticksAhead: Int, runnable: Runnable) {
		addTask(TickTask(this.tickCount + ticksAhead, runnable))
	}
	
	/**
	 * Schedule a task [runnable] to run every [step] ticks from [startTicksAhead] to [endTicksAhead] from now.
	 */
	fun scheduleRepeating(startTicksAhead: Int, endTicksAhead: Int, step: Int, runnable: Runnable) {
		var i = this.tickCount + startTicksAhead
		val end = i + endTicksAhead
		while (i < end) {
			addTask(TickTask(i, runnable))
			i += step
		}
	}
	
	/**
	 * Schedule a task [runnable] to run every [step] ticks from now until the server stops.
	 */
	fun scheduleRepeating(step: Int, runnable: Runnable) {
		scheduleInAdvance(1, RunForever(step, this, runnable))
	}
	
	/**
	 * Every time this task is executed, it schedules itself to run again in [step] ticks.
	 *
	 * Takes a reference instead of being an inner class because [IEpochScheduler] is an interface,
	 * and things aren't guaranteed to directly extend [EpochScheduler].
	 * (e.g. [EpochPatches][edu.ucf.epoch.epochpatches.EpochPatches] implements this by delegation, (NOT ANYMORE)
	 * but we want a RunForever to work like normal there)
	 */
	class RunForever(private val step: Int, private val scheduler: IEpochScheduler, private val action: Runnable)
		: Runnable
	{
		override fun run() {
			action.run()
			scheduler.scheduleInAdvance(step, this)
		}
	}
	
	/**
	 * Add a [TickTask] to be executed when its [TickTask.tick] is less than or equal to [tickCount]
	 */
	fun addTask(task: TickTask)
	
	/**
	 * Check which task is going to execute next from the queue, or null if the queue is empty.
	 */
	fun peek(): TickTask?
	
	/**
	 * Executes all tasks for the current tick, or does nothing if the queue is empty.
	 *
	 * Called every tick by [edu.ucf.epoch.epochpatches.EpochEventListeners.onServerTick].
	 */
	fun poll()
	
	/**
	 * Clear the internal queue.
	 */
	fun clear()
}

/**
 * Schedule a task [action] to run every [step] ticks,
 * starting from [startTicksAhead] from now, until [length] ticks from now.
 */
inline fun IEpochScheduler.scheduleRepeating(length: Int, step: Int = 1, startTicksAhead: Int = 0, crossinline action: (currentTick: Int) -> Unit) {
	var i = this.tickCount + startTicksAhead + 1
	val end = i + length
	while (i < end) {
		addTask(TickTask(i) { action(i) })
		i += step
	}
}


