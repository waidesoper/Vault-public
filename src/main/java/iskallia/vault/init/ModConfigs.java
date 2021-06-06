package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.config.*;
import iskallia.vault.config.TraderCoreConfig.TraderCoreCommonConfig;
import iskallia.vault.config.TraderCoreConfig.TraderCoreOmegaConfig;
import iskallia.vault.config.TraderCoreConfig.TraderCoreRaffleConfig;

public class ModConfigs {

    public static AbilitiesConfig ABILITIES;
    public static AbilitiesGUIConfig ABILITIES_GUI;
    public static TalentsConfig TALENTS;
    public static TalentsGUIConfig TALENTS_GUI;
    public static ResearchConfig RESEARCHES;
    public static ResearchesGUIConfig RESEARCHES_GUI;
    public static SkillDescriptionsConfig SKILL_DESCRIPTIONS;
    public static SkillGatesConfig SKILL_GATES;
    public static VaultLevelsConfig LEVELS_META;
    public static VaultRelicsConfig VAULT_RELICS;
    public static VaultOreConfig VAULT_ORES;
    public static VaultMobsConfig VAULT_MOBS;
    public static VaultItemsConfig VAULT_ITEMS;
    public static VaultAltarConfig VAULT_ALTAR;
    public static VaultGeneralConfig VAULT_GENERAL;
    public static VaultCrystalConfig VAULT_CRYSTAL;
    public static VaultPortalConfig VAULT_PORTAL;
    public static VaultVendingConfig VENDING_CONFIG;
    public static LegendaryTreasureNormalConfig LEGENDARY_TREASURE_NORMAL;
    public static LegendaryTreasureRareConfig LEGENDARY_TREASURE_RARE;
    public static LegendaryTreasureEpicConfig LEGENDARY_TREASURE_EPIC;
    public static LegendaryTreasureOmegaConfig LEGENDARY_TREASURE_OMEGA;
    public static GiftBombConfig GIFT_BOMB;
    public static StatueLootConfig STATUE_LOOT;
    public static CryoChamberConfig CRYO_CHAMBER;
//    public static KeyPressRecipesConfig KEY_PRESS;
    public static OverLevelEnchantConfig OVERLEVEL_ENCHANT;
    public static VaultStewConfig VAULT_STEW;
    public static MysteryBoxConfig MYSTERY_BOX;
    public static VaultModifiersConfig VAULT_MODIFIERS;
    //public static VaultGearBaseConfig VAULT_GEAR_BASE;
    public static TraderCoreCommonConfig TRADER_CORE_COMMON;
    public static TraderCoreOmegaConfig TRADER_CORE_OMEGA;
    public static TraderCoreRaffleConfig TRADER_CORE_RAFFLE;
    public static PandorasBoxConfig PANDORAS_BOX;
    public static EternalConfig ETERNAL;
    public static VaultTimerConfig VAULT_TIMER;
    public static CrystalUpgradeConfig CRYSTAL_UPGRADE;
    public static VaultGearConfig VAULT_GEAR_SCRAPPY;
    public static VaultGearConfig VAULT_GEAR_COMMON;
    public static VaultGearConfig VAULT_GEAR_RARE;
    public static VaultGearConfig VAULT_GEAR_EPIC;
    public static VaultGearConfig VAULT_GEAR_OMEGA;
    public static VaultGearConfig.General VAULT_GEAR;
    public static SetsConfig SETS;
    public static GlobalTraderConfig GLOBAL_TRADER;
    public static PlayerExpConfig PLAYER_EXP;
    public static FinalVaultGeneralConfig FINAL_VAULT_GENERAL;

    public static VaultFightersConfig VAULT_FIGHTERS;

