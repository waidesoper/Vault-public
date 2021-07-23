package iskallia.vault.event;

import iskallia.vault.Vault;
import iskallia.vault.capabilities.PlayerArenaCap;
import iskallia.vault.capabilities.PlayerArenaCapProperties;
import iskallia.vault.capabilities.PlayerArenaCapPropertiesDispatcher;
import iskallia.vault.entity.EternalEntity;
import iskallia.vault.entity.FighterEntity;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.FighterSizeMessage;
import iskallia.vault.network.message.JarOverlayUpdateWithLevel;
import iskallia.vault.network.message.ObeliskVH2OverlayUpdate;
import iskallia.vault.network.message.VaultInfoMessage;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerResearchesData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {

    public static boolean NATURAL_REGEN_OLD_VALUE = false;
    public static boolean MODIFIED_GAMERULE = false;

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (target.level.isClientSide) return;

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

        if (target instanceof FighterEntity)
            ModNetwork.CHANNEL.sendTo(new FighterSizeMessage(target, ((FighterEntity) target).sizeMultiplier), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        else if (target instanceof EternalEntity)
            ModNetwork.CHANNEL.sendTo(new FighterSizeMessage(target, ((EternalEntity) target).sizeMultiplier), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.CLIENT) return;

        RegistryKey<World> dimensionKey = event.player.level.dimension();
        GameRules gameRules = event.player.level.getGameRules();

        if (MODIFIED_GAMERULE && dimensionKey != Vault.VAULT_KEY) {
            gameRules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(NATURAL_REGEN_OLD_VALUE, event.player.getServer());
            MODIFIED_GAMERULE = false;
            return;
        }

        if (dimensionKey != Vault.VAULT_KEY) return;

        if (event.phase == TickEvent.Phase.START) {
            NATURAL_REGEN_OLD_VALUE = gameRules.getBoolean(GameRules.RULE_NATURAL_REGENERATION);
            gameRules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, event.player.getServer());
            MODIFIED_GAMERULE = true;

        } else if (event.phase == TickEvent.Phase.END) {
            gameRules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(NATURAL_REGEN_OLD_VALUE, event.player.getServer());
            MODIFIED_GAMERULE = false;
        }
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        if (event.getPlayer().level.isClientSide) return;

        int level = PlayerVaultStatsData.get((ServerWorld) event.getPlayer().level).getVaultStats(event.getPlayer()).getVaultLevel();
        ItemStack stack = event.getPlayer().getMainHandItem();

        if (ModAttributes.MIN_VAULT_LEVEL.exists(stack)
            && level < ModAttributes.MIN_VAULT_LEVEL.get(stack).get().getValue(stack)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick2(TickEvent.PlayerTickEvent event) {
        if (event.player.level.isClientSide) return;
        EquipmentSlotType[] slots = {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};

        for (EquipmentSlotType slot : slots) {
            ItemStack stack = event.player.getItemBySlot(slot);
            int level = PlayerVaultStatsData.get((ServerWorld) event.player.level).getVaultStats(event.player).getVaultLevel();

            if (ModAttributes.MIN_VAULT_LEVEL.exists(stack)
                && level < ModAttributes.MIN_VAULT_LEVEL.get(stack).get().getValue(stack)) {
                event.player.drop(stack.copy(), false, false);
                stack.setCount(0);
            }
        }
    }

    // #Crimson_Fluff
    @SubscribeEvent
    public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity) {
            if (! event.getObject().getCapability(PlayerArenaCapProperties.PLAYER_CAPABILITY).isPresent()) {
                event.addCapability(new ResourceLocation(Vault.MOD_ID, "arena"), new PlayerArenaCapPropertiesDispatcher());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            LazyOptional<PlayerArenaCap> capability = event.getOriginal().getCapability(PlayerArenaCapProperties.PLAYER_CAPABILITY);

            capability.ifPresent(oldPlayer -> {
                event.getPlayer().getCapability(PlayerArenaCapProperties.PLAYER_CAPABILITY).ifPresent(newPlayer -> {
                    newPlayer.copyFrom(oldPlayer);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ServerWorld serverWorld = player.getLevel();
        MinecraftServer server = player.getServer();
        PlayerVaultStatsData.get(serverWorld).getVaultStats(player).sync(server);
        PlayerResearchesData.get(serverWorld).getResearches(player).sync(server);
        PlayerAbilitiesData.get(serverWorld).getAbilities(player).sync(server);

        player.getCapability(PlayerArenaCapProperties.PLAYER_CAPABILITY,null).ifPresent(cap -> {
            ModNetwork.CHANNEL.sendTo(new JarOverlayUpdateWithLevel(cap.arenaLevelGet()), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        });

        // #Crimson_Fluff, this sets the CLIENT that VaultRaidOverlay needs to display the modifiers, when re-entering the vault
        // also update found obelisks if in VH2 mode
        VaultRaid raid = VaultRaidData.get(player.getLevel()).getActiveFor(player);
        if (raid != null) {
            ModNetwork.CHANNEL.sendTo(new VaultInfoMessage(raid), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            ModNetwork.CHANNEL.sendTo(new ObeliskVH2OverlayUpdate(raid.obelisksActivated, raid.obelisksNeeded), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }
}
