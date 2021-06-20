package iskallia.vault.container;

import iskallia.vault.init.ModContainers;
import iskallia.vault.init.ModRecipes;
import iskallia.vault.recipe.KeyPressRecipe;
import iskallia.vault.util.EntityHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;


/**
 * This is a Key Press Container that creates vault door keys.
 */
public class KeyPressContainer extends Container
{
    /**
     * World where container operates
     */
    private final World world;

    /**
     * List of available recipes.
     */
    private final List<KeyPressRecipe> keyPressRecipeList;

    /**
     * Current recipe.
     */
    private KeyPressRecipe keyPressRecipe;

    /**
     * Stores crafting result
     */
    private final CraftResultInventory craftResultInventory = new CraftResultInventory();

    /**
     * Inventory with 2 slots for key press
     */
    protected final IInventory keyPressInventory = new Inventory(2)
    {
        /**
         * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        public void markDirty()
        {
            super.setChanged();     // #Crimson_Fluff, was setDirty
            KeyPressContainer.this.onCraftMatrixChanged(this);
        }
    };

    /**
     * Slot for blank keys
     */
    public static final int KEY_SLOT = 0;

    /**
     * Slot for cluster
     */
    public static final int CLUSTER_SLOT = 1;

    /**
     * Result key
     */
    public static final int RESULT_SLOT = 2;


// ---------------------------------------------------------------------
// Section: Constructor
// ---------------------------------------------------------------------


    /**
     * Constructor for the container.
     *
     * @param windowId WindowId.
     * @param player Player instance.
     */
    public KeyPressContainer(int windowId, PlayerEntity player)
    {
        super(ModContainers.KEY_PRESS_CONTAINER, windowId);

        this.world = player.level;
        this.keyPressRecipeList = this.world.getRecipeManager().getAllRecipesFor(ModRecipes.KEY_PRESS_RECIPE);

        this.addSlot(new Slot(this.keyPressInventory, KEY_SLOT, 27, 47));
        this.addSlot(new Slot(this.keyPressInventory, CLUSTER_SLOT, 76, 47));
        this.addSlot(new Slot(this.craftResultInventory, RESULT_SLOT, 134, 47)
        {

            /**
             * Return whether this slot's stack can be taken from this slot.
             */
//            @Override
//            public boolean canTakeStack(PlayerEntity playerIn)
//            {
//                return KeyPressContainer.this.canTakeStack(playerIn, this.hasItem());
//            }


            @Override
            public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack)
            {
                return KeyPressContainer.this.onTake(thePlayer, stack);
            }


            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return false; // Do not accept any item in
            }
        });

        // Manages player inventory
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlot(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        // Manages player toolbar
        for (int k = 0; k < 9; ++k)
        {
            this.addSlot(new Slot(player.inventory, k, 8 + k * 18, 142));
        }
    }


// ---------------------------------------------------------------------
// Section: Methods
// ---------------------------------------------------------------------


    /**
     * This method checks if player can take stacks.
     *
     * @param playerIn PlayerEntity.
     * @param hasStack boolean that indicate if it is stack.
     * @return {@code true} if stack can be taken, {@code false} otherwise.
     */
    private boolean canTakeStack(PlayerEntity playerIn, boolean hasStack)
    {
        return this.keyPressRecipe != null && this.keyPressRecipe.matches(this.keyPressInventory, this.world);
    }


    /**
     * This method process recipe processing.
     *
     * @param player Player Entity.
     * @param craftedItem Crafted item stack.
     * @return Crafted ItemStack.
     */
    private ItemStack onTake(PlayerEntity player, ItemStack craftedItem)
    {
        craftedItem.onCraftedBy(player.level, player, craftedItem.getCount());
        this.craftResultInventory.startOpen(player);        // TODO: #Crimson_Fluff, was onCrafting
        this.reduceItemInSlot(KEY_SLOT);
        this.reduceItemInSlot(CLUSTER_SLOT);

        if (!player.level.isClientSide && !craftedItem.isEmpty())
        {
            player.level.globalLevelEvent(1030, player.blockPosition(), 0);
        }

        return craftedItem;
    }


    /**
     * This method shrinks item stack in given slot by 1.
     *
     * @param slot Index of the slot.
     */
    private void reduceItemInSlot(int slot)
    {
        ItemStack itemstack = this.keyPressInventory.getItem(slot);
        itemstack.shrink(1);
        this.keyPressInventory.setItem(slot, itemstack);
    }


    /**
     * Called when the Key Press Input Slot changes, calculates the new result and puts it in the output slot
     */
    public void updateKeyPressOutput()
    {
        List<KeyPressRecipe> recipeList = this.world.getRecipeManager().getRecipesFor(
            ModRecipes.KEY_PRESS_RECIPE,
            this.keyPressInventory,
            this.world);

        if (recipeList.isEmpty())
        {
            this.craftResultInventory.setItem(0, ItemStack.EMPTY);
        }
        else
        {
            this.keyPressRecipe = recipeList.get(0);
            ItemStack itemstack = this.keyPressRecipe.assemble(this.keyPressInventory);
            this.craftResultInventory.setRecipeUsed(this.keyPressRecipe);
            this.craftResultInventory.setItem(0, itemstack);
        }
    }


    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        //super.onCraftMatrixChanged(inventoryIn);      // TODO: #Crimson_Fluff

        if (inventoryIn == this.keyPressInventory)
        {
            this.updateKeyPressOutput();
        }
    }


    /**
     * Return items in players inventory.
     *
     * @param player Player who closed inventory.
     */
    @Override
    public void removed(PlayerEntity player)
    {
        super.removed(player);

        ItemStack keyStack = this.keyPressInventory.getItem(KEY_SLOT);
        ItemStack clusterStack = this.keyPressInventory.getItem(CLUSTER_SLOT);

        if (!keyStack.isEmpty() || !clusterStack.isEmpty())     // #Crimson_Fluff, Issue 361, closing anvil with items in deletes the items
        {
            EntityHelper.giveItem(player, keyStack);
            EntityHelper.giveItem(player, clusterStack);
        }
    }


    /**
     * Transfers items from with shift+click.
     */
    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem())
        {
            ItemStack itemStackInSlot = slot.getItem();
            itemstack = itemStackInSlot.copy();

            if (index == RESULT_SLOT)
            {
                if (!this.moveItemStackTo(itemStackInSlot, 3, 39, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStackInSlot, itemstack);
            }
            else if (index != KEY_SLOT && index != CLUSTER_SLOT)
            {
                if (index < 39)
                {
                    int mergeIndex = this.getMergeSlotIndex(itemstack);

                    if (mergeIndex == -1 ||
                        !this.moveItemStackTo(itemStackInSlot, mergeIndex, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if (!this.moveItemStackTo(itemStackInSlot, 3, 39, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemStackInSlot.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (itemStackInSlot.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStackInSlot);
        }

        return itemstack;
    }


    /**
     * This method returns merge slot index for given item stack.
     *
     * @param itemStack Item Stack that must be merged.
     * @return slot index.
     */
    private int getMergeSlotIndex(ItemStack itemStack)
    {
        for (KeyPressRecipe recipe : this.keyPressRecipeList)
        {
            if (recipe.isValidBlankKey(itemStack))
            {
                // return key slot for main item
                return KEY_SLOT;
            }
            if (recipe.isValidCluster(itemStack))
            {
                // return cluster slot for cluster
                return CLUSTER_SLOT;
            }
        }

        // Return -1 by default.
        return -1;
    }


    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean stillValid(PlayerEntity player)
    {
        return true;
    }
}