    public static void register() {
        VAULT_FIGHTERS = (VaultFightersConfig) new VaultFightersConfig().readConfig();

        ABILITIES = (AbilitiesConfig) new AbilitiesConfig().readConfig();
        ABILITIES_GUI = (AbilitiesGUIConfig) new AbilitiesGUIConfig().readConfig();
        TALENTS = (TalentsConfig) new TalentsConfig().readConfig();
        TALENTS_GUI = (TalentsGUIConfig) new TalentsGUIConfig().readConfig();
        RESEARCHES = (ResearchConfig) new ResearchConfig().readConfig();
        RESEARCHES_GUI = (ResearchesGUIConfig) new ResearchesGUIConfig().readConfig();
        SKILL_DESCRIPTIONS = (SkillDescriptionsConfig) new SkillDescriptionsConfig().readConfig();
        SKILL_GATES = (SkillGatesConfig) new SkillGatesConfig().readConfig();
        LEVELS_META = (VaultLevelsConfig) new VaultLevelsConfig().readConfig();
        VAULT_RELICS = (VaultRelicsConfig) new VaultRelicsConfig().readConfig();
        VAULT_ORES = (VaultOreConfig) new VaultOreConfig().readConfig();
        VAULT_MOBS = (VaultMobsConfig) new VaultMobsConfig().readConfig();
        VAULT_ITEMS = (VaultItemsConfig) new VaultItemsConfig().readConfig();
        VAULT_ALTAR = (VaultAltarConfig) new VaultAltarConfig().readConfig();
        VAULT_GENERAL = (VaultGeneralConfig) new VaultGeneralConfig().readConfig();
        VAULT_CRYSTAL = (VaultCrystalConfig) new VaultCrystalConfig().readConfig();
        VAULT_PORTAL = (VaultPortalConfig) new VaultPortalConfig().readConfig();
        VENDING_CONFIG = (VaultVendingConfig) new VaultVendingConfig().readConfig();
        LEGENDARY_TREASURE_NORMAL = (LegendaryTreasureNormalConfig) new LegendaryTreasureNormalConfig().readConfig();
        LEGENDARY_TREASURE_RARE = (LegendaryTreasureRareConfig) new LegendaryTreasureRareConfig().readConfig();
        LEGENDARY_TREASURE_EPIC = (LegendaryTreasureEpicConfig) new LegendaryTreasureEpicConfig().readConfig();
        LEGENDARY_TREASURE_OMEGA = (LegendaryTreasureOmegaConfig) new LegendaryTreasureOmegaConfig().readConfig();
        GIFT_BOMB = (GiftBombConfig) new GiftBombConfig().readConfig();
        STATUE_LOOT = (StatueLootConfig) new StatueLootConfig().readConfig();
        CRYO_CHAMBER = (CryoChamberConfig) new CryoChamberConfig().readConfig();
//        KEY_PRESS = (KeyPressRecipesConfig) new KeyPressRecipesConfig().readConfig();
        OVERLEVEL_ENCHANT = (OverLevelEnchantConfig) new OverLevelEnchantConfig().readConfig();
        VAULT_STEW = (VaultStewConfig) new VaultStewConfig().readConfig();
        MYSTERY_BOX = (MysteryBoxConfig) new MysteryBoxConfig().readConfig();
        VAULT_MODIFIERS = (VaultModifiersConfig) new VaultModifiersConfig().readConfig();
        //VAULT_GEAR_BASE = (VaultGearBaseConfig) new VaultGearBaseConfig().readAndFill();
        TRADER_CORE_COMMON = (TraderCoreCommonConfig) new TraderCoreCommonConfig().readConfig();
        TRADER_CORE_OMEGA = (TraderCoreOmegaConfig) new TraderCoreOmegaConfig().readConfig();
        TRADER_CORE_RAFFLE = (TraderCoreRaffleConfig) new TraderCoreRaffleConfig().readConfig();
        PANDORAS_BOX = (PandorasBoxConfig) new PandorasBoxConfig().readConfig();
        ETERNAL = (EternalConfig) new EternalConfig().readConfig();
        VAULT_TIMER = (VaultTimerConfig) new VaultTimerConfig().readConfig();
        CRYSTAL_UPGRADE = (CrystalUpgradeConfig) new CrystalUpgradeConfig().readConfig();
        VAULT_GEAR_SCRAPPY = (VaultGearConfig) new VaultGearConfig.Scrappy().readConfig();
        VAULT_GEAR_COMMON = (VaultGearConfig) new VaultGearConfig.Common().readConfig();
        VAULT_GEAR_RARE = (VaultGearConfig) new VaultGearConfig.Rare().readConfig();
        VAULT_GEAR_EPIC = (VaultGearConfig) new VaultGearConfig.Epic().readConfig();
        VAULT_GEAR_OMEGA = (VaultGearConfig) new VaultGearConfig.Omega().readConfig();
        VAULT_GEAR = (VaultGearConfig.General) new VaultGearConfig.General().readConfig();
        SETS = (SetsConfig) new SetsConfig().readConfig();
        GLOBAL_TRADER = (GlobalTraderConfig) new GlobalTraderConfig().readConfig();
        PLAYER_EXP = (PlayerExpConfig) new PlayerExpConfig().readConfig();
        FINAL_VAULT_GENERAL = (FinalVaultGeneralConfig) new FinalVaultGeneralConfig().readConfig();
        Vault.LOGGER.info("Vault Configs are loaded successfully!");
    }

}
