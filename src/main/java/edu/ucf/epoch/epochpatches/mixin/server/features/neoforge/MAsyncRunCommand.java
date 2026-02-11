package edu.ucf.epoch.epochpatches.mixin.server.features.neoforge;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import edu.ucf.epoch.epochpatches.EpochPatchesMod;
import edu.ucf.epoch.epochpatches.mixinsupport.MixinData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.server.console.TerminalHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

@Mixin(value = TerminalHandler.class, remap = false, priority=2000)
abstract class MAsyncRunCommand {
	/**
	 * Force `run_async` commands to run on THIS thread instead of being queued for the server to work on it on the main thread.
	 */
	@WrapOperation(
			method = "handleCommands",
			at = @At(
					target = "Lnet/minecraft/server/dedicated/DedicatedServer;handleConsoleInput(Ljava/lang/String;Lnet/minecraft/commands/CommandSourceStack;)V",
					value = "INVOKE"
			)
	)
	private static void epoch$addRunAsync(DedicatedServer instance, String msg, CommandSourceStack source, Operation<Void> original) {
		if (msg.startsWith("run_async ")) {
			instance.getCommands().performPrefixedCommand(source, msg.substring(10));
		} else {
			original.call(instance, msg, source);
		}
	}
}

@Mixin(targets = "net.minecraft.server.dedicated.DedicatedServer$1", priority=2000)
abstract class MAsyncRunCommand_DedicatedServerToo {
	/**
	 * Force `run_async` commands to run on THIS thread instead of being queued for the server to work on it on the main thread.
	 */
	@WrapOperation(
			method = "run",
			at = @At(
					target = "Lnet/minecraft/server/dedicated/DedicatedServer;handleConsoleInput(Ljava/lang/String;Lnet/minecraft/commands/CommandSourceStack;)V",
					value = "INVOKE"
			)
	)
	private static void epoch$addRunAsync(DedicatedServer instance, String msg, CommandSourceStack source, Operation<Void> original) {
		if (msg.startsWith("run_async ")) {
			instance.getCommands().performPrefixedCommand(source, msg.substring(10));
		} else {
			original.call(instance, msg, source);
		}
	}
}

@Mixin(targets="net/minecraft/server/dedicated/DedicatedServer$1")
abstract class MAsyncRunCommand_CaptureTerminalThread {
	@Inject(
			method="<init>",
			at=@At("RETURN")
	)
	private void epoch$captureTerminalThread(DedicatedServer this$0, String arg0, CallbackInfo ci) {
		MixinData.TERMINAL_THREAD = ((Thread)(Object)this);
	}
}

@Mixin(Commands.class)
abstract class MAsyncRunCommand_NoEventOnTerminalThread {
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
		if (Thread.currentThread() != MixinData.TERMINAL_THREAD)
			return original.call(instance, evt);
		
		return evt;
	}
}

