package iskallia.vault.entity;

import iskallia.vault.config.VaultFightersConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class VaultFighterEntity extends FighterEntity {

    public VaultFighterEntity(EntityType<? extends ZombieEntity> type, World world) {
        super(type, world);
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData spawnData, CompoundNBT dataTag) {
        ILivingEntityData livingData = super.finalizeSpawn(world, difficulty, reason, spawnData, dataTag);

        if (! this.level.isClientSide) {
            VaultRaid raid = VaultRaidData.get((ServerWorld) this.level).getAt(this.blockPosition());

//            if (raid != null) {
//                ServerPlayerEntity player = ((ServerWorld) world).getServer().getPlayerList().getPlayer(raid.playerIds.get(0));
//                String name = player != null ? player.getName().getString() : "";
//                this.setCustomName(new StringTextComponent(name));
//            }

            if (raid != null) {
                String name = null;

                // Resolves a risk of incorrect input value.
                if (ModConfigs.VAULT_FIGHTERS.POOL_MODE == null)
                    ModConfigs.VAULT_FIGHTERS.POOL_MODE = VaultFightersConfig.Mode.PLAYER;

                switch (ModConfigs.VAULT_FIGHTERS.POOL_MODE) {
                    case ONLINE_PLAYERS: {
                        // Choose random name from online player list.
                        String[] onlinePlayerNames = ((ServerWorld) world).getServer().getPlayerList().getPlayerNamesArray();
                        if (onlinePlayerNames.length != 0)
                            name = onlinePlayerNames[world.getRandom().nextInt(onlinePlayerNames.length)];

                        break;
                    }
                    case WHITELIST: {
                        // Choose random name from whitelist.
                        String[] whitelistedPlayerNames = ((ServerWorld) world).getServer().getPlayerList().getWhiteListNames();
                        if (whitelistedPlayerNames.length != 0)
                            name = whitelistedPlayerNames[world.getRandom().nextInt(whitelistedPlayerNames.length)];

                        break;
                    }
                    case LIST: {
                        // Choose random name from fighter list.
                        if (ModConfigs.VAULT_FIGHTERS.FIGHTER_LIST.size() != 0)
                            name = ModConfigs.VAULT_FIGHTERS.FIGHTER_LIST.get(world.getRandom().nextInt(ModConfigs.VAULT_FIGHTERS.FIGHTER_LIST.size()));

                        break;
                    }
                    default: {
                        break;
                    }
                }

                if (name == null) {
                    // Use raid player name.
                    ServerPlayerEntity player = ((ServerWorld) world).getServer().getPlayerList().getPlayer(raid.playerIds.get(0));
                    name = player != null ? player.getName().getString() : "";
                }

                this.setCustomName(new StringTextComponent(name));
            }
        }

        return livingData;
    }

}
