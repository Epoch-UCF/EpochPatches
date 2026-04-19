package edu.ucf.epoch.epochpatches.commands.subcommands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;

public final class RestartCommand {
	private static final String ARG_MINUTES = "minutes_until_restart";
	private static final String ARG_SECONDS = "seconds_until_restart";
	
	private static RestarterThread currentRestarterThread;
	
	public static LiteralArgumentBuilder<CommandSourceStack> make() {
		return literal("restart")
				       .then(literal("minutes")
						             .then(argument(ARG_MINUTES, IntegerArgumentType.integer(0))
								                   .executes(ctx -> queueRestart(IntegerArgumentType.getInteger(ctx, ARG_MINUTES) * 60, ctx.getSource()))))
				       .then(literal("seconds")
						             .then(argument(ARG_SECONDS, IntegerArgumentType.integer(0))
								                   .executes(ctx -> queueRestart(IntegerArgumentType.getInteger(ctx, ARG_SECONDS), ctx.getSource()))))
				       .then(literal("now")
						             .executes(RestartCommand::restartImmediately))
				       .then(literal("cancel")
						             .executes(RestartCommand::cancelRestart));
	}
	
	private static int queueRestart(final int secondsTillRestart, final CommandSourceStack sender) {
		if (currentRestarterThread != null) {
			sender.sendFailure(Component.literal("Restart already in progress. Time remaining: ")
			                            .append(String.valueOf(currentRestarterThread.getSeconds()))
			                            .append(" seconds."));
			return 0;
		}
		
		currentRestarterThread = new RestarterThread(secondsTillRestart, sender.getServer());
		
		currentRestarterThread.start();
		
		sender.sendSuccess(() -> Component.literal("Scheduled restart."), true);
		
		return SINGLE_SUCCESS;
	}
	
	private static int restartImmediately(final CommandContext<CommandSourceStack> ctx) {
		if (currentRestarterThread != null) {
			currentRestarterThread.interrupt();
			currentRestarterThread = null;
		}
		
		final var server = ctx.getSource().getServer();
		
		final var thread = new Thread(() -> {
			server.sendSystemMessage(Component.literal("Server restarting in 10 seconds."));
			try {
				Thread.sleep(10000);
			} catch (InterruptedException ignored) {}
			server.halt(true);
		}, "Epoch Restart Countdown");
		thread.setDaemon(true);
		thread.start();
		return SINGLE_SUCCESS;
	}
	
	private static int cancelRestart(final CommandContext<CommandSourceStack> ctx) {
		if (currentRestarterThread == null) {
			ctx.getSource().sendFailure(Component.literal("No restart scheduled."));
			return 0;
		}
		currentRestarterThread.interrupt();
		currentRestarterThread = null;
		ctx.getSource().sendSystemMessage(Component.literal("Restart cancelled."));
		return SINGLE_SUCCESS;
	}
}