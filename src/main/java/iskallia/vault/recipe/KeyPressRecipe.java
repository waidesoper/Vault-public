package iskallia.vault.recipe;

import com.google.gson.JsonObject;
import iskallia.vault.container.KeyPressContainer;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;


/**
 * Key Press Recipes class.
 */
public class KeyPressRecipe implements IRecipe<IInventory>
{
    /**
     * Class serializer.
     */
    public static final Serializer SERIALIZER = new Serializer();

    /**
     * Key slot ingredient.
     */
    private final Ingredient key;

    /**
     * Cluster slot ingredient.
     */
    private final Ingredient cluster;

    /**
     * Result from crafting.
     */
    private final ItemStack result;

    /**
     * Recipe resource ID.
     */
    private final ResourceLocation recipeId;


    // ---------------------------------------------------------------------
    // Section: Constructor
    // ---------------------------------------------------------------------


    /**
     * Default key recipe constructor.
     * @param recipeId Resource ID.
     * @param key Key Ingredient
     * @param cluster Cluster Ingredient
     * @param result ItemStuck from recipe.
     */
    public KeyPressRecipe(ResourceLocation recipeId, Ingredient key, Ingredient cluster, ItemStack result)
    {
        this.recipeId = recipeId;
        this.key = key;
        this.cluster = cluster;
        this.result = result;
    }


    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(IInventory inv, World worldIn)
    {
        return this.key.test(inv.getItem(KeyPressContainer.KEY_SLOT)) &&
            this.cluster.test(inv.getItem(KeyPressContainer.CLUSTER_SLOT));
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        ItemStack itemstack = this.result.copy();
        CompoundNBT compoundnbt = inv.getItem(KeyPressContainer.RESULT_SLOT).getTag();
        if (compoundnbt != null)
        {
            itemstack.setTag(compoundnbt.copy());
        }

        return itemstack;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }


    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }


    /**
     * This method returns if given item is valid for cluster ingredient.
     * @param itemStack ItemStack that must be checked.
     * @return {@code true} if given item is valid for cluster recipe, {@code false} otherwise.
     */
    public boolean isValidCluster(ItemStack itemStack)
    {
        return this.cluster.test(itemStack);
    }

    /**
     * This method returns if given item is valid for key ingredient.
     * @param itemStack ItemStack that must be checked.
     * @return {@code true} if given item is valid for key recipe, {@code false} otherwise.
     */
    public boolean isValidBlankKey(ItemStack itemStack)
    {
        return this.key.test(itemStack);
    }


    /**
     * This method returns icon for recipe.
     * @return KeyPress icon.
     */
    public ItemStack getIcon()
    {
        return new ItemStack(ModBlocks.KEY_PRESS);
    }


    /**
     * This method returns recipe Id.
     */
    public ResourceLocation getId()
    {
        return this.recipeId;
    }


    /**
     * This method returns serializer.
     */
    public IRecipeSerializer<?> getSerializer()
    {
        return KeyPressRecipe.SERIALIZER;
    }


    /**
     * This method returns recipe type.
     */
    public IRecipeType<?> getType()
    {
        return SERIALIZER.getRecipeType();
    }


    /**
     * This method returns recipes ingredient list.
     */
    @Override
    public NonNullList<Ingredient> getIngredients()
    {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.key);
        list.add(this.cluster);
        return list;
    }


    // ---------------------------------------------------------------------
    // Section: Serializer
    // ---------------------------------------------------------------------


    /**
     * This class manages recipes serialization and deserialization.
     */
    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<KeyPressRecipe>
    {

        // This registry name is what people will specify in their json files.
        Serializer()
        {
            this.setRegistryName(ModRecipes.KEY_PRESS_RECIPE.toString());
        }


        public IRecipeType<?> getRecipeType()
        {
            return ModRecipes.KEY_PRESS_RECIPE;
        }

        public KeyPressRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient key = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "key"));
            Ingredient cluster = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "cluster"));
            ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            return new KeyPressRecipe(recipeId, key, cluster, result);
        }

        @Nullable
        public KeyPressRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient key = Ingredient.fromNetwork(buffer);
            Ingredient cluster = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new KeyPressRecipe(recipeId, key, cluster, result);
        }

        public void toNetwork(PacketBuffer buffer, KeyPressRecipe recipe) {
            recipe.key.toNetwork(buffer);
            recipe.cluster.toNetwork(buffer);
            buffer.writeItemStack(recipe.result, false);
        }
    }
}