package iskallia.vault.world.data;

import iskallia.vault.Vault;
import iskallia.vault.init.ModFeatures;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.init.ModStructures;
import iskallia.vault.item.CrystalData;
import iskallia.vault.item.ItemVaultCrystal;
import iskallia.vault.network.message.ObeliskVH2OverlayUpdate;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VaultRaidData extends WorldSavedData {

    protected static final String DATA_NAME = Vault.MOD_ID + "_VaultRaid";

    private final Map<UUID, VaultRaid> activeRaids = new HashMap<>();
    private int xOffset = 0;

    public VaultRaidData() {
        this(DATA_NAME);
    }

    public VaultRaidData(String name) {
        super(name);
    }

    public VaultRaid getAt(BlockPos pos) {
        return this.activeRaids.values().stream().filter(raid -> raid.box.isInside(pos)).findFirst().orElse(null);
    }

    public void remove(ServerWorld server, UUID playerId) {
        VaultRaid v = this.activeRaids.remove(playerId);

        if (v != null) {
            v.ticksLeft = 0;
//            v.finish(server, playerId);
        }
    }

    public VaultRaid getActiveFor(ServerPlayerEntity player) {
        return this.activeRaids.get(player.getUUID());
    }

    public VaultRaid startNew(ServerPlayerEntity player, ItemVaultCrystal crystal, boolean isFinalVault) {
        return this.startNew(player, crystal.getRarity().ordinal(), "", new CrystalData(null), isFinalVault);
    }

    public VaultRaid startNew(ServerPlayerEntity player, int rarity, String playerBossName, CrystalData data, boolean isFinalVault) {
        return this.startNew(Collections.singletonList(player), Collections.emptyList(), rarity, playerBossName, data, isFinalVault);
    }

    public VaultRaid startNew(List<ServerPlayerEntity> players, List<ServerPlayerEntity> spectators, int rarity, String playerBossName, CrystalData data, boolean isFinalVault) {
        int level = players.stream()
            .map(player -> PlayerVaultStatsData.get(player.getLevel()).getVaultStats(player).getVaultLevel())
            .max(Integer::compareTo).get();

        VaultRaid raid = new VaultRaid(players, spectators, new MutableBoundingBox(
            this.xOffset, 0, 0, this.xOffset += VaultRaid.REGION_SIZE, 256, VaultRaid.REGION_SIZE
        ), level, rarity, playerBossName);

        raid.isFinalVault = isFinalVault;

        players.forEach(player -> {
            if (this.activeRaids.containsKey(player.getUUID())) {
                this.activeRaids.get(player.getUUID()).ticksLeft = 0;
            }
        });

        // #Crimson_Fluff
        // TODO: instead of creating a RaidData for every player, create 1 RaidData with coop players
        // 1 vault will have activeRaids() for every coop player, so 1 vault could have 10 entries in WorldData.activeRaids
        // 1 vault should be 1 activeRaid, with each coop player in a list inside that activeRaid

//        raid.getPlayerIds().forEach(uuid -> {
//            this.activeRaids.put(uuid, raid);
//        });

        this.activeRaids.put(players.get(0).getUUID(), raid);
        this.setDirty();

        ServerWorld world = players.get(0).getServer().getLevel(Vault.VAULT_KEY);

        // #Crimson_Fluff, moved from VaultRaid.start
        // Lag is caused when generating the vault, so put messages here... before the lag
        // VaultRaidData.startNew happens before VaultRaid.start !

        raid.modifiers.generate(world.getRandom(), raid.level, raid.playerBossName != null && !raid.playerBossName.isEmpty());
        data.apply(raid, world.getRandom());
        raid.modifiers.apply();

        StringTextComponent text = new StringTextComponent("");
        AtomicBoolean startsWithVowel = new AtomicBoolean(false);

        raid.modifiers.forEach((i, modifier) -> {
            StringTextComponent s = new StringTextComponent(modifier.getName());

            s.setStyle(Style.EMPTY.withColor(Color.fromRgb(modifier.getColor()))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new StringTextComponent(modifier.getName())
                        .setStyle(Style.EMPTY.withColor(Color.fromRgb(modifier.getColor())))
                        .append(new StringTextComponent("\n\n" + modifier.getDescription()).withStyle(TextFormatting.WHITE)))));

            text.append(s);

            if (i == 0) {
                char c = modifier.getName().charAt(0);  // Rarity is UpperCase
                startsWithVowel.set(c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U');
            }

            text.append(", ");
        });

        // if no modifiers then get vowel of Raid Rarity (COMMON, RARE, EPIC, OMEGA)
        if (raid.modifiers.size() == 0) {
            char c = VaultRarity.values()[raid.rarity].name().charAt(0);  // Rarity is UpperCase
            startsWithVowel.set(c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U');
        }
//        else text.append(new StringTextComponent(" "));

        TranslationTextComponent prefix = new TranslationTextComponent(startsWithVowel.get() ? "tip.the_vault.entered_an" : "tip.the_vault.entered_a");

        // keep name default, Rarity is uppercase
        String rarityName = VaultRarity.values()[raid.rarity].name();

        text.append(new StringTextComponent(rarityName).withStyle(VaultRarity.values()[raid.rarity].color));
        text.append(new TranslationTextComponent("tip.the_vault.vault"));
        prefix.withStyle(TextFormatting.WHITE);
        text.withStyle(TextFormatting.WHITE);

        players.forEach(player -> {
            IFormattableTextComponent playerName = player.getDisplayName().copy();
            playerName.withStyle(TextFormatting.GREEN);

            player.displayClientMessage(playerName.append(prefix).append(text), false);
            ModNetwork.CHANNEL.sendTo(new ObeliskVH2OverlayUpdate(raid.obelisksActivated, raid.obelisksNeeded), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        });

        world.getServer().getPlayerList().broadcastMessage(new TranslationTextComponent("tip.the_vault.generating").setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_ddd01e))), ChatType.CHAT, Util.NIL_UUID);  // #Crimson_Fluff
        // #Crimson_Fluff END


        players.get(0).getServer().submit(() -> {
            try {
                ChunkPos chunkPos = new ChunkPos((raid.box.x0 + raid.box.getXSpan() / 2) >> 4, (raid.box.z0 + raid.box.getZSpan() / 2) >> 4);

                StructureSeparationSettings settings = new StructureSeparationSettings(1, 0, - 1);

                StructureStart<?> start = (raid.isFinalVault ? ModFeatures.FINAL_VAULT_FEATURE : ModFeatures.VAULT_FEATURE).generate(world.registryAccess(),
                    world.getChunkSource().generator, world.getChunkSource().generator.getBiomeSource(),
                    world.getStructureManager(), world.getSeed(), chunkPos,
                    BiomeRegistry.PLAINS, 0, settings);

                //This is some cursed calculations, don't ask me how it works.
                int chunkRadius = VaultRaid.REGION_SIZE >> 5;

                for (int x = - chunkRadius; x <= chunkRadius; x += 17) {
                    for (int z = - chunkRadius; z <= chunkRadius; z += 17) {
                        world.getChunk(chunkPos.x + x, chunkPos.z + z, ChunkStatus.EMPTY, true).setStartForFeature(ModStructures.VAULT, start);
                    }
                }

                raid.start(world, chunkPos, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return raid;
    }

    public void tick(ServerWorld world) {
        this.activeRaids.values().forEach(vaultRaid -> vaultRaid.tick(world));

        boolean removed = false;

        List<Runnable> tasks = new ArrayList<>();

        for (VaultRaid raid : this.activeRaids.values()) {
            if (raid.isComplete()) {
                raid.syncTicksLeft(world.getServer());
                tasks.add(() -> raid.playerIds.forEach(uuid -> this.remove(world, uuid)));
                removed = true;
            }
        }

        tasks.forEach(Runnable::run);

        if (removed || this.activeRaids.size() > 0) this.setDirty();
    }

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START && event.world.dimension() == Vault.VAULT_KEY) {
            VaultRaidData vrd = get((ServerWorld) event.world);
            vrd.tick((ServerWorld) event.world);
        }
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.activeRaids.clear();

        nbt.getList("ActiveRaids", Constants.NBT.TAG_COMPOUND).forEach(raidNBT -> {
            VaultRaid raid = VaultRaid.fromNBT((CompoundNBT) raidNBT);
            raid.getPlayerIds().forEach(uuid -> this.activeRaids.put(uuid, raid));
        });

        this.xOffset = nbt.getInt("XOffset");
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        ListNBT raidsList = new ListNBT();
        this.activeRaids.values().forEach(raid -> raidsList.add(raid.serializeNBT()));
        nbt.put("ActiveRaids", raidsList);

        nbt.putInt("XOffset", this.xOffset);
        return nbt;
    }

    public static VaultRaidData get(ServerWorld world) {
        return world.getServer().overworld().getDataStorage().computeIfAbsent(VaultRaidData::new, DATA_NAME);
    }
}
