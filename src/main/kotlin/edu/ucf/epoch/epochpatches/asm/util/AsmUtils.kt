package edu.ucf.epoch.epochpatches.asm.util

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.Type.*
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.service.MixinService

object AsmUtils {
	@JvmField
	val LOGGER: Logger = LogManager.getLogger("Epoch Class Transformers")
	
	@JvmStatic
	fun getClassNode(name: String): ClassNode? = try {
		MixinService.getService().bytecodeProvider.getClassNode(name)
	} catch (_: ClassNotFoundException) {
		null
	}
	
	@JvmStatic
	fun descriptorForName(name: String): String = getObjectType(name.toInternalName()).descriptor
	
//	private val nameMapper = Launcher.INSTANCE?.environment()?.findNameMapping("srg")?.getOrNull()
//
//	fun getMappedName(name: String, domain: Domain): String {
//		if (!IS_OBF) {
//			return name
//		}
//		return nameMapper?.apply(domain, name) ?: name
//	}
	
	/**
	 * Get the correct return opcode for a given type. For example, an int or boolean needs `IRETURN`, but an object needs `ARETURN`, and void needs `RETURN`.
	 *
	 * @throws IllegalArgumentException if METHOD, INTERNAL, or other non-value [Type]s are given
	 */
	@JvmStatic
	fun returnOpcodeForType(type: Type): Int {
		return when (type.sort) {
			VOID -> RETURN
			INT, BOOLEAN, CHAR, BYTE, SHORT -> IRETURN
			Type.LONG -> LRETURN
			Type.DOUBLE -> DRETURN
			Type.FLOAT -> FRETURN
			OBJECT, ARRAY -> ARETURN
			else -> throw IllegalArgumentException("Invalid return type: $type")
		}
	}
}

fun String.toInternalName() = this.replace('.', '/')
fun String.toDescriptor() = "L${toInternalName()};"

fun Type.withAddedParameter(param: Type): Type {
	assert(this.sort == METHOD) { "Cannot add parameter to a non-method type!" }
	
	return getMethodType(this.returnType, *this.argumentTypes, param)
}

