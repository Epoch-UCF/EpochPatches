package edu.ucf.epoch.epochpatches.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.literal;

public final class EpochCommands {
	public static LiteralArgumentBuilder<CommandSourceStack> make() {
		return literal("epoch")
				       .requires((stack) -> stack.hasPermission(4));
	}
}
