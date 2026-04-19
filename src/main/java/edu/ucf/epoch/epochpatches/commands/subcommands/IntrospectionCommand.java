package edu.ucf.epoch.epochpatches.commands.subcommands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;

public final class IntrospectionCommand {
	public static LiteralArgumentBuilder<CommandSourceStack> make() {
		return literal("introspection")
				       .then(literal("exists")
						             .then(argument("class_name", StringArgumentType.string())
								                   .executes(IntrospectionCommand::runCommand)));
	}
	
	private static int runCommand(CommandContext<CommandSourceStack> ctx) {
		String className = StringArgumentType.getString(ctx, "class_name");
		try {
			Class.forName(className);
			ctx.getSource().sendSuccess(() -> Component.literal("Class ").append(className).append(" exists."), true);
			return 1;
		} catch (Throwable e) {
			ctx.getSource().sendFailure(Component.literal("No class '").append(className).append("' found."));
			return 0;
		}
	}
	
	private IntrospectionCommand() {}
}
