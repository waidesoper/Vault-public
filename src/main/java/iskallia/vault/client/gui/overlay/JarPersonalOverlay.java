package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class JarPersonalOverlay {
    public static int currentLevel = 0;

    private static final ResourceLocation HUD_RESOURCE = new ResourceLocation(Vault.MOD_ID, "textures/gui/vault-hud.png");

    @SubscribeEvent
    public static void onPostRender(RenderGameOverlayEvent.Post event) {
        if (! Vault.CONFIGURATION.arenaEnabled.get()) return;
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) return;

        MatrixStack matrixStack = event.getMatrixStack();
        Minecraft minecraft = Minecraft.getInstance();

        int hourglassWidth = 21;
        int hourglassHeight = 94;
        int posX = minecraft.getWindow().getGuiScaledWidth() - hourglassWidth - 2;
        int posY = minecraft.getWindow().getGuiScaledHeight() - hourglassHeight - 2;

        matrixStack.pushPose();
        RenderSystem.enableBlend();
        minecraft.getTextureManager().bind(HUD_RESOURCE);

        // Empty
        AbstractGui.blit(matrixStack, posX, posY, 13, 69, hourglassWidth, hourglassHeight, 256, 256);

        // Filled
        if (currentLevel > 0) {
            hourglassHeight = 72;

            // if number of subs in list is greater than amount we need, don't worry...
            int m = currentLevel % Vault.CONFIGURATION.arenaMagicNumber.get();

            float percent = (100.0f / Vault.CONFIGURATION.arenaMagicNumber.get()) * m;
            int newHeight = MathHelper.floor((percent / 100.0f) * hourglassHeight);

            AbstractGui.blit(matrixStack, posX, posY + 11 + hourglassHeight - newHeight, 35,80 + hourglassHeight - newHeight, hourglassWidth, newHeight, 256, 256);
        }

        matrixStack.popPose();
    }
}
