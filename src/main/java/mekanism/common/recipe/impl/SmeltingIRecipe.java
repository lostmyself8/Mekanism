package mekanism.common.recipe.impl;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.basic.BasicItemStackToItemStackRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismRecipeSerializers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

@NothingNullByDefault
public class SmeltingIRecipe extends BasicItemStackToItemStackRecipe implements ItemStackOutputInternal {

    public SmeltingIRecipe(ItemStackIngredient input, ItemStack output) {
        super(input, output);
    }

    @Override
    public RecipeType<ItemStackToItemStackRecipe> getType() {
        return MekanismRecipeType.SMELTING.get();
    }

    @Override
    public RecipeSerializer<SmeltingIRecipe> getSerializer() {
        return MekanismRecipeSerializers.SMELTING.get();
    }

    @Override
    public String getGroup() {
        return MekanismBlocks.ENERGIZED_SMELTER.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return MekanismBlocks.ENERGIZED_SMELTER.getItemStack();
    }

    @Override
    public ItemStack getOutputRaw() {
        return output;
    }
}