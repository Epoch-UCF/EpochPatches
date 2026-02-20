package edu.ucf.epoch.epochpatches.mixin.server.patches.clavis;

import it.hurts.shatterbyte.clavis.common.LockManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value=LockManager.class)
public class MLockManager {
	@Redirect(
			method="isLocked",
			at=@At(
					target="net/minecraft/world/level/Level.isClientSide : Z",
					value="FIELD",
					opcode = Opcodes.GETFIELD
			)
	)
	private static boolean returnFalseIfNull(Level instance) {
		if (instance == null) {
			return false;
		} else {
			return instance.isClientSide;
		}
	}
}
