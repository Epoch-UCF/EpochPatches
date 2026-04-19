package edu.ucf.epoch.epochpatches.asm

import edu.ucf.epoch.epochpatches.asm.util.toInternalName
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.spongepowered.asm.mixin.Mixin
import java.lang.invoke.MethodHandles

interface IClassTransformer {
	/**
	 * An array of the requested class names.
	 * May be dot-separated (e.g. `edu.ucf.epoch.IClassTransformer`) or slash-separated (e.g. `edu/ucf/epoch/IClassTransformer`)
	 */
	fun getRequested(): Array<String>
	
	fun transform(node: ClassNode)
	
	context(list: InsnList)
	operator fun AbstractInsnNode.unaryPlus() {
		list.add(this@unaryPlus)
	}
}
