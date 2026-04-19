package edu.ucf.epoch.epochpatches.mixin.server.features.localizedchat;

import com.kreezcraft.localizedchat.commands.TalkChat;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import edu.ucf.epoch.epochpatches.mixinsupport.MixinData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = TalkChat.class, remap = false)
public class MOperatorProxChat {
	@WrapMethod(
			method="isPlayerOpped"
	)
	private static boolean useConfigForOps(MinecraftServer server, ServerPlayer player, Operation<Boolean> original) {
		return original.call(server, player) && !MixinData.OPS_WITHOUT_BLAST.contains(player.getUUID());
	}
}
