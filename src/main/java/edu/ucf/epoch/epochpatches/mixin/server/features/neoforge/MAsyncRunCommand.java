package edu.ucf.epoch.epochpatches.mixin.server.features.neoforge;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import edu.ucf.epoch.epochpatches.mixinsupport.MixinData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.dedicated.DedicatedServer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.server.console.TerminalHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static edu.ucf.epoch.epochpatches.mixinsupport.MixinData.RUN_ASYNC_PREFIX;


@Mixin(targets = {"net.neoforged.neoforge.server.console.TerminalHandler", "net.minecraft.server.dedicated.DedicatedServer$1"})
abstract class MAsyncRunCommand_ParseCommand {
	
	/**
	 * Force `run_async` commands to run on THIS thread instead of being queued for the server to work on it on the main thread.
	 */
	@WrapOperation(
			method = {"run", "handleCommands"},
			at = @At(
					target = "Lnet/minecraft/server/dedicated/DedicatedServer;handleConsoleInput(Ljava/lang/String;Lnet/minecraft/commands/CommandSourceStack;)V",
					value = "INVOKE"
			)
	)
	private static void epoch$addRunAsync(DedicatedServer instance, String msg, CommandSourceStack source, Operation<Void> original) {
		if (msg.startsWith(RUN_ASYNC_PREFIX)) {
			Thread.startVirtualThread(() -> instance.getCommands().performPrefixedCommand(source, msg.substring(RUN_ASYNC_PREFIX.length())));
		} else {
			original.call(instance, msg, source);
		}
	}
}

@Mixin(Commands.class)
abstract class MAsyncRunCommand_NoEventOnAsyncThread {
	/**
	 * Don't post event if run on the commands thread.
	 */
	@WrapOperation(
			method="performCommand",
			at=@At(
					target="Lnet/neoforged/bus/api/IEventBus;post(Lnet/neoforged/bus/api/Event;)Lnet/neoforged/bus/api/Event;",
					value="INVOKE"
			)
	)
	private Event epoch$noPostEventOnTerminalThread(IEventBus instance, Event evt, Operation<Event> original) {
		if (!Thread.currentThread().isVirtual())
			return original.call(instance, evt);
		
		return evt;
	}
}

