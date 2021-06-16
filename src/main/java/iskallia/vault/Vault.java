package iskallia.vault;

import iskallia.vault.init.ModCommands;
import iskallia.vault.init.ModFeatures;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerResearchesData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Vault.MOD_ID)
public class Vault {

    public static final String MOD_ID = "the_vault";
    public static final Logger LOGGER = LogManager.getLogger();

    public static RegistryKey<World> VAULT_KEY = RegistryKey.create(Registry.DIMENSION_REGISTRY, Vault.id("vault"));

    // #Crimson_Fluff
    public static final DamageSource VAULT_FAILED = new DamageSource("vault_failed").bypassArmor().bypassInvul();

    public static final ResourceLocation STAT_VAULTS_ENTERED = registerCustomStat("vaults_entered");
    public static final ResourceLocation STAT_VAULTS_NOTIME = registerCustomStat("vaults_notime");
    public static final ResourceLocation STAT_VAULTS_BOSSKILL = registerCustomStat("vaults_bosskill");
    public static final ResourceLocation STAT_VAULTS_FIGHTERKILL = registerCustomStat("vaults_fighterkill");        // this is not a Boss Fighter
    public static final ResourceLocation STAT_VAULTS_GUARDIANKILL = registerCustomStat("vaults_guardiankill");
    public static final ResourceLocation STAT_VAULTS_FINALVAULTS = registerCustomStat("vaults_finalvaults");
    public static final ResourceLocation STAT_VAULTS_DIED_IN = registerCustomStat("vaults_killed");
    public static final ResourceLocation STAT_VAULTS_BAILED = registerCustomStat("vaults_bailed");
    public static final ResourceLocation STAT_GIVEN_BOOSTERS = registerCustomStat("given_boosters");
    public static final ResourceLocation STAT_GIVEN_BITS = registerCustomStat("given_bits");
    public static final ResourceLocation STAT_GIVEN_TRADERS = registerCustomStat("given_traders");
    public static final ResourceLocation STAT_GIVEN_GIFTS = registerCustomStat("given_gifts");
    public static final ResourceLocation STAT_DAMAGE_PARRYD = registerCustomStat("parryd");
    public static final ResourceLocation STAT_DAMAGE_VAMPIRED = registerCustomStat("vampired");
    public static final ResourceLocation STAT_DAMAGE_CRITS = registerCustomStat("crits");
    // #Crimson_Fluff END

    public Vault() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onCommandRegister);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::onBiomeLoad);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onPlayerLoggedIn);
    }

    // #Crimson_Fluff
    private static ResourceLocation registerCustomStat(String name) {
        ResourceLocation resourcelocation = new ResourceLocation(MOD_ID, name);
        Registry.register(Registry.CUSTOM_STAT, name, resourcelocation);
        Stats.CUSTOM.get(resourcelocation, IStatFormatter.DEFAULT);
        return resourcelocation;
    }
    // #Crimson_Fluff END

    public void onCommandRegister(RegisterCommandsEvent event) {
        ModCommands.registerCommands(event.getDispatcher(), event.getEnvironment());
    }

    public void onBiomeLoad(BiomeLoadingEvent event) {
        if (event.getName().equals(Vault.id("spoopy"))) {
            event.getGeneration()
                .addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, ModFeatures.VAULT_ORE)
                .addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, ModFeatures.BREADCRUMB_CHEST);
        }

        event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ModFeatures.VAULT_ROCK_ORE);
    }

    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ServerWorld serverWorld = player.getLevel();
        MinecraftServer server = player.getServer();
        PlayerVaultStatsData.get(serverWorld).getVaultStats(player).sync(server);
        PlayerResearchesData.get(serverWorld).getResearches(player).sync(server);
        PlayerAbilitiesData.get(serverWorld).getAbilities(player).sync(server);
    }


    public static String sId(String name) {
        return MOD_ID + ":" + name;
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

}