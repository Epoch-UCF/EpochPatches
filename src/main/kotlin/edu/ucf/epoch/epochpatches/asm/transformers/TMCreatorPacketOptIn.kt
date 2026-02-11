package edu.ucf.epoch.epochpatches.asm.transformers

import edu.ucf.epoch.epochpatches.asm.IClassTransformer
import edu.ucf.epoch.epochpatches.asm.Transformers
import edu.ucf.epoch.epochpatches.asm.util.toDescriptor
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*

@JvmInline
value class MCreatorPaths(val dataClassLocation: String) {
	val mapDataClass: String
		get() = "$dataClassLocation\$MapVariables"
	
	val worldDataClass: String
		get() = "$dataClassLocation\$WorldVariables"
}

private const val SYNC_DATA_ORIGINAL_METHOD_NAME = "syncData"
private const val SYNC_DATA_TF_METHOD_NAME = "epoch_syncData"

private const val SYNCDATA_ITF = "edu/ucf/epoch/epochpatches/mixinsupport/MCreatorSavedDataDuck"

private const val HOOK_CLASS = "edu/ucf/epoch/epochpatches/asm/hooks/RegisterPacketSender"
private const val HOOK_METHOD_GET_NAME = "checkIfInWorldDataPacketSender"
private const val HOOK_METHOD_GET_DESC = "(Ljava/lang/Class;Lnet/minecraft/world/level/LevelAccessor;Ljava/lang/String;)V"

/**
 * Since MCreator is deterministic, tell them to use [WorldDataPacketSender][edu.ucf.epoch.epochpatches.impl.mcreatorshit.WorldDataPacketSender] HERE instead of via manual mixins.
 *
 * It also means we don't have to depend on their jars, we can just do it reflectively.
 */
class TMCreatorPacketSender(val info: MCreatorPaths) : IClassTransformer {
	override fun getRequested() = arrayOf(info.mapDataClass, info.worldDataClass)
	
	override fun transform(node: ClassNode) {
		node.interfaces.add(SYNCDATA_ITF)
		transformSyncData(node)
		node.methods.firstOrNull { it.name == "get" }
			?.let { transformGetMethod(node, it) }
				?: Transformers.LOGGER.error("TMCreatorPacketSender: No method 'get' for class ${node.name}!")
	}
	
	
	/**
	 * Insert call to [edu.ucf.epoch.epochpatches.asm.hooks.RegisterPacketSender.checkIfInWorldDataPacketSender] at the head of the method.
	 */
	fun transformGetMethod(classNode: ClassNode, method: MethodNode) {
		val head = method.instructions.first { it !is LabelNode && it !is LineNumberNode }
		val dataKey = (method.instructions.first { it is LdcInsnNode && it.cst is String } as LdcInsnNode).cst as String
		
		method.instructions.insertBefore(head, InsnList().apply {
			+LdcInsnNode(Type.getType(classNode.name.toDescriptor()))
			+IntInsnNode(ALOAD, 0)
			+LdcInsnNode(dataKey)
			+MethodInsnNode(INVOKESTATIC, HOOK_CLASS, HOOK_METHOD_GET_NAME, HOOK_METHOD_GET_DESC)
		})
	}
	
	/**
	 * Remove the `syncData` method's `this.setDirty()` call, rename it to `epoch$syncData`,
	 * and add a new `syncData` method that solely schedules the packet to be sent later.
	 */
	fun transformSyncData(clazz: ClassNode) {
		val originalSyncMethod = clazz.methods.find { it.name == SYNC_DATA_ORIGINAL_METHOD_NAME }
		                         ?: return Transformers.LOGGER.error("TMCreatorPacketSender: No method \"syncData\" found on class \"{}\"", clazz.name);
		/* to remove:
		ALOAD 0
	    INVOKEVIRTUAL com/curseforge/macabre/network/MacabreModVariables$WorldVariables.setDirty ()V
		 */
		originalSyncMethod.instructions.iterator().let { iter ->
			while (iter.hasNext()) {
				val curr = iter.next()
				if (curr is IntInsnNode && curr.opcode == ALOAD && curr.operand == 0) {
					iter.remove()
					iter.next()
					iter.remove()
					break;
				}
			}
		}
		
		// create a mimic of the original method that intercepts calls to it
		clazz.methods.add(MethodNode(ASM5, originalSyncMethod.access, originalSyncMethod.name, originalSyncMethod.desc, originalSyncMethod.signature, originalSyncMethod.exceptions.toTypedArray()).apply {
			instructions = InsnList().apply {
				val l0 = LabelNode()
				val l1 = LabelNode()
				+l0
					+IntInsnNode(ALOAD, 0)
					+IntInsnNode(ALOAD, 1)
					+MethodInsnNode(INVOKESTATIC, HOOK_CLASS, "schedulePacketInsteadOfSyncingData", "(Lnet/minecraft/world/level/saveddata/SavedData;Lnet/minecraft/world/level/LevelAccessor;)V")
				
				+l1
					+InsnNode(RETURN)
			}
		})
		
		// original syncData method will now only be invoked by the PacketSender to actually send the packet
		originalSyncMethod.name = SYNC_DATA_TF_METHOD_NAME
	}
}