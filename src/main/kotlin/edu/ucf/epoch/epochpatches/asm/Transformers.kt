package edu.ucf.epoch.epochpatches.asm

import edu.ucf.epoch.epochpatches.asm.transformers.MCreatorPaths
import edu.ucf.epoch.epochpatches.asm.transformers.TDisableProcedureEntirely
import edu.ucf.epoch.epochpatches.asm.transformers.TMCreatorPacketSender
import edu.ucf.epoch.epochpatches.asm.util.AsmUtils
import edu.ucf.epoch.epochpatches.asm.util.toInternalName
import edu.ucf.epoch.epochpatches.mixin.MixinPackageAccess
import edu.ucf.epoch.epochpatches.util.toMultiMap
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.ACC_ABSTRACT
import org.objectweb.asm.Opcodes.ACC_INTERFACE
import org.objectweb.asm.Opcodes.ACC_PUBLIC
import org.objectweb.asm.Opcodes.V21
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.Mixin

/**
 * A standardized way of running old-fashioned ASM transformers to bypass the limitations of Mixin.
 *
 * Mixins can't do anything programmatically, so we need to interface with the backend it wraps around.
 */
object Transformers {
	@JvmField
	val LOGGER = AsmUtils.LOGGER
	
	@JvmStatic
	private val transformers: Map<String, List<IClassTransformer>> = sequenceOf(
			listOf(
					TDisableProcedureEntirely(),
			),
			arrayOf(
					MCreatorPaths("net.mcreator.callofyucutan.network.CallOfYucutanModVariables".toInternalName()),
					MCreatorPaths("net.createteleporters.network.CreateteleportersModVariables".toInternalName()),
					MCreatorPaths("net.mcreator.kingofthemobsters.network.KomModVariables".toInternalName()),
					MCreatorPaths("com.curseforge.macabre.network.MacabreModVariables".toInternalName()),
			).map { TMCreatorPacketSender(it) }
	)
		.flatten()
		.flatMap { tf -> tf.getRequested().asSequence().map { it to tf } }
		.toMultiMap(keyTransform = { it.replace('/', '.')})
    
    /**
	 * Mixin requires that any ASM transformation happen alongside an _existing_ Mixin targeting a class.
	 *
	 * Because having the transformers' targets defined both in random separate mixins AND in the transformers themselves sounds _awful_,
	 * this fabricates and emits mixins for each defined ASM transformer on the fly.
	 *
	 * The fabricated mixins are just interfaces with a [org.spongepowered.asm.mixin.Mixin] annotation,
	 * that has a `targets` field containing the requested targets.
	 *
	 * @return A list of the generated mixin names relative to the mixin package, for use in [org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin.getMixins]
	 */
	@JvmStatic
	fun makeTargetingMixin(): String {
		LOGGER.info("Creating transformers!")
		
		val emitter = MixinPackageAccess.LOOKUP
		
		val mixinPackage = emitter.lookupClass().packageName
		
		val relativeClassName = "ASMTargetingMixin"
		val generatedClassName = "$mixinPackage.$relativeClassName"
		
		ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS).apply {
			@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
			visit(V21, ACC_PUBLIC or ACC_ABSTRACT or ACC_INTERFACE, generatedClassName.toInternalName(), null, Object::class.java.name.toInternalName(), null)
			
			visitAnnotation(Mixin::class.java.descriptorString(), true).apply {
				visitArray("targets").apply {
					transformers.keys.forEach {
						visit("", it)
					}
				}
			}
			
			visitEnd()
		}.toByteArray().also {
			emitter.defineClass(it)
		}
		
		return relativeClassName
    }
	
	
	@JvmStatic
	fun executeTransformer(mixinClassName: String, targetClassNode: ClassNode) {
        transformers[mixinClassName]?.forEach {
			it.transform(targetClassNode)
		}
	}
}


