package iskallia.vault.world.data;

import iskallia.vault.Vault;
import iskallia.vault.altar.AltarInfusionRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerVaultAltarData extends WorldSavedData {

    protected static final String DATA_NAME = Vault.MOD_ID + "_PlayerAltarRecipes";

    private Map<UUID, AltarInfusionRecipe> playerMap = new HashMap<>();

    public PlayerVaultAltarData() {
        super(DATA_NAME);
    }

    public PlayerVaultAltarData(String name) {
        super(name);
    }

    public static PlayerVaultAltarData get(ServerWorld world) {
        return world.getServer().overworld()
            .getDataStorage().computeIfAbsent(PlayerVaultAltarData::new, DATA_NAME);
    }

    public AltarInfusionRecipe getRecipe(PlayerEntity player) {
        return this.getRecipe(player.getUUID());
    }

    public AltarInfusionRecipe getRecipe(UUID uuid) {
        return this.playerMap.get(uuid);
    }

    public AltarInfusionRecipe getRecipe(ServerWorld world, PlayerEntity player) {
        AltarInfusionRecipe recipe = this.playerMap.computeIfAbsent(player.getUUID(), k -> new AltarInfusionRecipe(world, player));
        this.setDirty();
        return recipe;
    }

    public boolean hasRecipe(UUID uuid) {
        return this.playerMap.containsKey(uuid);
    }


    /* ---------------------------------------------- */


    public PlayerVaultAltarData add(UUID uuid, AltarInfusionRecipe recipe) {
        this.playerMap.put(uuid, recipe);

        setDirty();
        return this;
    }

    public PlayerVaultAltarData remove(UUID uuid) {
        this.playerMap.remove(uuid);

        setDirty();
        return this;
    }

    public PlayerVaultAltarData update(UUID id, AltarInfusionRecipe recipe) {
        this.remove(id);
        this.add(id, recipe);


        setDirty();
        return this;
    }

    @Override
    public void load(CompoundNBT nbt) {
        ListNBT playerList = nbt.getList("PlayerEntries", Constants.NBT.TAG_STRING);
        ListNBT recipeList = nbt.getList("AltarRecipeEntries", Constants.NBT.TAG_COMPOUND);

        if (playerList.size() != recipeList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }

        for (int i = 0; i < playerList.size(); i++) {
            UUID playerUUID = UUID.fromString(playerList.getString(i));
            playerMap.put(playerUUID, AltarInfusionRecipe.deserialize(recipeList.getCompound(i)));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        ListNBT playerList = new ListNBT();
        ListNBT recipeList = new ListNBT();

        this.playerMap.forEach((uuid, recipe) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));
            recipeList.add(AltarInfusionRecipe.serialize(recipe));
        });

        nbt.put("PlayerEntries", playerList);
        nbt.put("AltarRecipeEntries", recipeList);

        return nbt;
    }

}
