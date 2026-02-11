package edu.ucf.epoch.epochpatches.impl.mcreatorshit

import edu.ucf.epoch.epochpatches.EpochPatchesMod.LOGGER
import edu.ucf.epoch.epochpatches.impl.mcreatorshit.WorldDataPacketSender.run
import edu.ucf.epoch.epochpatches.mixinsupport.MCreatorSavedDataDuck
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.saveddata.SavedData
import java.util.UUID
import kotlin.collections.iterator

typealias Dimension = ResourceKey<Level>

private typealias FastMap<T, U> = Object2ObjectOpenHashMap<T, U>
private typealias FastSet<T> = ObjectOpenHashSet<T>

/**
 * Stop MCreator from literally DDOSing clients with world data update packets >_<
 *
 * Current MCreator has _every_ change to world/map data send its _own_ packet containing the _entire_ WorldVariables/MapVariables object.
 * Instead of that, this consolidates all of those into a single "XVariables object" packet sent every [MCREATOR_PACKET_INTERVAL][edu.ucf.epoch.epochpatches.EpochPatches.MCREATOR_PACKET_INTERVAL] ticks.
 *
 * In all honesty, barely any of what MCreator does is even necessary.  Only a handful of variables need to be sent to the client in the first place,
 * and those should really just have their own custom packets.  As such, throttling them doesn't really change much.
 *
 * If someone finds a problem, like their number of upgrade points not updating in the UI because it uses player variables, then we can use individual Mixins to sync those separately.
 *
 * # Pipeline
 *
 * ## Transformer
 *
 * [TMCreatorPacketSender][edu.ucf.epoch.epochpatches.asm.transformers.TMCreatorPacketSender] makes each `WorldData.get` method register the packet to be synced over the given dimension,
 * and makes `WorldData#syncData` schedule with US to sync the data to the client sometime in the future instead of
 * invoking the original packet-sending method. The original packet-sending method is also renamed to `epoch_savedData`.
 *
 * The PacketTransformer also attaches the [MCreatorSavedDataDuck][edu.ucf.epoch.epochpatches.mixinsupport.MCreatorSavedDataDuck] interface,
 * which allows us to invoke that "`epoch_syncData`" method without needing reflection.
 *
 * ## Scheduler
 *
 * - When something changes on the MCreator mod's end, instead of sending the packet itself, it tells us that it wants to schedule a `MCreatorMod.WorldData` packet on the dimension "SomeDimension".
 *   - Only one request sticks around per type, since we sync the entire data package in a single packet.
 *   - If something updates multiple times in a single tick, there's no reason to sync anything but the final change.
 * - Every few seconds, the scheduler invokes us, and we check the requests and send each packet over its dimension in the [run] method.
 */
object WorldDataPacketSender : Runnable {
	lateinit var server: MinecraftServer
	
	private val storedPacketSenders: FastMap<Dimension, FastMap<Class<*>, WorldDataSync>> = Object2ObjectOpenHashMap()
	
	private val scheduledWorldData: MutableMap<Dimension, FastSet<Class<*>>> = Object2ObjectOpenHashMap()
	
//	private val scheduledPlayerData: MutableMap<UUID, FastSet<Class<*>>> = Object2ObjectOpenHashMap()
	
	/**
	 * Whether the given class is registered to send a packet over the given dimension.
	 */
	fun isRegistered(type: Class<*>, dimension: Dimension): Boolean {
		return storedPacketSenders[dimension]?.get(type) != null
	}
	
	/**
	 * Register a data type "[type]" to be synced with the client over the given [dimension], using the given [synchronizer] to send the packet.
	 *
	 */
	fun registerWorldData(type: Class<*>, dimension: Dimension, synchronizer: WorldDataSync) {
		if (!isRegistered(type, dimension)) {
			LOGGER.debug("Registering packet sender: {} - {}", type, dimension);
			
			storedPacketSenders.computeIfAbsent(dimension) { _: Dimension ->
				Object2ObjectOpenHashMap()
			}.put(type, synchronizer)
		}
	}
	
	// Current handling is to just make a mixin to stop them from syncing altogether, because it can usually be avoided.
	// Idk why MCreator insists on having everything server-side be synced to the client when it really doesn't need to.
	fun registerPlayerVariables(uuid: UUID, type: Class<*>) {
		TODO("Not yet implemented.")
	}
	
	val alreadyGaveMessage = FastSet<Pair<Class<*>, Dimension>>();
	
	/**
	 * Default MCreator behavior is that whenever any change happens to any part of the data, it tries to send a packet containing all the data, not just the single change that occurred.
	 *
	 * We change it so that it tells our sender that it wants to sync that type of data.  Then we still send the entire thing at once, just rate-limited.
	 *
	 * We don't use isDirty checks to avoid retaining the whole data.
	 */
	fun schedule(type: Class<*>, dimension: Dimension) {
		if (!isRegistered(type, dimension)) {
			val pair = type to dimension
			if (pair !in alreadyGaveMessage) {
				LOGGER.error("Attempted to schedule packet for class '{}' that was not registered in dimension '{}'", type, dimension)
				alreadyGaveMessage.add(pair)
			}
			return;
		}
		
		scheduledWorldData.computeIfAbsent(dimension) { ObjectOpenHashSet() }
			.add(type)
	}
	
	/**
	 * Run every scheduled packet sender, fetching them from the map with their dimension and type.
	 *
	 * Grouped by dimension for efficiency so we aren't getting the level multiple times per tick.
	 */
	override fun run() {
		for ((dim, toRun) in scheduledWorldData) {
			val level: ServerLevel = server.getLevel(dim) ?: return LOGGER.error("Null server level for dimension \"{}\"!", dim)
			for (type in toRun) {
				storedPacketSenders[dim]?.get(type)?.accept(level) ?: LOGGER.error("World data packet send scheduled for {} without existing!", type)
			}
		}
		
		scheduledWorldData.values.forEach(FastSet<*>::clear)
		scheduledWorldData.clear()
	}
}

class WorldDataSync(
	val loadFunction: SavedData.Factory<*>,
	val dataName: String
) {
	constructor(clazz: Class<out SavedData>, dataName: String)
			: this(SavedData.Factory(clazz.getConstructor()::newInstance, clazz.getDeclaredMethod("load", CompoundTag::class.java, HolderLookup.Provider::class.java).let { { tag, provider -> it.invoke(null, tag, provider) as SavedData } }), dataName)
	
	fun accept(level: ServerLevel) {
		val data = level.dataStorage.get(loadFunction, dataName) as? MCreatorSavedDataDuck ?: return;
		data.epoch_syncData(level)
	}
}