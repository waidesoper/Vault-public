package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.block.LootStatueBlock;
import iskallia.vault.block.entity.LootStatueTileEntity;
import iskallia.vault.entity.model.StatuePlayerModel;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.StatueType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class LootStatueRenderer extends TileEntityRenderer<LootStatueTileEntity> {

    protected static final StatuePlayerModel<PlayerEntity> PLAYER_MODEL = new StatuePlayerModel<>(0.1f, true);

    private Minecraft mc = Minecraft.getInstance();

    public LootStatueRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(LootStatueTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        BlockState blockState = tileEntity.getBlockState();
        Direction direction = blockState.getValue(LootStatueBlock.FACING);
        LootStatueBlock block = (LootStatueBlock) blockState.getBlock();    // cant use TileEntity.getStatueType !

        // #Crimson_Fluff, slight code tidy up, render acceleration chips on Mega_Statue (Yellow Gift Box)
        // chips no longer follow player head, seemed silly

        // render acceleration chips
        if (tileEntity.getChipCount() > 0) {
//            ClientPlayerEntity player = mc.player;
            int lightLevel = getLightAtPos(tileEntity.getLevel(), tileEntity.getBlockPos().above());

            for (int i = 0; i < tileEntity.getChipCount(); i++) {
                renderItem(new ItemStack(ModItems.ACCELERATION_CHIP),
                    getTranslation(i, block.getType()),
                    Vector3f.YP.rotationDegrees(180.0F + direction.toYRot()),
                    matrixStack, buffer, partialTicks, combinedOverlay, lightLevel);
            }
        }

        String latestNickname = tileEntity.getSkin().getLatestNickname();

        if (latestNickname == null || latestNickname.equals(""))
            return;

        ResourceLocation skinLocation = tileEntity.getSkin().getLocationSkin();
        RenderType renderType = PLAYER_MODEL.renderType(skinLocation);
        IVertexBuilder vertexBuilder = buffer.getBuffer(renderType);

        float scale = 0.4f;
        float headScale = 1.75f;        // #Crimson_Fluff, is mega statue possible? show larger head
        float hatScale = 3f;
        float crownScale = 1.5f;

        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.9, 0.5);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));

        // #Crimson_Fluff, don't render arms/body/legs of Mega Statue (yellow gift present, cos cant see it anyways)
        if (block.getType() != StatueType.GIFT_MEGA) {
            PLAYER_MODEL.body.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1);
            PLAYER_MODEL.leftLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1);
            PLAYER_MODEL.rightLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1);
            PLAYER_MODEL.leftArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1);
            PLAYER_MODEL.rightArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1);

            PLAYER_MODEL.jacket.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1);
            PLAYER_MODEL.leftPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1);
            PLAYER_MODEL.rightPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1);
            PLAYER_MODEL.leftSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1);

            matrixStack.pushPose();
            matrixStack.translate(0, 0, - 0.62f);
            PLAYER_MODEL.rightSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1);
            matrixStack.popPose();
        }

        matrixStack.scale(headScale, headScale, headScale);
        PLAYER_MODEL.hat.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1);
        PLAYER_MODEL.head.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1);
        matrixStack.popPose();

        Minecraft minecraft = Minecraft.getInstance();

        if (block.getType() == StatueType.GIFT_MEGA && minecraft.player != null) {
            matrixStack.pushPose();
            matrixStack.translate(0.5, 1.1, 0.5);
            matrixStack.scale(hatScale, hatScale, hatScale);
            matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180));
//            matrixStack.rotate(Vector3f.ZP.rotationDegrees(20f));
//            ItemStack itemStack = new ItemStack(Registry.ITEM.get(Vault.id("bow_hat")));
            ItemStack itemStack = new ItemStack(ModBlocks.BOW_HAT.asItem());
            IBakedModel ibakedmodel = minecraft
                .getItemRenderer().getModel(itemStack, null, null);
            minecraft.getItemRenderer()
                .render(itemStack, ItemCameraTransforms.TransformType.GROUND, true,
                    matrixStack, buffer, combinedLight, combinedOverlay, ibakedmodel);
            matrixStack.popPose();
        }

        if (tileEntity.hasCrown() && minecraft.player != null) {
            matrixStack.pushPose();
            matrixStack.translate(0.5, 1.2, 0.5);
            matrixStack.scale(crownScale, crownScale, crownScale);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(minecraft.player.tickCount));
            //        matrixStack.rotate(Vector3f.ZP.rotationDegrees(20f));
//            ItemStack itemStack = new ItemStack(Registry.ITEM.get(Vault.id("mvp_crown")));
            ItemStack itemStack = new ItemStack(ModBlocks.MVP_CROWN.asItem());
            IBakedModel ibakedmodel = minecraft
                .getItemRenderer().getModel(itemStack, null, null);
            minecraft.getItemRenderer()
                .render(itemStack, ItemCameraTransforms.TransformType.GROUND, true,
                    matrixStack, buffer, combinedLight, combinedOverlay, ibakedmodel);
            matrixStack.popPose();
        }

        if (mc.hitResult != null && mc.hitResult.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult result = (BlockRayTraceResult) mc.hitResult;

            if (tileEntity.getBlockPos().equals(result.getBlockPos())) {
                renderLabel(matrixStack, buffer, combinedLight, latestNickname, 0xFF_FFFFFF);
            }
        }
    }

    private void renderLabel(MatrixStack matrixStack, IRenderTypeBuffer buffer, int lightLevel, String text, int color) {
        FontRenderer fontRenderer = mc.font;

        matrixStack.pushPose();
        float scale = 0.02f;
        int opacity = (int) (.4f * 255.0F) << 24;
        float offset = (float) (- fontRenderer.width(text) / 2);
        Matrix4f matrix4f = matrixStack.last().pose();

        matrixStack.translate(0.5f, 1.7f, 0.5f);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation()); // face the camera
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F)); // flip vertical

        fontRenderer.drawInBatch(text, offset, 0, color, false, matrix4f, buffer, true, opacity, lightLevel);
        fontRenderer.drawInBatch(text, offset, 0, - 1, false, matrix4f, buffer, false, 0, lightLevel);
        matrixStack.popPose();
    }

    private void renderItem(ItemStack stack, double[] translation, Quaternion rotation, MatrixStack matrixStack, IRenderTypeBuffer buffer, float partialTicks, int combinedOverlay, int lightLevel) {
        matrixStack.pushPose();
        matrixStack.translate(translation[0], translation[1], translation[2]);
        matrixStack.mulPose(rotation);
        matrixStack.scale(0.25f, 0.25f, 0.25f);
        IBakedModel ibakedmodel = mc.getItemRenderer().getModel(stack, null, null);
        mc.getItemRenderer().render(stack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer, lightLevel, combinedOverlay, ibakedmodel);
        matrixStack.popPose();
    }

    private int getLightAtPos(World world, BlockPos pos) {
        int blockLight = world.getBrightness(LightType.BLOCK, pos);
        int skyLight = world.getBrightness(LightType.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }

    private double[] getTranslation(int index, StatueType item) {
        double a = 0.3;
        if (item == StatueType.GIFT_MEGA) a = 0.85;  // the present

        switch (index) {
            case 0:
                return new double[] {0.75, a, 0.25};
            case 1:
                return new double[] {0.75, a, 0.75};
            case 2:
                return new double[] {0.25, a, 0.75};
            default:
                return new double[] {0.25, a, 0.25};
        }
    }
}
