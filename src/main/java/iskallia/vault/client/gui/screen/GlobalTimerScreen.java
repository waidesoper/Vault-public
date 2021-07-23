package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import iskallia.vault.client.gui.helper.FontHelper;
import iskallia.vault.init.ModConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

public class GlobalTimerScreen extends Screen {
    private static final ResourceLocation HUD_RESOURCE = new ResourceLocation(Vault.MOD_ID, "textures/gui/global_timer_screen.png");

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static long secondsLeft = 0;

    public GlobalTimerScreen() {
        super(new StringTextComponent("Global Timer"));

        // Parse the date once, when the screen is opened not every render tick()
        try {
            secondsLeft = (sdf.parse(ModConfigs.VAULT_GENERAL.VAULT_FINAL_EVENT_DATE).getTime() / 1000);

        } catch (ParseException e) {
            Vault.LOGGER.info("VaultGeneral config: VaultFinalEventDate is invalid. UTC yyyy-MM-dd HH:mm");
            secondsLeft = 0;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack, 0x00_000000);

        Minecraft minecraft = Minecraft.getInstance();

        float midX = minecraft.getWindow().getGuiScaledWidth() / 2f;
        float midY = minecraft.getWindow().getGuiScaledHeight() / 2f;

        int containerWidth = 140;
        int containerHeight = 70;

        long secondsToGo = secondsLeft - Instant.now().getEpochSecond();
        if (secondsLeft < 0) secondsLeft = 0;

        minecraft.getTextureManager().bind(HUD_RESOURCE);
        blit(matrixStack, (int) (midX - containerWidth / 2), (int) (midY - containerHeight / 2), 0, 0, containerWidth, containerHeight, 256, 256);

        String formattedTime = formatTimeLeft(secondsToGo);
        int formattedTimeLength = minecraft.font.width(formattedTime);

        String formattedSeconds = formatSecondsLeft(secondsToGo);
        int formattedSecondsLength = minecraft.font.width(formattedSeconds);

        String label = new TranslationTextComponent("tip.the_vault.remain").getString();
        int labelWidth = minecraft.font.width(label);
        minecraft.font.draw(matrixStack, label,
            midX - labelWidth / 2f, midY - 20, 0xFF_3f3f3f);

        matrixStack.pushPose();
        matrixStack.translate(0, 5, 0);
        matrixStack.pushPose();
        matrixStack.translate(midX - formattedSecondsLength / 2f, midY, 0);
        matrixStack.scale(2, 2, 2);
        FontHelper.drawStringWithBorder(matrixStack, formattedTime,
            - formattedTimeLength / 2f, - 4,
            0xFF_FFFFFF, 0xFF_483121);
        matrixStack.popPose();

        FontHelper.drawStringWithBorder(matrixStack, formattedSeconds,
            5 + midX + formattedTimeLength / 2f + 12, midY,
            0xFF_FFFFFF, 0xFF_483121);
        matrixStack.popPose();

        minecraft.getTextureManager().bind(HUD_RESOURCE);

        int hourglassWidth = 12;
        int hourglassHeight = 16;

        matrixStack.pushPose();
        matrixStack.translate(midX - containerWidth / 2f, midY, 0);
        matrixStack.scale(2, 2, 2);
        matrixStack.translate(- 18, 0, 0);
        matrixStack.mulPose(new Quaternion(0, 0, (System.currentTimeMillis() / 10L) % 360, true));
        blit(matrixStack, (int) (- hourglassWidth / 2f), (int) (- hourglassHeight / 2f), 0, 70, hourglassWidth, hourglassHeight);
        matrixStack.popPose();
    }

    public static String formatTimeLeft(long secondsLeft) {
        long minutesLeft = (secondsLeft / 60);
        long hoursLeft = (secondsLeft / (60 * 60));
        long daysLeft = (secondsLeft / (60 * 60 * 24));
        return String.format("%02d:%02d:%02d", daysLeft, hoursLeft % 24, minutesLeft % 60);
    }

    public static String formatSecondsLeft(long secondsLeft) {
        return String.format("%02d", secondsLeft % 60);
    }

}
