package edu.ucf.epoch.epochpatches.impl.diagnostics

import edu.ucf.epoch.epochpatches.Constants
import net.neoforged.neoforge.logging.ThreadInfoUtil
import java.lang.management.ManagementFactory
import java.nio.file.Files
import java.nio.file.Path

object DeadlockDetector {
	private val outFile: Path
		get() = Constants.EPOCH_FOLDER.resolve("deadlock-${System.currentTimeMillis()}.txt")
	
	fun findDeadlock() {
		Files.newOutputStream(outFile).use { writer ->
			ManagementFactory.getThreadMXBean().dumpAllThreads(true, true).forEach { info ->
				writer.write((ThreadInfoUtil.getEntireStacktrace(info) + "\n").toByteArray())
			}
		}
	}
}