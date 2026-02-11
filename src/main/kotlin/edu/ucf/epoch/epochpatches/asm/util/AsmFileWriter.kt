package edu.ucf.epoch.epochpatches.asm.util

import edu.ucf.epoch.epochpatches.asm.util.AsmUtils
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

private val replaceWithSlash = Regex("[.$]")

object AsmFileWriter {
	const val OUTPUT_PATH = ".asm.out"
	
	var shouldWrite = true
	val outDirectory: Path = File(OUTPUT_PATH).also {
		when {
			!it.exists() -> it.mkdir()
			!it.isDirectory -> {
				AsmUtils.LOGGER.error("Unable to write to $OUTPUT_PATH: is file!")
				shouldWrite = false
			}
		}
	}.toPath()
	
	fun clear() {
		if (shouldWrite) {
			FileUtils.deleteDirectory(outDirectory.toFile())
		}
	}
	
	init {
		clear()
	}
	
	fun printClass(node: ClassNode) {
		printClass(node.name, ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS).let {
			node.accept(it)
			it.toByteArray()
		})
	}
	
	fun printClass(name: String, bytes: ByteArray) {
		val outFile = outDirectory.resolve(name.replace(replaceWithSlash, "/") + ".class")
		
		try {
			FileUtils.createParentDirectories(outFile.toFile())
			
			Files.newOutputStream(outFile).use {
				it.write(bytes)
			}
		} catch (e: Exception) {
			AsmUtils.LOGGER.warn("Unable to write: {}", name, e)
		}
	}
}