package edu.ucf.epoch.epochpatches.methodhandles

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.saveddata.SavedData
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType

/**
 * My hard drive is so full, I can't hold more dependencies ;_;
 *
 * So this is just where I do my (jank-looking but perfectly fast) JVM-bound reflection
 * so I don't have to download a whole library for a single method.
 */
object MethodHandlesForMixins {
	@JvmStatic
	private val lookup = MethodHandles.lookup()
	
}

// interesting idea: structural typing (by-name only) with method handles?
// ...isn't that just how interface methods work?