package iskallia.vault.recipe;

import iskallia.vault.init.ModItems;
import iskallia.vault.item.RelicPartItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class UnidentifiedRelicRecipe extends SpecialRecipe {

    public UnidentifiedRelicRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        RelicPartItem relic = null;
        int diamondCount = 0;

        for (int i = 0; i < inv.getContainerSize(); ++ i) {
            ItemStack stack = inv.getItem(i);

            if (stack.getItem() == ModItems.VAULT_DIAMOND) {
                if (diamondCount++ == 8) return false;
            } else if (stack.getItem() instanceof RelicPartItem) {
                if (relic != null) return false;
                relic = (RelicPartItem) stack.getItem();
            } else {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(CraftingInventory inv) {
        return new ItemStack(ModItems.UNIDENTIFIED_RELIC);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 9;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return null;
        //        return ModRecipes.Serializer.CRAFTING_SPECIAL_UNIDENTIFIED_RELIC;
    }

}