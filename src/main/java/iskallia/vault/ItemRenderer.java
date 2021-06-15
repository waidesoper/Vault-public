package iskallia.vault;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.init.ModAttributes;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.text.DecimalFormat;

public class ItemRenderer {
    public static void renderDurability(FontRenderer fr, ItemStack stack, int xPosition, int yPosition) {
        if (! ModAttributes.GEAR_MAX_LEVEL.exists(stack)) {
            return;
        }

        RenderSystem.disableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        float progress = (float) ModAttributes.GEAR_MAX_LEVEL.getOrDefault(stack, 1).getValue(stack);
        progress = (progress - ModAttributes.GEAR_LEVEL.getOrDefault(stack, 0.0F).getValue(stack)) / progress;
        progress = MathHelper.clamp(progress, 0.0F, 1.0F);

        if (progress != 0.0F && progress != 1.0F) {
            int i = Math.round(13.0F - progress * 13.0F);

            int j = MathHelper.hsvToRgb(Math.max(0.0F, 1.0F - progress) / 3.0F, 1.0F, 1.0F);
            //this.draw(bufferbuilder, xPosition + 2, yPosition + 15, 13, 2, 0, 0, 0, 255);
            //this.draw(bufferbuilder, xPosition + 2, yPosition + 15, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
        }

        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
    }
}
