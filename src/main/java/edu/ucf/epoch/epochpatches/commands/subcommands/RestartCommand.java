package edu.ucf.epoch.epochpatches.commands.subcommands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;

public final class RestartCommand {
	private static final String ARG_MINUTES = "minutes_until_restart";
	private static final String ARG_SECONDS = "seconds_until_restart";
	
	public static LiteralArgumentBuilder<CommandSourceStack> make() {
		return literal("restart")
				       .then(literal("minutes")
						             .then(argument(ARG_MINUTES, IntegerArgumentType.integer(0))
								                   .executes(RestartCommand::queueRestart)))
				       .then(literal("seconds")
						             .then(argument(ARG_SECONDS, IntegerArgumentType.integer(0))
								                   .executes(RestartCommand::queueRestart)))
				       .then(literal("now").executes(ctx -> {
						   ctx.getSource().getServer().halt(true);
						   return 1;
				       }));
	}
	
	private static int queueRestart(final CommandContext<CommandSourceStack> ctx) {
		int secondsTillRestart;
		try {
			secondsTillRestart = IntegerArgumentType.getInteger(ctx, ARG_MINUTES) * 60;
		} catch (Exception e) {
			secondsTillRestart = IntegerArgumentType.getInteger(ctx, ARG_SECONDS);
		}
		
		final var thread = new RestarterThread(secondsTillRestart, ctx.getSource().getServer());
		thread.setDaemon(true);
		thread.start();
		
		ctx.getSource().sendSuccess(() -> Component.literal("Scheduled restart."), true);
		
		return SINGLE_SUCCESS;
	}
	
	
	private static final class RestarterThread extends Thread {
		private final int seconds;
		private final MinecraftServer server;
		
		public RestarterThread(final int seconds, final MinecraftServer server) {
			this.seconds = seconds + (30 - (seconds % 30)); // round up to multiple of 30
			this.server = server;
		}
		
		@Override
		public void run() {
			int secondsRemaining = this.seconds;
			
			printWarning(secondsRemaining, true);
			
			try {
				Thread.sleep(30000);
			} catch (InterruptedException ignored) {}
			
			secondsRemaining -= 30;
			
			
			while (secondsRemaining > 0) {
				printWarning(secondsRemaining, false);
				try {
					Thread.sleep(30000);
				} catch (InterruptedException ignored) {}
				
				secondsRemaining -= 30;
			}
			
			server.halt(true);
		}
		
		/**
		 * Give warning every 5 minutes, except for last 2 minutes and 30 seconds.
		 */
		private void printWarning(final int secondsRemaining, final boolean forceMessage) {
			final String warning;
			switch (secondsRemaining) {
				case 30:
					warning = "30 seconds";
					break;
				case 60:
					warning = "1 minute";
					break;
				case 120:
					warning = "2 minutes";
					break;
				default:
					if (!forceMessage && secondsRemaining % (60 * 5) != 0) {
						return;
					}
					warning = (secondsRemaining / 60) + " minutes";
			}
			
			server.sendSystemMessage(Component.literal("Server restart in " + warning + "."));
		}
	}
}