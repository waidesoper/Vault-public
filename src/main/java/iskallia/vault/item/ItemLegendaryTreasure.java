package iskallia.vault.item;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.VaultRarity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLegendaryTreasure extends Item {

    private VaultRarity vaultRarity;

    public ItemLegendaryTreasure(ItemGroup group, ResourceLocation id, VaultRarity vaultRarity) {
        super(new Properties()
            .tab(group)
            .stacksTo(1));

        this.setRegistryName(id);
        this.vaultRarity = vaultRarity;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (worldIn.isClientSide) return super.use(worldIn, playerIn, handIn);
        if (handIn != Hand.MAIN_HAND) return super.use(worldIn, playerIn, handIn);
        ItemStack stack = playerIn.getMainHandItem();
        if (stack.getItem() instanceof ItemLegendaryTreasure) {
            ItemLegendaryTreasure item = (ItemLegendaryTreasure) stack.getItem();
            ItemStack toDrop = ItemStack.EMPTY;
            switch (item.getRarity()) {
                case COMMON:
                    toDrop = ModConfigs.LEGENDARY_TREASURE_NORMAL.getRandom();
                    break;
                case RARE:
                    toDrop = ModConfigs.LEGENDARY_TREASURE_RARE.getRandom();
                    break;
                case EPIC:
                    toDrop = ModConfigs.LEGENDARY_TREASURE_EPIC.getRandom();
                    break;
                case OMEGA:
                    toDrop = ModConfigs.LEGENDARY_TREASURE_OMEGA.getRandom();
                    break;
            }
            playerIn.drop(toDrop, false);
            ItemRelicBoosterPack.successEffectsAsItem(worldIn, playerIn.position(), stack);     // #Crimson_Fluff
            stack.shrink(1);
        }

        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getItem() instanceof ItemLegendaryTreasure) {
            ItemLegendaryTreasure item = (ItemLegendaryTreasure) stack.getItem();
            tooltip.add(new TranslationTextComponent("tip.the_vault.artifact_identify").withStyle(TextFormatting.GOLD));
            tooltip.add(new TranslationTextComponent("tip.the_vault.rarity", new StringTextComponent(item.getRarity().name()).withStyle(item.getRarity().color)));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ITextComponent getName(ItemStack stack) {
        if (stack.getItem() instanceof ItemLegendaryTreasure) {
            ItemLegendaryTreasure item = (ItemLegendaryTreasure) stack.getItem();
            return new TranslationTextComponent("item.the_vault.legendary_treasure").withStyle(item.getRarity().color);
        }
        return super.getName(stack);
    }

    public VaultRarity getRarity() {
        return vaultRarity;
    }


}
