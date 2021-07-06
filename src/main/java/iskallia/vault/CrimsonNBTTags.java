package iskallia.vault;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.Collection;

public class CrimsonNBTTags {
    private static final long WINDOW = Minecraft.getInstance().getWindow().getWindow();

    @OnlyIn(Dist.CLIENT)
    private static boolean isHoldingShift() { return InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_SHIFT) || InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_SHIFT); }

    @OnlyIn(Dist.CLIENT)
    private static boolean isHoldingCtrl() { return InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_CONTROL) || InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_CONTROL); }

    public static void showNBTandInfo(ItemTooltipEvent event) {
        ItemStack current = event.getItemStack();
        if (current.isEmpty()) return;

        if (current.hasTag()) {
//            if (current.getMaxDamage() != 0 && current.getDamageValue() == 0)
//                event.getToolTip().add(new TranslationTextComponent("tip." + CrimsonNBT.MOD_ID + ".durability").append(": " + current.getMaxDamage()).withStyle(TextFormatting.DARK_GRAY));

            if (isHoldingCtrl()) {
                String st = current.getTag().toString();
                int l = 300;

                if (st.length() > l) {
                    event.getToolTip().add(new StringTextComponent(st.substring(0, l)).withStyle(TextFormatting.DARK_GRAY));
                    event.getToolTip().add(new StringTextComponent((st.length() - l) + " ").append(new TranslationTextComponent("tip." + Vault.MOD_ID + ".more")).withStyle(TextFormatting.DARK_GRAY));

                } else event.getToolTip().add(new StringTextComponent(st).withStyle(TextFormatting.DARK_GRAY));

            } else
                event.getToolTip().add(new TranslationTextComponent("tip." + Vault.MOD_ID + ".ctrl", new TranslationTextComponent("tip.ctrl").withStyle(TextFormatting.YELLOW)).withStyle(TextFormatting.GRAY));
        }

        Collection<ResourceLocation> iTag = ItemTags.getAllTags().getMatchingTags(current.getItem());
        if (isHoldingShift()) {
            if (iTag.size() > 0) {
                event.getToolTip().add(new TranslationTextComponent("tip." + Vault.MOD_ID + ".item_tags").withStyle(TextFormatting.GRAY));

                for (ResourceLocation tag : iTag)
                    event.getToolTip().add(new StringTextComponent("  #" + tag).withStyle(TextFormatting.DARK_GRAY));
            }

            iTag = BlockTags.getAllTags().getMatchingTags(Block.byItem(current.getItem()));
            if (iTag.size() > 0) {
                event.getToolTip().add(new TranslationTextComponent("tip." + Vault.MOD_ID + ".block_tags").withStyle(TextFormatting.GRAY));

                for (ResourceLocation tag : iTag)
                    event.getToolTip().add(new StringTextComponent("  #" + tag).withStyle(TextFormatting.DARK_GRAY));
            }

        } else {
            if (iTag.size() == 0) iTag = BlockTags.getAllTags().getMatchingTags(Block.byItem(current.getItem()));

            if (iTag.size() > 0)
                event.getToolTip().add(new TranslationTextComponent("tip." + Vault.MOD_ID + ".shift", new TranslationTextComponent("tip.shift").withStyle(TextFormatting.YELLOW)).withStyle(TextFormatting.GRAY));
        }

        // NOTE: Waila adds mod name to tooltip
        if (! ModList.get().isLoaded("waila")) {
            String modName = getModName(current);
            if (modName != null)
                if (! event.getToolTip().get(event.getToolTip().size() - 1).getString().equals(modName))
                    event.getToolTip().add(new StringTextComponent(modName).withStyle(TextFormatting.BLUE));
        }
    }

    @Nullable
    private static String getModName(ItemStack itemStack) {
        String modName = itemStack.getItem().getCreatorModId(itemStack);

        if (modName != null)
            return ModList.get().getModContainerById(modName)
            .map(modContainer -> modContainer.getModInfo().getDisplayName())
            .orElse(StringUtils.capitalize(modName));

        return null;
    }
}
