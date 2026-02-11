package edu.ucf.epoch.epochpatches.asm.transformers

import com.llamalad7.mixinextras.utils.ASMUtils
import edu.ucf.epoch.epochpatches.asm.IClassTransformer
import edu.ucf.epoch.epochpatches.asm.util.AsmUtils
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode

class TDisableProcedureEntirely : IClassTransformer {
	override fun getRequested(): Array<String> = arrayOf(
			"net/mcreator/bioforge/procedures/CheckingVariablesForThisProcedure",
			"cyberspace/procedures/LeaveCommandProcedureProcedure"
	)
	
	override fun transform(node: ClassNode) {
		node.methods.forEach {
			it.visibleAnnotations?.clear()
			it.invisibleAnnotations?.clear()

			it.instructions = InsnList().apply {
				+InsnNode(Opcodes.RETURN)
			}
		}
	}
}