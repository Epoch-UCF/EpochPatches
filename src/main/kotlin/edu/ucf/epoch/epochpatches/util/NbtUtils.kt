package edu.ucf.epoch.epochpatches.util

import com.google.gson.JsonObject
import com.mojang.brigadier.StringReader
import com.mojang.serialization.JsonOps
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.TagParser

/**
 * Create a new CompoundTag from SNBT
 */
fun CompoundTag(s: String): CompoundTag {
	return TagParser(StringReader(s)).readStruct()
}

/**
 * Convert a [CompoundTag] to a [JsonObject]
 */
fun CompoundTag.toJson() = CompoundTag.CODEC.encode(this, JsonOps.INSTANCE, JsonObject()).orThrow
