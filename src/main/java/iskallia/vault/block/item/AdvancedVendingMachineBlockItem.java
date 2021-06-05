package iskallia.vault.block.item;

import iskallia.vault.init.ModItems;
import iskallia.vault.util.nbt.NBTSerializer;
import iskallia.vault.vending.TraderCore;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class AdvancedVendingMachineBlockItem extends BlockItem {

    public AdvancedVendingMachineBlockItem(Block block) {
        super(block, new Properties()
                .group(ModItems.VAULT_MOD_GROUP)
                .maxStackSize(64));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = stack.getTag();

        if (nbt != null) {
            CompoundNBT blockEntityTag = nbt.getCompound("BlockEntityTag");
            ListNBT cores = blockEntityTag.getList("coresList", Constants.NBT.TAG_COMPOUND);
            for (INBT tag : cores) {
                TraderCore core;
                try {
                    core = NBTSerializer.deserialize(TraderCore.class, (CompoundNBT) tag);
                    TranslationTextComponent text = new TranslationTextComponent("tip.the_vault.vending_vendor", core.getName());
                    text.setStyle(Style.EMPTY.setColor(Color.fromInt(0xFF_ff9966)));
                    tooltip.add(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
