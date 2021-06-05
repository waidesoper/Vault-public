package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.world.gen.decorator.BreadcrumbFeature;
import iskallia.vault.world.gen.decorator.OverworldOreFeature;
import iskallia.vault.world.gen.decorator.RegionOreFeature;
import iskallia.vault.world.gen.ruletest.VaultRuleTest;
import iskallia.vault.world.gen.structure.ArenaStructure;
import iskallia.vault.world.gen.structure.VaultStructure;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.RegistryEvent;

public class ModFeatures {

    public static StructureFeature<VaultStructure.Config, ? extends Structure<VaultStructure.Config>> VAULT_FEATURE;
    public static StructureFeature<VaultStructure.Config, ? extends Structure<VaultStructure.Config>> FINAL_VAULT_FEATURE;
    public static StructureFeature<ArenaStructure.Config, ? extends Structure<ArenaStructure.Config>> ARENA_FEATURE;
    public static ConfiguredFeature<?, ?> VAULT_ORE;
    public static ConfiguredFeature<?, ?> BREADCRUMB_CHEST;
    public static ConfiguredFeature<?, ?> VAULT_ROCK_ORE;

    public static void registerStructureFeatures() {
        VAULT_FEATURE = register("vault", ModStructures.VAULT.withConfiguration(new VaultStructure.Config(() -> VaultStructure.Pools.START, 6)));
        FINAL_VAULT_FEATURE = register("final_vault", ModStructures.VAULT.withConfiguration(new VaultStructure.Config(() -> VaultStructure.Pools.FINAL_START, 6)));
        ARENA_FEATURE = register("arena", ModStructures.ARENA.withConfiguration(new ArenaStructure.Config(() -> ArenaStructure.Pools.START, 8)));
    }

    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        RegionOreFeature.register(event);
        BreadcrumbFeature.register(event);
        OverworldOreFeature.register(event);

        VAULT_ORE = register("vault_ore", RegionOreFeature.INSTANCE.withConfiguration(new OreFeatureConfig(VaultRuleTest.INSTANCE, Blocks.DIORITE.getDefaultState(), 0)).func_242731_b(1));
        BREADCRUMB_CHEST = register("breadcrumb_chest", BreadcrumbFeature.INSTANCE.withConfiguration(NoFeatureConfig.field_236559_b_));
        VAULT_ROCK_ORE = register("vault_rock_ore", OverworldOreFeature.INSTANCE.withConfiguration(
                new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ModBlocks.VAULT_ROCK_ORE.getDefaultState(), 1))
                .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(5, 0, 6))).square());
    }

    private static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> register(String name, ConfiguredFeature<FC, F> feature) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, Vault.id(name), feature);
    }

    private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, F> register(String name, StructureFeature<FC, F> feature) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, Vault.id(name), feature);
    }

}
