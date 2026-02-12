package edu.ucf.epoch.epochpatches.commands.subcommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import edu.ucf.epoch.epochpatches.mixinsupport.EpochMethodHandles;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Map;
import java.util.Optional;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class DataCommands {
	public static LiteralArgumentBuilder<CommandSourceStack> make() {
		var attachment_name_plus = argument("attachment_name", ResourceLocationArgument.id())
				                                 .suggests((context, builder) -> {
					                                 var attachments = dataAttachmentsForEntity(EntityArgument.getEntity(context, "target"));
					                                 for (var type : attachments.keySet()) {
						                                 builder.suggest(NeoForgeRegistries.ATTACHMENT_TYPES.getKey(type).toString());
					                                 }
					                                 return builder.buildFuture();
				                                 })
				                                 .executes(DataCommands::getData).build();
		return literal("data_attachments")
				       .then(literal("get").then(
						       literal("entity")
								       .then(argument("target", EntityArgument.entity())
										             .then(attachment_name_plus))
								       .then(attachment_name_plus)));
	}
	
	private static Map<AttachmentType<?>, Object> dataAttachmentsForEntity(Entity entity) {
		return EpochMethodHandles.AttachmentHolder$getAttachmentMap(entity);
	}
	
	private static int getData(CommandContext<CommandSourceStack> ctx) {
		Entity target;
		try {
			target = EntityArgument.getEntity(ctx, "target");
		} catch (Exception e) {
			var playerExecuting = ctx.getSource().getPlayer();
			if (playerExecuting == null) {
				ctx.getSource().sendFailure(Component.literal("Console users must use the target selector."));
				return 0;
			}
			Optional<Entity> targetedEntity = DebugRenderer.getTargetedEntity(playerExecuting, 10);
			if (targetedEntity.isEmpty()) {
				ctx.getSource().sendFailure(Component.literal("No entity being looked at."));
				return 0;
			}
			target = targetedEntity.get();
		}
		
		
		var attachment_name = ResourceLocationArgument.getId(ctx, "attachment_name");
		var actualAttachment = NeoForgeRegistries.ATTACHMENT_TYPES.get(attachment_name);
		if (actualAttachment == null) {
			ctx.getSource().sendFailure(Component.literal("No data attachment exists for id '").append(attachment_name.toString()).append("'."));
			return 0;
		}
		
		Optional<?> data = target.getExistingData(actualAttachment);
		if (data.isEmpty()) {
			ctx.getSource().sendFailure(Component.literal("Target does not contain data for attachment type '").append(attachment_name.toString()).append("'."));
			return 0;
		}
		
		IAttachmentSerializer<Tag, Object> serializer = EpochMethodHandles.AttachmentType$serializer(actualAttachment);
		String stringForm;
		if (serializer != null) {
			stringForm = serializer.write(data.get(), target.registryAccess()).toString();
		} else {
			stringForm = data.get().toString();
		}
		
		ctx.getSource().sendSuccess(() -> Component.literal(stringForm), true);
		return 1;
	}
	
	private DataCommands() {}
}
