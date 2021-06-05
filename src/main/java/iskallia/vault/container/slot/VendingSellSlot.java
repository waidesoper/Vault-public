package iskallia.vault.container.slot;

import iskallia.vault.init.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class VendingSellSlot extends Slot {

    public VendingSellSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    // #Crimson_Fluff, make a sound when bought item
    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        thePlayer.playSound(ModSounds.VENDING_MACHINE_SFX, SoundCategory.PLAYERS, 1f, 1f);
        return stack;
    }
}
