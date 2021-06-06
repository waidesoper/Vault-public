package iskallia.vault.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.helper.Rectangle;
import iskallia.vault.client.gui.screen.GlobalTraderScreen;
import iskallia.vault.client.gui.screen.VendingMachineScreen;
import iskallia.vault.vending.Trade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

public class GlobalTradeWidget extends Widget {

    public static final int BUTTON_WIDTH = 88;
    public static final int BUTTON_HEIGHT = 27;

    protected GlobalTraderScreen parentScreen;
    protected Trade trade;

    public GlobalTradeWidget(int x, int y, Trade trade, GlobalTraderScreen parentScreen) {
        super(x, y, 0, 0, new StringTextComponent(""));
        this.parentScreen = parentScreen;
        this.trade = trade;
    }

    public Trade getTrade() {
        return trade;
    }

    public void mouseMoved(double mouseX, double mouseY) {

    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return x <= mouseX && mouseX <= x + BUTTON_WIDTH
            && y <= mouseY && mouseY <= y + BUTTON_HEIGHT;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();

        minecraft.getTextureManager().bind(VendingMachineScreen.HUD_RESOURCE);

        Trade trade = this.trade;
        ItemStack buy = trade.getBuy().toStack();
        ItemStack sell = trade.getSell().toStack();

        ItemRenderer itemRenderer = minecraft.getItemRenderer();

        Rectangle tradeBoundaries = parentScreen.getTradeBoundaries();
        int yOFfset = parentScreen.tradesContainer.getyOffset();

        if (trade.getTradesLeft() == 0) {
            blit(matrixStack, x, y,
                277, 96, BUTTON_WIDTH, BUTTON_HEIGHT, 512, 256);
            RenderSystem.disableDepthTest();
            itemRenderer.renderGuiItem(buy,
                5 + x + tradeBoundaries.x0,
                6 + y + tradeBoundaries.y0 - yOFfset);
            itemRenderer.renderGuiItem(sell,
                55 + x + tradeBoundaries.x0,
                6 + y + tradeBoundaries.y0 - yOFfset);
            return;
        }

        boolean isHovered = isHovered(mouseX, mouseY);

        boolean isSelected = parentScreen.getMenu().getSelectedTrade() == this.trade;

        blit(matrixStack, x, y,
            277, isHovered || isSelected ? 68 : 40, BUTTON_WIDTH, BUTTON_HEIGHT, 512, 256);

        RenderSystem.disableDepthTest();
        itemRenderer.renderGuiItem(buy,
            5 + x + tradeBoundaries.x0,
            6 + y + tradeBoundaries.y0 - yOFfset);
        itemRenderer.renderGuiItem(sell,
            55 + x + tradeBoundaries.x0,
            6 + y + tradeBoundaries.y0 - yOFfset);

        minecraft.font.draw(matrixStack,
            buy.getCount() + "", x + 23, y + 10, 0xFF_FFFFFF);
        minecraft.font.draw(matrixStack,
            sell.getCount() + "", x + 73, y + 10, 0xFF_FFFFFF);
        RenderSystem.enableDepthTest();
    }

}
