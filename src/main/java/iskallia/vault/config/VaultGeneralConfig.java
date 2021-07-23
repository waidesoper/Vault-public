package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VaultGeneralConfig extends Config {
    @Expose public int TICK_COUNTER;
    @Expose public int NO_EXIT_CHANCE;
    @Expose private List<String> ITEM_BLACKLIST;
    @Expose private List<String> BLOCK_BLACKLIST;
//    @Expose private Map<Integer, String> MONTHS_TO_TAG_COLOR;
    @Expose public float VAULT_EXIT_XP_MIN_PERCENT;
    @Expose public float VAULT_EXIT_XP_MAX_PERCENT;

    @Expose public String VAULT_FINAL_EVENT_DATE;
    @Expose public int VAULT_PLAYER_XP;

    @Override
    public String getName() {
        return "vault_general";
    }

    @Override
    protected void reset() {
        this.TICK_COUNTER = 20 * 60 * 25;
        this.NO_EXIT_CHANCE = 10;

        this.VAULT_PLAYER_XP = 10000;						// #Crimson_Fluff, moved here from playerxp config
        this.VAULT_FINAL_EVENT_DATE = "2021-09-21 13:00";	// #Crimson_Fluff, moved here

        this.ITEM_BLACKLIST = new ArrayList<>();
        this.ITEM_BLACKLIST.add(Items.ENDER_CHEST.getRegistryName().toString());
        this.ITEM_BLACKLIST.add(ModItems.SKILL_ORB.getRegistryName().toString());			// #Crimson_Fluff, added these Blocks/Items from modpack
        this.ITEM_BLACKLIST.add(ModItems.KNOWLEDGE_STAR.getRegistryName().toString());
        this.ITEM_BLACKLIST.add(ModItems.KEY_PIECE.getRegistryName().toString());

        this.BLOCK_BLACKLIST = new ArrayList<>();
        this.BLOCK_BLACKLIST.add(Blocks.ENDER_CHEST.getRegistryName().toString());
        this.BLOCK_BLACKLIST.add(ModBlocks.KEY_PRESS.getRegistryName().toString());

//        this.MONTHS_TO_TAG_COLOR = new LinkedHashMap<>();
//        this.MONTHS_TO_TAG_COLOR.put(1, TextFormatting.AQUA.name());
//        this.MONTHS_TO_TAG_COLOR.put(7, TextFormatting.DARK_AQUA.name());
//        this.MONTHS_TO_TAG_COLOR.put(13, TextFormatting.YELLOW.name());
//        this.MONTHS_TO_TAG_COLOR.put(25, TextFormatting.GOLD.name());
//        this.MONTHS_TO_TAG_COLOR.put(48, TextFormatting.RED.name());

        this.VAULT_EXIT_XP_MIN_PERCENT = 0.1F;		// #Crimson_Fluff, values from the modpack
        this.VAULT_EXIT_XP_MAX_PERCENT = 0.8F;
    }

//    public TextFormatting getTagFormat(int months) {
//        for (Map.Entry<Integer, String> e : this.MONTHS_TO_TAG_COLOR.entrySet())
//            if (months >= e.getKey()) return TextFormatting.valueOf(e.getValue());
//
//        return TextFormatting.WHITE;
//    }

    @SubscribeEvent
    public static void cancelItemInteraction(PlayerInteractEvent event) {
        if (event.getPlayer().level.dimension() != Vault.VAULT_KEY) return;

        if (ModConfigs.VAULT_GENERAL.ITEM_BLACKLIST.contains(event.getItemStack().getItem().getRegistryName().toString())) {
            if (event.isCancelable())
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void cancelBlockInteraction(PlayerInteractEvent event) {
        if (event.getPlayer().level.dimension() != Vault.VAULT_KEY) return;
        BlockState state = event.getWorld().getBlockState(event.getPos());

        if (ModConfigs.VAULT_GENERAL.BLOCK_BLACKLIST.contains(state.getBlock().getRegistryName().toString())) {
            if (event.isCancelable())
                event.setCanceled(true);
        }
    }
}
