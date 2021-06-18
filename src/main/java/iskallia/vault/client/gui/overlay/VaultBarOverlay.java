package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import iskallia.vault.client.gui.helper.AnimationTwoPhased;
import iskallia.vault.client.gui.helper.FontHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class VaultBarOverlay {

    public static final ResourceLocation RESOURCE = new ResourceLocation(Vault.MOD_ID, "textures/gui/vault-hud.png");

    public static int vaultLevel;
    public static int vaultExp, tnl;
    public static int unspentSkillPoints;
    public static int unspentKnowledgePoints;

    public static AnimationTwoPhased expGainedAnimation = new AnimationTwoPhased(0f, 1f, 0f, 500);
    public static long previousTick = System.currentTimeMillis();

    @SubscribeEvent
    public static void
    onPostRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR)
            return; // Render only on POTION_ICONS

        long now = System.currentTimeMillis();

        MatrixStack matrixStack = event.getMatrixStack();
        Minecraft minecraft = Minecraft.getInstance();
        int midX = minecraft.getWindow().getGuiScaledWidth() / 2;
        int bottom = minecraft.getWindow().getGuiScaledHeight();
        int right = minecraft.getWindow().getGuiScaledWidth() - 5;

        String text = String.valueOf(vaultLevel);
        int textX = midX + 51 - (minecraft.font.width(text) / 2);
        //int textY = bottom - 54;
        int barWidth = 85;
        float expPercentage = (float) vaultExp / tnl;

        // #Crimson_Fluff Common Code + Translations
        ClientPlayerEntity player = minecraft.player;
        boolean iconsShowing = player != null && player.getActiveEffects().stream().anyMatch(EffectInstance::showIcon);
        int yOffset = iconsShowing ? 28 : 18;
        //minecraft.getTextureManager().bindTexture(RESOURCE);

        if (VaultBarOverlay.unspentSkillPoints > 0) {
//            ClientPlayerEntity player = minecraft.player;
//            boolean iconsShowing = player != null && player.getActivePotionEffects().stream()
//                    .anyMatch(EffectInstance::isShowIcon);
//            minecraft.getTextureManager().bindTexture(RESOURCE);
            String unspentText = new TranslationTextComponent(unspentSkillPoints == 1
                ? "tip.the_vault.skill_unspent"
                : "tip.the_vault.skill_unspents").getString();
            String unspentPointsText = unspentSkillPoints + " ";
            int unspentPointsWidth = minecraft.font.width(unspentPointsText);
            int unspentWidth = minecraft.font.width(unspentText);
//            int gap = 5;
//            int yOffset = 18;
            minecraft.font.drawShadow(matrixStack, unspentPointsText,
                right - unspentWidth - unspentPointsWidth, yOffset,
                0xFF_ffd800
            );
            minecraft.font.drawShadow(matrixStack, unspentText,
                right - unspentWidth, yOffset,
                0xFF_ffffff
            );

            yOffset += minecraft.font.lineHeight + 3;      // #Crimson_Fluff, can't assume font height
                                                           // increase yOffset in case we have unspentKnowledgePoints
        }

        if (VaultBarOverlay.unspentKnowledgePoints > 0) {
//            ClientPlayerEntity player = minecraft.player;
//            boolean iconsShowing = player != null && player.getActivePotionEffects().stream()
//                    .anyMatch(EffectInstance::isShowIcon);
//            minecraft.getTextureManager().bindTexture(RESOURCE);
            String unspentText = new TranslationTextComponent(unspentKnowledgePoints == 1
                ? "tip.the_vault.res_unspent"
                : "tip.the_vault.res_unspents").getString();
            String unspentPointsText = unspentKnowledgePoints + " ";
            int unspentPointsWidth = minecraft.font.width(unspentPointsText);
            int unspentWidth = minecraft.font.width(unspentText);
//            int gap = 5;
//            int yOffset = 18;
//           matrixStack.pushPose();
//            if (VaultBarOverlay.unspentSkillPoints > 0) {
//                matrixStack.translate(0, 12, 0);
//            }
            minecraft.font.drawShadow(matrixStack, unspentPointsText,
                    right - unspentWidth - unspentPointsWidth, yOffset,
                    0xFF_40d7b1
            );
            minecraft.font.drawShadow(matrixStack, unspentText,
                    right - unspentWidth, yOffset,
                    0xFF_ffffff
            );
//            matrixStack.popPose();
        }

        expGainedAnimation.tick((int) (now - previousTick));
        previousTick = now;

        //minecraft.getProfiler().startSection("vaultBar");
        if (player.isCreative()) bottom += 20;  // VaultXPBar appear above hotbar

        minecraft.getTextureManager().bind(RESOURCE);
        RenderSystem.enableBlend();
        minecraft.gui.blit(matrixStack,
                midX + 10, bottom - 48,
                1, 1, barWidth, 5);

        if (expGainedAnimation.getValue() != 0) {
            GlStateManager._color4f(1, 1, 1, expGainedAnimation.getValue());
            minecraft.gui.blit(matrixStack,
                    midX + 9, bottom - 49,
                    62, 41, 84, 7);
            GlStateManager._color4f(1, 1, 1, 1);
        }

        minecraft.gui.blit(matrixStack,
                midX + 10, bottom - 48,
                1, 7, (int) (barWidth * expPercentage), 5);

        if (expGainedAnimation.getValue() != 0) {
            GlStateManager._color4f(1, 1, 1, expGainedAnimation.getValue());
            minecraft.gui.blit(matrixStack,
                    midX + 9, bottom - 49,
                    62, 49, (int) (barWidth * expPercentage), 7);
            GlStateManager._color4f(1, 1, 1, 1);
        }

        FontHelper.drawStringWithBorder(matrixStack,
                text,
                textX, bottom - 45 - minecraft.font.lineHeight,
                0xFF_ffe637, 0x3c3400);
        //minecraft.getProfiler().endSection();
    }

}
