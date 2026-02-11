package edu.ucf.epoch.epochpatches.asm

import edu.ucf.epoch.epochpatches.asm.transformers.MCreatorPaths
import edu.ucf.epoch.epochpatches.asm.transformers.TDisableProcedureEntirely
import edu.ucf.epoch.epochpatches.asm.transformers.TMCreatorPacketSender
import edu.ucf.epoch.epochpatches.asm.transformers.TVanishUtilSet
import edu.ucf.epoch.epochpatches.asm.util.AsmFileWriter
import edu.ucf.epoch.epochpatches.asm.util.AsmUtils
import edu.ucf.epoch.epochpatches.asm.util.toInternalName

/**
 * A standardized way of running old-fashioned ASM transformers to bypass the limitations of Mixin.
 *
 * Mixins can't do anything programmatically, so we need to interface with the backend it wraps around.
 */
object Transformers {
	@JvmStatic
	val LOGGER = AsmUtils.LOGGER
	
	@JvmStatic
	private var accessors: Array<ManualAccessor>? = arrayOf(
	)
		get() {
			val current = field
			field = null
			return current
		}
	
	@JvmStatic
	private var transformers: Array<IClassTransformer>? = listOf(
			listOf(
					TDisableProcedureEntirely(),
					TVanishUtilSet(),
			),
			arrayOf(
					MCreatorPaths("net.mcreator.callofyucutan.network.CallOfYucutanModVariables".toInternalName()),
					MCreatorPaths("net.createteleporters.network.CreateteleportersModVariables".toInternalName()),
					MCreatorPaths("net.mcreator.kingofthemobsters.network.KomModVariables".toInternalName()),
					MCreatorPaths("com.curseforge.macabre.network.MacabreModVariables".toInternalName()),
			).map { TMCreatorPacketSender(it) }
	).flatten().toTypedArray()
		get() {
			val current = field
			field = null
			return current
		}
	
	@JvmStatic
	fun executeTransformers() {
		AsmUtils.getClassNode(ManualAccessor.ACCESSOR_BRIDGE)?.let { bridgeNode ->
			accessors!!.forEach {
				it.run(bridgeNode)
			}
			AsmFileWriter.printClass(bridgeNode)
		} ?: LOGGER.error("Error evaluating accessor bridge: Class ${ManualAccessor.ACCESSOR_BRIDGE} not found.")
		
		transformers!!.asSequence()
			.flatMap { tf -> tf.getRequested().asSequence().map { it to tf } }
			.groupBy { it.first }
			.forEach { (reqClass, transformers) ->
				val node = AsmUtils.getClassNode(reqClass)
				           ?: return@forEach LOGGER.warn("Transformer target \"{}\" not found for the following transformers: {}", reqClass, transformers.joinToString(", ") { (_, tf) -> tf.javaClass.name });
				
				transformers.forEach { (_, tf) ->
					tf.transform(node)
				}
				
				AsmFileWriter.printClass(node)
			}
	}
}