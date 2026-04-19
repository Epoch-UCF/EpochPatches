package edu.ucf.epoch.epochpatches.util;

import edu.ucf.epoch.epochpatches.EpochPatchesMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Optional;

public final class NameGetUtils {
	@SuppressWarnings("unchecked")
	public static <T extends RecipeInput> Optional<ResourceLocation> getNameForRecipe(Recipe<T> recipe) {
		return EpochPatchesMod.server.getRecipeManager()
		                             .getAllRecipesFor(((RecipeType<Recipe<T>>) recipe.getType()))
		                             .parallelStream()
		                             .filter(it -> it.value().equals(recipe))
		                             .findFirst()
		                             .map(RecipeHolder::id);
	}
}
