package edu.ucf.epoch.epochpatches.mixinsupport;

import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.Map;

/**
 * MethodHandles are a way to get unchanging, zero-overhead access to some method somewhere.
 * It's cheaper than reflection, but requires a little more legwork to set up.
 */
@SuppressWarnings("unchecked") public final class EpochMethodHandles {
	private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
	
	private static final MethodHandle AttachmentHolder$getAttachmentMap;
	public static Map<AttachmentType<?>, Object> AttachmentHolder$getAttachmentMap(AttachmentHolder instance) {
		try {
			return (Map<AttachmentType<?>, Object>) AttachmentHolder$getAttachmentMap.invokeExact(instance);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	private static final VarHandle AttachmentType$serializer;
	public static @Nullable IAttachmentSerializer<Tag, Object> AttachmentType$serializer(AttachmentType<?> instance) {
		return (IAttachmentSerializer<Tag, Object>) AttachmentType$serializer.get(instance);
	}
	
	static {
		try {
			AttachmentHolder$getAttachmentMap = MethodHandles.privateLookupIn(AttachmentHolder.class, lookup)
			                                                 .findVirtual(AttachmentHolder.class, "getAttachmentMap", MethodType.methodType(Map.class));
			
			AttachmentType$serializer = MethodHandles.privateLookupIn(AttachmentType.class, lookup)
			                                         .findVarHandle(AttachmentType.class, "serializer", IAttachmentSerializer.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
