package edu.ucf.epoch.epochpatches.mixin.server.optimizations.vanishmod;

import edu.ucf.epoch.epochpatches.impl.mocked.DelegatedHashSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashSet;

/**
 * Use FastUtil set instead of laggy Java HashSet
 */
@Mixin(targets="redstonedubstep.mods.vanishmod.VanishUtil", remap=false) @Pseudo
abstract class MVanishUtil {
	@Redirect(
			method = "<clinit>",
			at = @At(
					target = "java/util/HashSet",
					value = "NEW"
			)
	)
	private static HashSet<?> useFastSet() {
		return new DelegatedHashSet<>(new ObjectOpenHashSet<>(5, ObjectOpenHashSet.VERY_FAST_LOAD_FACTOR));
	}
}

