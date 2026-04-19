package edu.ucf.epoch.epochpatches.commands.subcommands;

import com.kreezcraft.localizedchat.ConfigCache;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import edu.ucf.epoch.epochpatches.mixinsupport.MixinData;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

import static net.minecraft.commands.Commands.*;

public class ProxChatCommands {
	public static LiteralArgumentBuilder<CommandSourceStack> make() {
		return literal("toggle_op_global_chat")
				.requires(source -> source.hasPermission(LEVEL_MODERATORS))
				.executes(ctx -> {
					if (ConfigCache.opAsPlayer) {
						ConfigCache.opAsPlayer = false;
						ctx.getSource().sendSuccess(() -> Component.literal("Ops will now talk in ").append(Component.literal("global").withStyle(ChatFormatting.RED).append(" chat.")), true);
					} else {
						ConfigCache.opAsPlayer = true;
						ctx.getSource().sendSuccess(() -> Component.literal("Ops will now talk in ").append(Component.literal("local").withStyle(ChatFormatting.YELLOW).append(" chat.")), true);
					}
					return Command.SINGLE_SUCCESS;
				});
	}
}
