package mekanism.api.recipes;

import java.util.List;
import java.util.function.BiPredicate;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Main Input: ItemStack
 * <br>
 * Secondary/Extra Input: ItemStack
 * <br>
 * Output: ItemStack
 *
 * @apiNote Combiners and Combining Factories can process this recipe type.
 */
@NothingNullByDefault
public abstract class CombinerRecipe extends MekanismRecipe implements BiPredicate<@NotNull ItemStack, @NotNull ItemStack> {

    @Override
    public abstract boolean test(ItemStack input, ItemStack extra);

    /**
     * Gets the main input ingredient.
     */
    public abstract ItemStackIngredient getMainInput();

    /**
     * Gets the secondary input ingredient.
     */
    public abstract ItemStackIngredient getExtraInput();

    /**
     * Gets a new output based on the given inputs.
     *
     * @param input Specific input.
     * @param extra Specific secondary/extra input.
     *
     * @return New output.
     *
     * @apiNote While Mekanism does not currently make use of the inputs, it is important to support it and pass the proper value in case any addons define input based
     * outputs where things like NBT may be different.
     * @implNote The passed in inputs should <strong>NOT</strong> be modified.
     */
    @Contract(value = "_, _ -> new", pure = true)
    public abstract ItemStack getOutput(@NotNull ItemStack input, @NotNull ItemStack extra);

    @NotNull
    @Override
    public abstract ItemStack getResultItem(@NotNull RegistryAccess registryAccess);

    /**
     * For JEI, gets the output representations to display.
     *
     * @return Representation of the output, <strong>MUST NOT</strong> be modified.
     */
    public abstract List<ItemStack> getOutputDefinition();

    @Override
    public boolean isIncomplete() {
        return getMainInput().hasNoMatchingInstances() || getExtraInput().hasNoMatchingInstances();
    }
}
