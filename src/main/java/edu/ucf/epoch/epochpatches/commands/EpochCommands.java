package edu.ucf.epoch.epochpatches.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import edu.ucf.epoch.epochpatches.commands.subcommands.DataCommands;
import edu.ucf.epoch.epochpatches.commands.subcommands.RestartCommand;
import edu.ucf.epoch.epochpatches.impl.diagnostics.DeadlockDetector;
import net.minecraft.commands.CommandSourceStack;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.minecraft.commands.Commands.literal;

public final class EpochCommands {
	public static LiteralArgumentBuilder<CommandSourceStack> make() {
		return literal("epoch")
				       .requires((stack) -> stack.hasPermission(4))
				       .then(literal("deadlock").executes(EpochCommands::doDeadlockCommand))
				       .then(RestartCommand.make())
				       .then(DataCommands.make());
	}
	
	private static int doDeadlockCommand(final CommandContext<CommandSourceStack> ctx) {
		DeadlockDetector.INSTANCE.findDeadlock();
		return SINGLE_SUCCESS;
	}
}
