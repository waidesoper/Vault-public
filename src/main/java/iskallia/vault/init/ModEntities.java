package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.entity.*;
import iskallia.vault.entity.renderer.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModEntities {

    public static EntityType<FighterEntity> FIGHTER;
    public static EntityType<MonsterEyeEntity> MONSTER_EYE;
    public static EntityType<RobotEntity> ROBOT;
    public static EntityType<BlueBlazeEntity> BLUE_BLAZE;
    public static EntityType<BoogiemanEntity> BOOGIEMAN;
    public static EntityType<VaultGuardianEntity> VAULT_GUARDIAN;
    public static EntityType<VaultFighterEntity> VAULT_FIGHTER;
    public static EntityType<EternalEntity> ETERNAL;
    public static EntityType<TreasureGoblinEntity> TREASURE_GOBLIN;
    public static EntityType<FinalBossEntity> FINAL_BOSS;
    public static EntityType<FinalDummyEntity> FINAL_DUMMY;

    public static void register(RegistryEvent.Register<EntityType<?>> event) {
        FIGHTER = register("fighter", EntityType.Builder.of(FighterEntity::new, EntityClassification.MONSTER)
            .sized(0.6F, 1.95F), event);
        MONSTER_EYE = register("monster_eye", EntityType.Builder.of(MonsterEyeEntity::new, EntityClassification.MONSTER)
            .sized(2.04F * 2, 2.04F * 2), event);
        ROBOT = register("robot", EntityType.Builder.of(RobotEntity::new, EntityClassification.MONSTER)
            .sized(1.4F * 2, 2.7F * 2), event);
        BLUE_BLAZE = register("blue_blaze", EntityType.Builder.of(BlueBlazeEntity::new, EntityClassification.MONSTER)
            .sized(0.6F * 2, 1.8F * 2), event);
        BOOGIEMAN = register("boogieman", EntityType.Builder.of(BoogiemanEntity::new, EntityClassification.MONSTER)
            .sized(0.6F * 2, 1.95F * 2), event);
        VAULT_GUARDIAN = register("vault_guardian", EntityType.Builder.of(VaultGuardianEntity::new, EntityClassification.MONSTER)
            .sized(1.3F, 2.95F), event);
        VAULT_FIGHTER = register("vault_fighter", EntityType.Builder.of(VaultFighterEntity::new, EntityClassification.MONSTER)
            .sized(0.6F, 1.95F), event);
        ETERNAL = register("eternal", EntityType.Builder.of(EternalEntity::new, EntityClassification.CREATURE)
            .sized(0.6F, 1.95F), event);
        TREASURE_GOBLIN = register("treasure_goblin", EntityType.Builder.of(TreasureGoblinEntity::new, EntityClassification.CREATURE)
            .sized(1f, 1f), event);
        FINAL_BOSS = register("final_boss", EntityType.Builder.of(FinalBossEntity::new, EntityClassification.MONSTER)
            .sized(0.6F, 1.95F), event);
        FINAL_DUMMY = register("final_dummy", EntityType.Builder.of(FinalDummyEntity::new, EntityClassification.MONSTER)
            .sized(0.6F, 1.95F), event);
    }

    // #Crimson_Fluff, remove deprecated GlobalEntityAttributes
    public static void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(FIGHTER, ZombieEntity.createAttributes().build());
        event.put(MONSTER_EYE, ZombieEntity.createAttributes().build());
        event.put(ROBOT, ZombieEntity.createAttributes().build());
        event.put(BLUE_BLAZE, ZombieEntity.createAttributes().build());
        event.put(BOOGIEMAN, ZombieEntity.createAttributes().build());
        event.put(VAULT_GUARDIAN, ZombieEntity.createAttributes().build());
        event.put(VAULT_FIGHTER, ZombieEntity.createAttributes().build());
        event.put(ETERNAL, ZombieEntity.createAttributes().build());
        event.put(TREASURE_GOBLIN, ZombieEntity.createAttributes().build());
        event.put(FINAL_BOSS, FinalBossEntity.createAttributes().build());
        event.put(FINAL_DUMMY, FinalDummyEntity.createAttributes().build());
    }

    public static <T extends LivingEntity> EntityType<T> register(String name, EntityType.Builder<T> builder, RegistryEvent.Register<EntityType<?>> event) {
        EntityType<T> entityType = builder.build(Vault.sId(name));
        event.getRegistry().register(entityType.setRegistryName(Vault.id(name)));
//        if (attributes != null) GlobalEntityTypeAttributes.put(entityType, attributes.get().build());
        return entityType;
    }

    public static class Renderers {
        public static void register(FMLClientSetupEvent event) {
            RenderingRegistry.registerEntityRenderingHandler(FIGHTER, FighterRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(MONSTER_EYE, MonsterEyeRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ROBOT, RobotRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(BLUE_BLAZE, BlueBlazeRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(BOOGIEMAN, BoogiemanRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(VAULT_GUARDIAN, VaultGuardianRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(VAULT_FIGHTER, FighterRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ETERNAL, EternalRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(TREASURE_GOBLIN, TreasureGoblinRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(FINAL_BOSS, FighterRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(FINAL_DUMMY, VaultGuardianRenderer::new);
        }
    }

}
