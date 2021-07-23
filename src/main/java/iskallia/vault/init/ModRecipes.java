package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.recipe.KeyPressRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;

public class ModRecipes {
    public static final IRecipeType<KeyPressRecipe> KEY_PRESS_RECIPE = new IRecipeType<KeyPressRecipe>() {
        @Override
        public String toString() { return Vault.id("key_press_recipe").toString(); }
    };

    public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(KEY_PRESS_RECIPE.toString()), KEY_PRESS_RECIPE);
        event.getRegistry().register(KeyPressRecipe.SERIALIZER);
    }
}
