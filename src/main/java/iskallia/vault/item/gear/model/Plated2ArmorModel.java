package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class Plated2ArmorModel<T extends LivingEntity> extends VaultGearModel<T> {

    public Plated2ArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        texWidth = isLayer2() ? 64 : 128;
        texHeight = isLayer2() ? 32 : 128;

        Head = new ModelRenderer(this);
        Head.setPos(0.0F, 0.0F, 0.0F);
        Head.texOffs(0, 25).addBox(- 4.0F, - 7.0F, - 4.25F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        Head.texOffs(53, 28).addBox(- 6.25F, - 4.0F, - 5.0F, 1.0F, 5.0F, 6.0F, 0.0F, false);
        Head.texOffs(53, 28).addBox(5.25F, - 4.0F, - 5.0F, 1.0F, 5.0F, 6.0F, 0.0F, false);
        Head.texOffs(0, 25).addBox(- 6.25F, - 1.0F, - 6.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        Head.texOffs(0, 25).addBox(3.25F, - 1.0F, - 6.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(- 2.75F, 0.5F, - 5.5F);
        Head.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, - 0.3927F, 0.0F);
        cube_r1.texOffs(0, 20).addBox(- 1.5F, - 0.5F, - 0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(2.75F, 0.5F, - 5.5F);
        Head.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.3927F, 0.0F);
        cube_r2.texOffs(0, 20).addBox(- 1.5F, - 0.5F, - 0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(5.35F, - 9.25F, 3.0F);
        Head.addChild(cube_r3);
        setRotationAngle(cube_r3, - 0.6109F, 0.0F, 0.0F);
        cube_r3.texOffs(0, 0).addBox(- 0.5F, - 2.75F, - 1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);
        cube_r3.texOffs(0, 0).addBox(- 11.25F, - 2.75F, - 1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(3.6F, - 8.75F, - 1.0F);
        Head.addChild(cube_r4);
        setRotationAngle(cube_r4, - 0.6109F, 0.0F, 0.0F);
        cube_r4.texOffs(0, 14).addBox(- 0.5F, - 2.25F, - 1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        cube_r4.texOffs(0, 14).addBox(- 7.5F, - 2.25F, - 1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(0.1F, - 8.75F, - 2.75F);
        Head.addChild(cube_r5);
        setRotationAngle(cube_r5, - 0.6545F, 0.0F, 0.0F);
        cube_r5.texOffs(32, 28).addBox(- 0.5F, - 2.25F, - 1.0F, 1.0F, 7.0F, 2.0F, 0.0F, false);

        Body = new ModelRenderer(this);
        Body.setPos(0.0F, 0.0F, 0.0F);
        Body.texOffs(40, 0).addBox(- 4.0F, 0.0F, - 2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
        Body.texOffs(0, 0).addBox(- 6.0F, - 2.0F, - 4.0F, 12.0F, 6.0F, 8.0F, 0.0F, false);
        Body.texOffs(0, 14).addBox(- 5.5F, 4.0F, - 4.0F, 11.0F, 3.0F, 8.0F, 0.0F, false);
        Body.texOffs(31, 18).addBox(- 5.25F, 7.0F, - 3.5F, 10.0F, 3.0F, 7.0F, 0.0F, false);
        Body.texOffs(40, 44).addBox(- 5.0F, - 1.0F, - 5.0F, 10.0F, 3.0F, 2.0F, 0.0F, false);

        RightArm = new ModelRenderer(this);
        RightArm.setPos(- 5.0F, 2.0F, 0.0F);
        RightArm.texOffs(24, 44).addBox(- 3.0F, - 2.0F, - 2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        RightArm.texOffs(32, 32).addBox(- 7.0F, - 4.25F, - 3.5F, 7.0F, 5.0F, 7.0F, 0.0F, true);
        RightArm.texOffs(0, 41).addBox(- 7.0F, 2.0F, - 3.5F, 5.0F, 2.0F, 7.0F, 0.0F, true);

        ModelRenderer cube_r6 = new ModelRenderer(this);
        cube_r6.setPos(- 6.0F, - 5.0F, 0.0F);
        RightArm.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.0F, 0.4363F);
        cube_r6.texOffs(0, 50).addBox(0.0F, - 1.0F, - 3.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setPos(5.0F, 2.0F, 0.0F);
        LeftArm.texOffs(24, 44).addBox(- 1.0F, - 2.0F, - 2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);
        LeftArm.texOffs(0, 41).addBox(1.75F, 2.0F, - 3.5F, 5.0F, 2.0F, 7.0F, 0.0F, false);
        LeftArm.texOffs(32, 32).addBox(0.0F, - 4.25F, - 3.5F, 7.0F, 5.0F, 7.0F, 0.0F, false);

        ModelRenderer cube_r7 = new ModelRenderer(this);
        cube_r7.setPos(4.125F, - 4.0F, 0.0F);
        LeftArm.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.0F, - 0.3927F);
        cube_r7.texOffs(40, 49).addBox(- 2.125F, - 1.0F, - 3.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);

        RightBoot = new ModelRenderer(this);
        RightBoot.setPos(- 1.9F, 12.0F, 0.0F);
        RightBoot.texOffs(56, 56).addBox(- 2.0F, 9.0F, - 2.0F, 4.0F, 3.0F, 4.0F, 1.0F, false);

        LeftBoot = new ModelRenderer(this);
        LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        LeftBoot.texOffs(56, 56).addBox(- 2.0F, 9.0F, - 2.0F, 4.0F, 3.0F, 4.0F, 1.0F, true);


        Belt = new ModelRenderer(this);
        Belt.setPos(0.0F, 0.0F, 0.0F);
        Belt.texOffs(16, 16).addBox(- 4.0F, 0.0F, - 2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

        ModelRenderer cube_r1_l = new ModelRenderer(this);
        cube_r1_l.setPos(2.0976F, 20.076F, - 0.2181F);
        Belt.addChild(cube_r1_l);
        setRotationAngle(cube_r1_l, - 0.0436F, 0.0F, 0.6981F);
        cube_r1_l.texOffs(0, 0).addBox(- 3.25F, - 8.25F, - 2.75F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        ModelRenderer cube_r2_l = new ModelRenderer(this);
        cube_r2_l.setPos(- 1.9F, 12.0F, 0.0F);
        Belt.addChild(cube_r2_l);
        setRotationAngle(cube_r2_l, 0.0F, 0.0F, - 0.7854F);
        cube_r2_l.texOffs(0, 0).addBox(- 7.0F, - 2.5F, - 2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        RightLeg = new ModelRenderer(this);
        RightLeg.setPos(- 1.9F, 12.0F, 0.0F);
        RightLeg.texOffs(0, 16).addBox(- 2.0F, 0.0F, - 2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

        LeftLeg = new ModelRenderer(this);
        LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        LeftLeg.texOffs(0, 16).addBox(- 2.0F, 0.0F, - 2.0F, 4.0F, 12.0F, 4.0F, 0.5F, true);
    }

}
