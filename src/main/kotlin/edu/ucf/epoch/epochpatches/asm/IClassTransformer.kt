package edu.ucf.epoch.epochpatches.asm

import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList

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