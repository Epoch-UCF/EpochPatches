package edu.ucf.epoch.epochpatches.asm.transformers

import edu.ucf.epoch.epochpatches.asm.IClassTransformer
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.TypeInsnNode

private const val TO_REPLACE = "java/util/HashSet"
private const val REPLACE_WITH = "edu/ucf/epoch/epochpatches/impl/mocked/HashSet_FastUtil"

abstract class TUseFastHashSet : IClassTransformer {
	abstract override fun getRequested(): Array<String>
	
	abstract fun getRequestedMethods(target: ClassNode): List<MethodNode>
	
	
	final override fun transform(node: ClassNode) {
		for (method in getRequestedMethods(node)) {
			transformMethod(method)
		}
	}
	
	fun transformMethod(method: MethodNode) {
		val iter = method.instructions.iterator()
		while (iter.hasNext()) {
			val insn = iter.next()
			when {
				insn is TypeInsnNode && insn.opcode == Opcodes.NEW && insn.desc == TO_REPLACE -> {
					insn.desc = REPLACE_WITH
				}
				insn is MethodInsnNode && insn.owner == TO_REPLACE && insn.opcode == Opcodes.INVOKESPECIAL && insn.name == "<init>" -> {
					insn.owner = REPLACE_WITH
				}
			}
		}
	}
}

class TVanishUtilSet : TUseFastHashSet() {
	override fun getRequested() = arrayOf(
			"redstonedubstep.mods.vanishmod.VanishUtil"
	)
	
	override fun getRequestedMethods(target: ClassNode): List<MethodNode> {
		return target.methods.filter { it.name == "<clinit>" }
	}
}