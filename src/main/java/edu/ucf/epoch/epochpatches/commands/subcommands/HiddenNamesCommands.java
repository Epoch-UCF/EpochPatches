package edu.ucf.epoch.epochpatches.commands.subcommands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import github.pitbox46.hiddennames.PlayerDuck;
import github.pitbox46.hiddennames.data.NameData;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class HiddenNamesCommands {
	public static LiteralArgumentBuilder<CommandSourceStack> make() {
		return literal("hiddennames")
				.then(literal("get_alias")
						.then(argument("player", EntityArgument.player())
								.executes(HiddenNamesCommands::getAliasFromPlayer)))
				.then(literal("get_username")
						.then(argument("alias", StringArgumentType.string())
								.executes(HiddenNamesCommands::getPlayerFromAlias)));
	}
	
	private static int getAliasFromPlayer(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		var player = EntityArgument.getPlayer(ctx, "player");
		var originalName = ((PlayerDuck) player).hiddenNames$getUnmodifiedDisplayName();
		NameData nameData = NameData.DATA.get(player.getUUID());
		if (nameData == null) {
			ctx.getSource().sendFailure(Component.literal("No alias found for player ").append(originalName).append("."));
			return 0;
		}
		
		ctx.getSource().sendSuccess(() -> Component.literal("Player '").append(originalName).append("' has alias ").append(player.getDisplayName().copy().withStyle(ChatFormatting.YELLOW)).append("."), true);
		return 1;
	}
	
	private static int getPlayerFromAlias(CommandContext<CommandSourceStack> ctx) {
		var alias = StringArgumentType.getString(ctx, "alias");
		final var allMatching = new ArrayList<Player>();
		PlayerList playerList = ctx.getSource().getServer().getPlayerList();
		
		NameData.DATA.forEach((uuid, nameData) -> {
			if (nameData.getDisplayName().contains(Component.literal(alias))) {
				allMatching.add(playerList.getPlayer(uuid));
			}
		});
		if (allMatching.isEmpty()) {
			ctx.getSource().sendFailure(Component.literal("No aliases found containing '").append(alias).append("'."));
			return 0;
		}
		
		ctx.getSource().sendSuccess(() -> {
			var out = Component.literal("Players with aliases containing '").append(alias).append("':");
			allMatching.forEach(it -> out.append("\n\t- ").append(((PlayerDuck) it).hiddenNames$getUnmodifiedDisplayName()));
			return out;
		}, true);
		return 1;
	}
}
