package edu.ucf.epoch.epochpatches.asm

import edu.ucf.epoch.epochpatches.asm.util.AsmUtils
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*

/**
 * Because for SOME REASON, Mixin accessors just don't work properly anymore in this project. I wish I knew why.
 * Anyway, this is a standardized way of making accessor transformers for fields.
 *
 * (EDIT: Mixin accessors work now, so ignore this.)
 */
class ManualAccessor(
		/**
		 * Name of the target class, "/"-separated
		 */
		val targetClass: String,
		/**
		 * Name of the field. Descriptor will be gotten from the field itself.
		 */
		val targetFieldName: String,
		/**
		 * Name of the method hook in AccessorBridges for this field
		 */
		val bridgeMethodName: String
) {
	companion object {
		const val ACCESSOR_BRIDGE = "edu/ucf/epoch/epochpatches/asm/hooks/AccessorBridges"
	}
	
	
	fun run(accessorBridgeNode: ClassNode) {
		val targetNode = AsmUtils.getClassNode(targetClass) ?: return AsmUtils.LOGGER.error("Accessor target not found: {}", targetClass)
		val fieldNode = targetNode.fields.firstOrNull { it.name == targetFieldName } ?: return AsmUtils.LOGGER.error("Field not found: {}.{}", targetClass, targetFieldName)
		
		exposeField(targetNode, fieldNode).let {
			accessorMethodHook(accessorBridgeNode, it)
		}
		
		edu.ucf.epoch.epochpatches.asm.util.AsmFileWriter.printClass(targetNode)
	}
	
	
	fun exposeField(node: ClassNode, fieldNode: FieldNode): MethodNode {
		val isStatic = (Opcodes.ACC_STATIC and fieldNode.access) != 0
		val fieldType = Type.getType(fieldNode.desc)
		
		return MethodNode().apply {
			access = Opcodes.ACC_PUBLIC or (if (isStatic) Opcodes.ACC_STATIC else 0)
			name = "epochAccessor\$get$targetFieldName"
			desc = Type.getMethodType(fieldType).descriptor
			instructions = InsnList().apply {
				if (!isStatic) {
					add(IntInsnNode(Opcodes.ALOAD, 0))
				}
				add(FieldInsnNode(
						if (isStatic) Opcodes.GETSTATIC else Opcodes.GETFIELD,
						targetClass,
						fieldNode.name,
						fieldNode.desc
				))
				add(InsnNode(AsmUtils.returnOpcodeForType(fieldType)))
			}
		}.also {
			node.methods.add(it)
		}
	}
	
	fun accessorMethodHook(node: ClassNode, addedMethod: MethodNode) {
		val theHook = node.methods.firstOrNull { it.name == bridgeMethodName } ?: return AsmUtils.LOGGER.error("Manual accessor failure! $ACCESSOR_BRIDGE.{} not found", bridgeMethodName)
		
		theHook.instructions.run {
			clear()
			val opcode = if (addedMethod.access and Opcodes.ACC_STATIC != 0) {
				Opcodes.INVOKESTATIC
			} else {
				Opcodes.INVOKEVIRTUAL
			}
			add(MethodInsnNode(opcode, targetClass, addedMethod.name, addedMethod.desc))
			add(InsnNode(AsmUtils.returnOpcodeForType(Type.getReturnType(addedMethod.desc))))
		}
	}
}