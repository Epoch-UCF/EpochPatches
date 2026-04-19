package edu.ucf.epoch.epochpatches.commands.subcommands;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

public final class RestarterThread extends Thread {
	public int getSeconds() {
		return seconds;
	}
	
	private int seconds;
	private final MinecraftServer server;
	
	public RestarterThread(final int seconds, final MinecraftServer server) {
		super("Epoch Scheduled Restart Countdown");
		this.seconds = seconds + (30 - (seconds % 30)); // round up to multiple of 30
		this.server = server;
		
		setDaemon(true);
		setPriority(Thread.MIN_PRIORITY);
	}
	
	@Override
	public void run() {
		printWarning(seconds, true);
		
		try {
			Thread.sleep(30000);
			seconds -= 30;
		} catch (InterruptedException e) {
			return;
		}
		
		while (seconds > 0) {
			if (this.isInterrupted())
				return;
			
			printWarning(seconds, false);
			try {
				//noinspection BusyWait
				Thread.sleep(30000);
				seconds -= 30;
			} catch (InterruptedException e) {
				return;
			}
		}
		
		if (this.isInterrupted())
			return;
		
		server.sendSystemMessage(Component.literal("Server restarting."));
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException ignored) {
		} finally {
			server.halt(true);
		}
	}
	
	/**
	 * Give warning every 5 minutes, except for last 2 minutes and last 30 seconds.
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