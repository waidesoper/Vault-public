package iskallia.vault.block.item;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.StatueType;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class LootStatueBlockItem extends BlockItem {
    public LootStatueBlockItem(Block block) {
        super(block, new Properties()
            .tab(ModItems.VAULT_MOD_GROUP)
            .stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = stack.getTag();

        if (nbt != null) {
            CompoundNBT blockEntityTag = nbt.getCompound("BlockEntityTag");
            String nickname = blockEntityTag.getString("PlayerNickname");

            StringTextComponent text = new StringTextComponent(" Nickname: " + nickname);
            text.setStyle(Style.EMPTY.withColor(Color.fromRgb(0xFF_ff9966)));
            tooltip.add(text);
        }

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public static ItemStack forVaultBoss(String nickname, int variant, boolean hasCrown) {
        return getStatueBlockItem(nickname, StatueType.values()[variant], hasCrown, false);
    }

    public static ItemStack forArenaChampion(String nickname, int variant, boolean hasCrown) {
        return getStatueBlockItem(nickname, StatueType.values()[variant], hasCrown, false);
    }

    public static ItemStack forGift(String nickname, int variant, boolean hasCrown) {
        return getStatueBlockItem(nickname, StatueType.values()[variant], hasCrown, false);
    }

    public static ItemStack getStatueBlockItem(String nickname, StatueType type, boolean hasCrown, boolean blankStatue) {
        ItemStack itemStack = ItemStack.EMPTY;

        switch (type) {
            case GIFT_NORMAL:
                itemStack = new ItemStack(ModBlocks.GIFT_NORMAL_STATUE);
                break;

            case GIFT_MEGA:
                itemStack = new ItemStack(ModBlocks.GIFT_MEGA_STATUE);
                break;

            case VAULT_BOSS:
                itemStack = new ItemStack(ModBlocks.VAULT_PLAYER_LOOT_STATUE);
                break;

            case ARENA_CHAMPION:
                itemStack = new ItemStack(ModBlocks.ARENA_PLAYER_LOOT_STATUE);
                break;
        }

        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("PlayerNickname", nickname);
        nbt.putInt("StatueType", type.ordinal());
        nbt.putInt("Interval", ModConfigs.STATUE_LOOT.getInterval(type));
        ItemStack loot;
        if (blankStatue)
            loot = ModConfigs.STATUE_LOOT.getLoot();
        else
            loot = ModConfigs.STATUE_LOOT.randomLoot(type);
        nbt.put("LootItem", loot.serializeNBT());
        nbt.putBoolean("HasCrown", hasCrown);

        CompoundNBT stackNBT = new CompoundNBT();
        stackNBT.put("BlockEntityTag", nbt);
        itemStack.setTag(stackNBT);

        return itemStack;
    }
}
