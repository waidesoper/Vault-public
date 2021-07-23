package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.item.*;
import iskallia.vault.item.gear.*;
import iskallia.vault.util.VaultRarity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Random;

public class ModItems {

    public static ItemGroup VAULT_MOD_GROUP = new ItemGroup(Vault.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(VAULT_BURGER);
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }
    };

    public static VaultXPFoodItem VAULT_BURGER = new VaultXPFoodItem(Vault.id("vault_burger"), () -> ModConfigs.VAULT_ITEMS.VAULT_BURGER.XP_MIN_PERCENT, () -> ModConfigs.VAULT_ITEMS.VAULT_BURGER.XP_MAX_PERCENT, new Item.Properties().tab(VAULT_MOD_GROUP));
    public static ItemSkillOrb SKILL_ORB = new ItemSkillOrb(VAULT_MOD_GROUP);
    public static ItemVaultGem VAULT_ROCK = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("vault_rock"));
    public static ItemVaultGem ALEXANDRITE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_alexandrite"));
    public static ItemVaultGem BENITOITE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_benitoite"));
    public static ItemVaultGem LARIMAR_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_larimar"));
    public static ItemVaultGem BLACK_OPAL_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_black_opal"));
    public static ItemVaultGem PAINITE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_painite"));
    public static ItemVaultGem ISKALLIUM_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_iskallium"));
    public static ItemVaultGem RENIUM_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_renium"));
    public static ItemVaultGem GORGINITE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_gorginite"));
    public static ItemVaultGem SPARKLETINE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_sparkletine"));
    public static ItemVaultGem WUTODIE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_wutodie"));
    public static ItemVaultGem POG = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_pog"));
    public static ItemVaultCrystal VAULT_CRYSTAL_NORMAL = new ItemVaultCrystal(VAULT_MOD_GROUP, Vault.id("vault_crystal_normal"), VaultRarity.COMMON);
    public static ItemVaultCrystal VAULT_CRYSTAL_RARE = new ItemVaultCrystal(VAULT_MOD_GROUP, Vault.id("vault_crystal_rare"), VaultRarity.RARE);
    public static ItemVaultCrystal VAULT_CRYSTAL_EPIC = new ItemVaultCrystal(VAULT_MOD_GROUP, Vault.id("vault_crystal_epic"), VaultRarity.EPIC);
    public static ItemVaultCrystal VAULT_CRYSTAL_OMEGA = new ItemVaultCrystal(VAULT_MOD_GROUP, Vault.id("vault_crystal_omega"), VaultRarity.OMEGA);
    public static ItemVaultKey ALEXANDRITE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_alexandrite"));
    public static ItemVaultKey BENITOITE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_benitoite"));
    public static ItemVaultKey LARIMAR_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_larimar"));
    public static ItemVaultKey BLACK_OPAL_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_black_opal"));
    public static ItemVaultKey PAINITE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_painite"));
    public static ItemVaultKey ISKALLIUM_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_iskallium"));
    public static ItemVaultKey RENIUM_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_renium"));
    public static ItemVaultKey GORGINITE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_gorginite"));
    public static ItemVaultKey SPARKLETINE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_sparkletine"));
    public static ItemVaultKey WUTODIE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_wutodie"));

    public static ItemSkellingtonKey SKELLY_KEY = new ItemSkellingtonKey(VAULT_MOD_GROUP, Vault.id("key_skelly"));        // #Crimson_Fluff, Skelly Key

    public static ItemBit BIT_100 = new ItemBit(VAULT_MOD_GROUP, Vault.id("bit_100"), 100);
    public static ItemBit BIT_500 = new ItemBit(VAULT_MOD_GROUP, Vault.id("bit_500"), 500);
    public static ItemBit BIT_1000 = new ItemBit(VAULT_MOD_GROUP, Vault.id("bit_1000"), 1000);
    public static ItemBit BIT_5000 = new ItemBit(VAULT_MOD_GROUP, Vault.id("bit_5000"), 5000);
    public static ItemBit BIT_10000 = new ItemBit(VAULT_MOD_GROUP, Vault.id("bit_10000"), 10000);
    public static ItemRelicBoosterPack RELIC_BOOSTER_PACK = new ItemRelicBoosterPack(VAULT_MOD_GROUP, Vault.id("relic_booster_pack"));

    public static RelicPartItem DRAGON_HEAD_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_dragon_head"));
    public static RelicPartItem DRAGON_TAIL_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_dragon_tail"));
    public static RelicPartItem DRAGON_FOOT_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_dragon_foot"));
    public static RelicPartItem DRAGON_CHEST_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_dragon_chest"));
    public static RelicPartItem DRAGON_BREATH_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_dragon_breath"));
    public static RelicPartItem MINERS_DELIGHT_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_miners_delight"));
    public static RelicPartItem MINERS_LIGHT_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_miners_light"));
    public static RelicPartItem PICKAXE_HANDLE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_pickaxe_handle"));
    public static RelicPartItem PICKAXE_HEAD_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_pickaxe_head"));
    public static RelicPartItem PICKAXE_TOOL_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_pickaxe_tool"));
    public static RelicPartItem SWORD_BLADE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_sword_blade"));
    public static RelicPartItem SWORD_HANDLE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_sword_handle"));
    public static RelicPartItem SWORD_STICK_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_sword_stick"));
    public static RelicPartItem WARRIORS_ARMOUR_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_warriors_armour"));
    public static RelicPartItem WARRIORS_CHARM_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_warriors_charm"));
    public static RelicPartItem DIAMOND_ESSENCE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_diamond_essence"));
    public static RelicPartItem GOLD_ESSENCE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_gold_essence"));
    public static RelicPartItem MYSTIC_GEM_ESSENCE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_mystic_gem_essence"));
    public static RelicPartItem NETHERITE_ESSENCE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_netherite_essence"));
    public static RelicPartItem PLATINUM_ESSENCE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_platinum_essence"));
    public static RelicPartItem TWITCH_EMOTE1_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twitch_emote1"));
    public static RelicPartItem TWITCH_EMOTE2_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twitch_emote2"));
    public static RelicPartItem TWITCH_EMOTE3_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twitch_emote3"));
    public static RelicPartItem TWITCH_EMOTE4_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twitch_emote4"));
    public static RelicPartItem TWITCH_EMOTE5_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twitch_emote5"));
    public static RelicPartItem CUPCAKE_BLUE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_cupcake_blue"));
    public static RelicPartItem CUPCAKE_LIME_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_cupcake_lime"));
    public static RelicPartItem CUPCAKE_PINK_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_cupcake_pink"));
    public static RelicPartItem CUPCAKE_PURPLE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_cupcake_purple"));
    public static RelicPartItem CUPCAKE_RED_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_cupcake_red"));
    public static RelicPartItem AIR_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_air"));
    public static RelicPartItem SPIRIT_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_spirit"));
    public static RelicPartItem FIRE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_fire"));
    public static RelicPartItem EARTH_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_earth"));
    public static RelicPartItem WATER_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_water"));

    public static ItemVaultRune VAULT_RUNE = new ItemVaultRune(VAULT_MOD_GROUP, Vault.id("vault_rune"));
    public static ObeliskInscriptionItem OBELISK_INSCRIPTION = new ObeliskInscriptionItem(VAULT_MOD_GROUP, Vault.id("obelisk_inscription"));
    public static ItemTraderCore TRADER_CORE = new ItemTraderCore(VAULT_MOD_GROUP, Vault.id("trader_core"));
    public static ItemTraderCore TRADER_CORE_OMEGA = new ItemTraderCore(VAULT_MOD_GROUP, Vault.id("trader_core_omega"));
    public static ItemTraderCore TRADER_CORE_RAFFLE = new ItemTraderCore(VAULT_MOD_GROUP, Vault.id("trader_core_raffle"));
    public static ItemUnidentifiedArtifact UNIDENTIFIED_ARTIFACT = new ItemUnidentifiedArtifact(VAULT_MOD_GROUP, Vault.id("unidentified_artifact"));
    public static ItemLegendaryTreasure LEGENDARY_TREASURE_NORMAL = new ItemLegendaryTreasure(VAULT_MOD_GROUP, Vault.id("legendary_treasure_normal"), VaultRarity.COMMON);
    public static ItemLegendaryTreasure LEGENDARY_TREASURE_RARE = new ItemLegendaryTreasure(VAULT_MOD_GROUP, Vault.id("legendary_treasure_rare"), VaultRarity.RARE);
    public static ItemLegendaryTreasure LEGENDARY_TREASURE_EPIC = new ItemLegendaryTreasure(VAULT_MOD_GROUP, Vault.id("legendary_treasure_epic"), VaultRarity.EPIC);
    public static ItemLegendaryTreasure LEGENDARY_TREASURE_OMEGA = new ItemLegendaryTreasure(VAULT_MOD_GROUP, Vault.id("legendary_treasure_omega"), VaultRarity.OMEGA);
    public static ItemGiftBomb NORMAL_GIFT_BOMB = new ItemGiftBomb(VAULT_MOD_GROUP, ItemGiftBomb.Variant.NORMAL, Vault.id("gift_bomb_normal"));
    public static ItemGiftBomb SUPER_GIFT_BOMB = new ItemGiftBomb(VAULT_MOD_GROUP, ItemGiftBomb.Variant.SUPER, Vault.id("gift_bomb_super"));
    public static ItemGiftBomb MEGA_GIFT_BOMB = new ItemGiftBomb(VAULT_MOD_GROUP, ItemGiftBomb.Variant.MEGA, Vault.id("gift_bomb_mega"));
    public static ItemGiftBomb OMEGA_GIFT_BOMB = new ItemGiftBomb(VAULT_MOD_GROUP, ItemGiftBomb.Variant.OMEGA, Vault.id("gift_bomb_omega"));
    public static RelicItem VAULT_RELIC = new RelicItem(VAULT_MOD_GROUP, Vault.id("vault_relic"));
    public static ItemSkillOrbFrame SKILL_ORB_FRAME = new ItemSkillOrbFrame(VAULT_MOD_GROUP, Vault.id("orb_frame"));
    public static ItemSkillShard SKILL_SHARD = new ItemSkillShard(VAULT_MOD_GROUP, Vault.id("skill_shard"));
    public static ItemVaultFruit.BitterLemon BITTER_LEMON = new ItemVaultFruit.BitterLemon(VAULT_MOD_GROUP, Vault.id("bitter_lemon"), 30 * 20);
    public static ItemVaultFruit.SourOrange SOUR_ORANGE = new ItemVaultFruit.SourOrange(VAULT_MOD_GROUP, Vault.id("sour_orange"), 60 * 20);
    public static ItemVaultFruit.MysticPear MYSTIC_PEAR = new ItemVaultFruit.MysticPear(VAULT_MOD_GROUP, Vault.id("mystic_pear"), 5 * 60 * 20);
    public static BasicItem KEY_PIECE = new BasicItem(Vault.id("key_piece"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem KEY_MOULD = new BasicItem(Vault.id("key_mould"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem BLANK_KEY = new BasicItem(Vault.id("blank_key"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem NETHERITE_CLUSTER = new BasicItem(Vault.id("cluster_netherite"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem ALEXANDRITE_CLUSTER = new BasicItem(Vault.id("cluster_alexandrite"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem BENITOITE_CLUSTER = new BasicItem(Vault.id("cluster_benitoite"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem LARIMAR_CLUSTER = new BasicItem(Vault.id("cluster_larimar"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem BLACK_OPAL_CLUSTER = new BasicItem(Vault.id("cluster_black_opal"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem PAINITE_CLUSTER = new BasicItem(Vault.id("cluster_painite"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem ISKALLIUM_CLUSTER = new BasicItem(Vault.id("cluster_iskallium"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem RENIUM_CLUSTER = new BasicItem(Vault.id("cluster_renium"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem GORGINITE_CLUSTER = new BasicItem(Vault.id("cluster_gorginite"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem SPARKLETINE_CLUSTER = new BasicItem(Vault.id("cluster_sparkletine"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem WUTODIE_CLUSTER = new BasicItem(Vault.id("cluster_wutodie"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static RelicPartItem TWOLF999_HEAD_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twolf999_head"));
    public static RelicPartItem TWOLF999_COMBAT_VEST_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twolf999_combat_vest"));
    public static RelicPartItem TWOLF999_COMBAT_LEGGINGS_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twolf999_combat_leggings"));
    public static RelicPartItem TWOLF999_COMBAT_GLOVES_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twolf999_combat_gloves"));
    public static RelicPartItem TWOLF999_COMBAT_BOOTS_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twolf999_combat_boots"));
    public static RelicPartItem ARMOR_OF_FORBEARANCE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_armor_of_forbearance"));
    public static RelicPartItem HEART_OF_THE_VOID_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_heart_of_the_void"));
    public static RelicPartItem NEMESIS_THWARTER_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_nemesis_thwarter"));
    public static RelicPartItem REVERENCE_EDGE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_reverence_edge"));
    public static RelicPartItem WINGS_OF_EQUITY_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_wings_of_equity"));

    public static VaultStewItem VAULT_STEW_MYSTERY = new VaultStewItem(Vault.id("vault_stew_mystery"), VaultStewItem.Rarity.MYSTERY, new Item.Properties().food(VaultStewItem.FOOD).stacksTo(1).tab(VAULT_MOD_GROUP));
    public static VaultStewItem VAULT_STEW_NORMAL = new VaultStewItem(Vault.id("vault_stew_normal"), VaultStewItem.Rarity.NORMAL, new Item.Properties().food(VaultStewItem.FOOD).stacksTo(1).tab(VAULT_MOD_GROUP));
    public static VaultStewItem VAULT_STEW_RARE = new VaultStewItem(Vault.id("vault_stew_rare"), VaultStewItem.Rarity.RARE, new Item.Properties().food(VaultStewItem.FOOD).stacksTo(1).tab(VAULT_MOD_GROUP));
    public static VaultStewItem VAULT_STEW_EPIC = new VaultStewItem(Vault.id("vault_stew_epic"), VaultStewItem.Rarity.EPIC, new Item.Properties().food(VaultStewItem.FOOD).stacksTo(1).tab(VAULT_MOD_GROUP));
    public static VaultStewItem VAULT_STEW_OMEGA = new VaultStewItem(Vault.id("vault_stew_omega"), VaultStewItem.Rarity.OMEGA, new Item.Properties().food(VaultStewItem.FOOD).stacksTo(1).tab(VAULT_MOD_GROUP));
    public static BasicItem POISONOUS_MUSHROOM = new BasicItem(Vault.id("poisonous_mushroom"), new Item.Properties().tab(VAULT_MOD_GROUP));
    public static BasicItem VAULT_GOLD = new BasicItem(Vault.id("vault_gold"), new Item.Properties().tab(VAULT_MOD_GROUP));
    public static BasicItem VAULT_DIAMOND = new BasicItem(Vault.id("vault_diamond"), new Item.Properties().tab(VAULT_MOD_GROUP));
    public static BasicItem SKILL_ESSENCE = new BasicItem(Vault.id("skill_essence"), new Item.Properties().tab(VAULT_MOD_GROUP));
    public static VaultXPFoodItem OOZING_PIZZA = new VaultXPFoodItem(Vault.id("oozing_pizza"), () -> ModConfigs.VAULT_ITEMS.OOZING_PIZZA.XP_MIN_PERCENT, () -> ModConfigs.VAULT_ITEMS.OOZING_PIZZA.XP_MIN_PERCENT, new Item.Properties().tab(VAULT_MOD_GROUP));
    public static LootableItem UNIDENTIFIED_RELIC = new LootableItem(Vault.id("unidentified_relic"), new Item.Properties().tab(VAULT_MOD_GROUP), () -> new ItemStack(ModConfigs.VAULT_RELICS.getRandomPart()));
    public static ItemVaultFruit.SweetKiwi SWEET_KIWI = new ItemVaultFruit.SweetKiwi(VAULT_MOD_GROUP, Vault.id("sweet_kiwi"), 20 * 5);
    public static BasicItem HUNTER_EYE = new BasicItem(Vault.id("hunter_eye"), new Item.Properties().tab(VAULT_MOD_GROUP));
    public static BasicItem BURGER_PATTY = new BasicItem(Vault.id("burger_patty"), new Item.Properties().tab(VAULT_MOD_GROUP));
    public static BasicItem BURGER_BUN = new BasicItem(Vault.id("burger_bun"), new Item.Properties().tab(VAULT_MOD_GROUP));
    public static BasicItem MATURE_CHEDDAR = new BasicItem(Vault.id("mature_cheddar"), new Item.Properties().tab(VAULT_MOD_GROUP));
    public static BasicItem MYSTIC_TOMATO = new BasicItem(Vault.id("mystic_tomato"), new Item.Properties().tab(VAULT_MOD_GROUP));
    public static VialOfSandItem VIAL_OF_SAND = new VialOfSandItem(Vault.id("vial_of_sand"), new Item.Properties().tab(VAULT_MOD_GROUP));
    public static BasicItem VAULT_SCRAP = new BasicItem(Vault.id("vault_scrap"), new Item.Properties().tab(VAULT_MOD_GROUP));
    public static BasicItem VAULT_INGOT = new BasicItem(Vault.id("vault_ingot"), new Item.Properties().tab(VAULT_MOD_GROUP));
    public static BasicItem VAULT_PLATINUM = new BasicItem(Vault.id("vault_platinum"), new Item.Properties().tab(VAULT_MOD_GROUP));
    public static LootableItem MYSTERY_BOX = new LootableItem(Vault.id("mystery_box"), new Item.Properties().tab(VAULT_MOD_GROUP), () -> ModConfigs.MYSTERY_BOX.POOL.getRandom(new Random()).toStack());

    public static BasicItem ACCELERATION_CHIP = new BasicItem(Vault.id("acceleration_chip"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static LootableItem PANDORAS_BOX = new LootableItem(Vault.id("pandoras_box"), new Item.Properties().tab(VAULT_MOD_GROUP), () -> ModConfigs.PANDORAS_BOX.POOL.getRandom(new Random()).toStack());

    //public static BasicItem NETHERITE_CHUNK = new BasicItem(Vault.id("chunk_netherite"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem ALEXANDRITE_CHUNK = new BasicItem(Vault.id("chunk_alexandrite"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem BENITOITE_CHUNK = new BasicItem(Vault.id("chunk_benitoite"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem LARIMAR_CHUNK = new BasicItem(Vault.id("chunk_larimar"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem BLACK_OPAL_CHUNK = new BasicItem(Vault.id("chunk_black_opal"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem PAINITE_CHUNK = new BasicItem(Vault.id("chunk_painite"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem ISKALLIUM_CHUNK = new BasicItem(Vault.id("chunk_iskallium"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem RENIUM_CHUNK = new BasicItem(Vault.id("chunk_renium"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem GORGINITE_CHUNK = new BasicItem(Vault.id("chunk_gorginite"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem SPARKLETINE_CHUNK = new BasicItem(Vault.id("chunk_sparkletine"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem WUTODIE_CHUNK = new BasicItem(Vault.id("chunk_wutodie"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem OMEGA_POG = new BasicItem(Vault.id("omega_pog"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));

    public static BasicItem ETERNAL_SOUL = new BasicItem(Vault.id("eternal_soul"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem SPARK = new BasicItem(Vault.id("spark"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem VOID_CORE = new BasicItem(Vault.id("void_core"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));

    public static BasicItem STAR_SHARD = new BasicItem(Vault.id("star_shard"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem STAR_CORE = new BasicItem(Vault.id("star_core"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static BasicItem STAR_ESSENCE = new BasicItem(Vault.id("star_essence"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));
    public static ItemKnowledgeStar KNOWLEDGE_STAR = new ItemKnowledgeStar(VAULT_MOD_GROUP);

    public static VaultSwordItem SWORD = new VaultSwordItem(Vault.id("sword"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(1));
    public static VaultAxeItem AXE = new VaultAxeItem(Vault.id("axe"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(1));
    public static VaultDaggerItem DAGGER = new VaultDaggerItem(Vault.id("dagger"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(1));
    public static VaultArmorItem HELMET = new VaultArmorItem(Vault.id("helmet"), EquipmentSlotType.HEAD, new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(1));
    public static VaultArmorItem CHESTPLATE = new VaultArmorItem(Vault.id("chestplate"), EquipmentSlotType.CHEST, new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(1));
    public static VaultArmorItem LEGGINGS = new VaultArmorItem(Vault.id("leggings"), EquipmentSlotType.LEGS, new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(1));
    public static VaultArmorItem BOOTS = new VaultArmorItem(Vault.id("boots"), EquipmentSlotType.FEET, new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(1));
    public static EtchingItem ETCHING = new EtchingItem(Vault.id("etching"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(1));
    public static BasicItem ETCHING_FRAGMENT = new BasicItem(Vault.id("etching_fragment"), new Item.Properties().tab(VAULT_MOD_GROUP));

    public static PuzzleRuneItem PUZZLE_RUNE = new PuzzleRuneItem(Vault.id("puzzle_rune"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(1));

    public static BasicItem INFUSED_ETERNAL_SOUL = new BasicItem(Vault.id("infused_eternal_soul"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(64));

    // #Crimson_Fluff
    public static ItemMagnet ITEM_MAGNET = new ItemMagnet(Vault.id("magnet"), new Item.Properties().tab(VAULT_MOD_GROUP).stacksTo(1).durability(128));


    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(VAULT_BURGER);
        registry.register(SKILL_ORB);
        registry.register(ALEXANDRITE_GEM);
        registry.register(BENITOITE_GEM);
        registry.register(LARIMAR_GEM);
        registry.register(BLACK_OPAL_GEM);
        registry.register(PAINITE_GEM);
        registry.register(ISKALLIUM_GEM);
        registry.register(RENIUM_GEM);
        registry.register(GORGINITE_GEM);
        registry.register(SPARKLETINE_GEM);
        registry.register(WUTODIE_GEM);
        registry.register(VAULT_ROCK);
        registry.register(POG);
        registry.register(VAULT_CRYSTAL_NORMAL);
        registry.register(VAULT_CRYSTAL_RARE);
        registry.register(VAULT_CRYSTAL_EPIC);
        registry.register(VAULT_CRYSTAL_OMEGA);
        registry.register(ALEXANDRITE_KEY);
        registry.register(BENITOITE_KEY);
        registry.register(LARIMAR_KEY);
        registry.register(BLACK_OPAL_KEY);
        registry.register(PAINITE_KEY);
        registry.register(ISKALLIUM_KEY);
        registry.register(RENIUM_KEY);
        registry.register(GORGINITE_KEY);
        registry.register(SPARKLETINE_KEY);
        registry.register(WUTODIE_KEY);
        registry.register(SKELLY_KEY);     // #Crimson_Fluff
        registry.register(BIT_100);
        registry.register(BIT_500);
        registry.register(BIT_1000);
        registry.register(BIT_5000);
        registry.register(BIT_10000);
        registry.register(RELIC_BOOSTER_PACK);

        registry.register(DRAGON_HEAD_RELIC);
        registry.register(DRAGON_TAIL_RELIC);
        registry.register(DRAGON_FOOT_RELIC);
        registry.register(DRAGON_CHEST_RELIC);
        registry.register(DRAGON_BREATH_RELIC);
        registry.register(MINERS_DELIGHT_RELIC);
        registry.register(MINERS_LIGHT_RELIC);
        registry.register(PICKAXE_HANDLE_RELIC);
        registry.register(PICKAXE_HEAD_RELIC);
        registry.register(PICKAXE_TOOL_RELIC);
        registry.register(SWORD_BLADE_RELIC);
        registry.register(SWORD_HANDLE_RELIC);
        registry.register(SWORD_STICK_RELIC);
        registry.register(WARRIORS_ARMOUR_RELIC);
        registry.register(WARRIORS_CHARM_RELIC);
        registry.register(DIAMOND_ESSENCE_RELIC);
        registry.register(GOLD_ESSENCE_RELIC);
        registry.register(MYSTIC_GEM_ESSENCE_RELIC);
        registry.register(NETHERITE_ESSENCE_RELIC);
        registry.register(PLATINUM_ESSENCE_RELIC);
        registry.register(CUPCAKE_BLUE_RELIC);
        registry.register(CUPCAKE_LIME_RELIC);
        registry.register(CUPCAKE_PINK_RELIC);
        registry.register(CUPCAKE_PURPLE_RELIC);
        registry.register(CUPCAKE_RED_RELIC);
        registry.register(AIR_RELIC);
        registry.register(SPIRIT_RELIC);
        registry.register(FIRE_RELIC);
        registry.register(EARTH_RELIC);
        registry.register(WATER_RELIC);
        registry.register(TWOLF999_HEAD_RELIC);
        registry.register(TWOLF999_COMBAT_VEST_RELIC);
        registry.register(TWOLF999_COMBAT_LEGGINGS_RELIC);
        registry.register(TWOLF999_COMBAT_GLOVES_RELIC);
        registry.register(TWOLF999_COMBAT_BOOTS_RELIC);
        registry.register(ARMOR_OF_FORBEARANCE_RELIC);
        registry.register(HEART_OF_THE_VOID_RELIC);
        registry.register(NEMESIS_THWARTER_RELIC);
        registry.register(REVERENCE_EDGE_RELIC);
        registry.register(WINGS_OF_EQUITY_RELIC);

        registry.register(VAULT_RUNE);
        registry.register(OBELISK_INSCRIPTION);
        registry.register(TRADER_CORE);
        registry.register(TRADER_CORE_RAFFLE);
        registry.register(TRADER_CORE_OMEGA);
        registry.register(LEGENDARY_TREASURE_NORMAL);
        registry.register(LEGENDARY_TREASURE_RARE);
        registry.register(LEGENDARY_TREASURE_EPIC);
        registry.register(LEGENDARY_TREASURE_OMEGA);
        registry.register(UNIDENTIFIED_ARTIFACT);
        registry.register(NORMAL_GIFT_BOMB);
        registry.register(SUPER_GIFT_BOMB);
        registry.register(MEGA_GIFT_BOMB);
        registry.register(OMEGA_GIFT_BOMB);
        registry.register(VAULT_RELIC);
        registry.register(TWITCH_EMOTE1_RELIC);
        registry.register(TWITCH_EMOTE2_RELIC);
        registry.register(TWITCH_EMOTE3_RELIC);
        registry.register(TWITCH_EMOTE4_RELIC);
        registry.register(TWITCH_EMOTE5_RELIC);
        registry.register(SKILL_ORB_FRAME);
        registry.register(SKILL_SHARD);
        registry.register(BITTER_LEMON);
        registry.register(SOUR_ORANGE);
        registry.register(MYSTIC_PEAR);
        registry.register(KEY_PIECE);
        registry.register(KEY_MOULD);
        registry.register(BLANK_KEY);
        registry.register(NETHERITE_CLUSTER);
        registry.register(ALEXANDRITE_CLUSTER);
        registry.register(BENITOITE_CLUSTER);
        registry.register(LARIMAR_CLUSTER);
        registry.register(BLACK_OPAL_CLUSTER);
        registry.register(PAINITE_CLUSTER);
        registry.register(ISKALLIUM_CLUSTER);
        registry.register(RENIUM_CLUSTER);
        registry.register(GORGINITE_CLUSTER);
        registry.register(SPARKLETINE_CLUSTER);
        registry.register(WUTODIE_CLUSTER);

        registry.register(VAULT_STEW_MYSTERY);
        registry.register(VAULT_STEW_NORMAL);
        registry.register(VAULT_STEW_RARE);
        registry.register(VAULT_STEW_EPIC);
        registry.register(VAULT_STEW_OMEGA);
        registry.register(POISONOUS_MUSHROOM);
        registry.register(VAULT_GOLD);
        registry.register(VAULT_DIAMOND);
        registry.register(SKILL_ESSENCE);
        registry.register(OOZING_PIZZA);
        registry.register(UNIDENTIFIED_RELIC);
        registry.register(SWEET_KIWI);
        registry.register(HUNTER_EYE);
        registry.register(BURGER_PATTY);
        registry.register(BURGER_BUN);
        registry.register(MATURE_CHEDDAR);
        registry.register(MYSTIC_TOMATO);
        registry.register(VIAL_OF_SAND);
        registry.register(VAULT_SCRAP);
        registry.register(VAULT_INGOT);
        registry.register(VAULT_PLATINUM);
        registry.register(MYSTERY_BOX);

        registry.register(ACCELERATION_CHIP);
        registry.register(PANDORAS_BOX);

        registry.register(ALEXANDRITE_CHUNK);
        registry.register(BENITOITE_CHUNK);
        registry.register(LARIMAR_CHUNK);
        registry.register(BLACK_OPAL_CHUNK);
        registry.register(PAINITE_CHUNK);
        registry.register(ISKALLIUM_CHUNK);
        registry.register(RENIUM_CHUNK);
        registry.register(GORGINITE_CHUNK);
        registry.register(SPARKLETINE_CHUNK);
        registry.register(WUTODIE_CHUNK);
        registry.register(OMEGA_POG);

        registry.register(ETERNAL_SOUL);
        registry.register(SPARK);
        registry.register(VOID_CORE);

        registry.register(STAR_SHARD);
        registry.register(STAR_CORE);
        registry.register(STAR_ESSENCE);
        registry.register(KNOWLEDGE_STAR);

        registry.register(SWORD);
        registry.register(AXE);
        registry.register(DAGGER);
        registry.register(HELMET);
        registry.register(CHESTPLATE);
        registry.register(LEGGINGS);
        registry.register(BOOTS);
        registry.register(ETCHING);
        registry.register(ETCHING_FRAGMENT);

        registry.register(PUZZLE_RUNE);
        registry.register(INFUSED_ETERNAL_SOUL);

        registry.register(ITEM_MAGNET);     // #Crimson_Fluff
    }
}
