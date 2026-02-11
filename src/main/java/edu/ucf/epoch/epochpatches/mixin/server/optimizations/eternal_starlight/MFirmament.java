package edu.ucf.epoch.epochpatches.mixin.server.optimizations.eternal_starlight;

import edu.ucf.epoch.epochpatches.mixinsupport.IFirmamentExtensions;
import it.unimi.dsi.fastutil.longs.Long2IntAVLTreeMap;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phanastrae.operation_starcleave.world.firmament.Firmament;

@Mixin(value = Firmament.class, remap = false)
abstract class MFirmament implements IFirmamentExtensions {
    @Unique
    private final Long2IntMap epoch$firmamentDamageTickCache = new Long2IntAVLTreeMap();

    @Shadow
    public abstract int getDamage(int var1, int var2);

    @Unique
    private static long epoch$intsToLong(int x, int z) {
        return (long)x << 32 | (long)z;
    }

    public final int epoch$getCachedDamage(int x, int z) {
        return this.epoch$firmamentDamageTickCache.computeIfAbsent(epoch$intsToLong(x, z), (it) -> this.getDamage(x, z));
    }

    @Inject(
        method="tick",
        at=@At("HEAD")
    )
    private void flushCacheOnTick(CallbackInfo ci) {
        this.epoch$firmamentDamageTickCache.clear();
    }
}
