package iskallia.vault.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.Constants;

public class PlayerArenaCap {
    private CompoundNBT NBT = new CompoundNBT();

    public PlayerArenaCap() { }

    public void copyFrom(PlayerArenaCap source) { NBT = source.NBT; }
    public void saveNBTData(CompoundNBT compound) { compound.merge(NBT); }
    public void loadNBTData(CompoundNBT compound) { NBT = compound; }

    public void arenaLevelSet(int level) { NBT.putInt("jarLevel", level); }
    public void arenaLevelAdd(int level) { NBT.putInt("jarLevel", NBT.getInt("jarLevel") + level); }
    public int arenaLevelGet() {
        return NBT.getInt("jarLevel");
    }

    public void arenaSubsAdd(String name) {
        ListNBT subs = NBT.getList("subs", Constants.NBT.TAG_STRING);
        subs.add(StringNBT.valueOf(name));

        NBT.put("subs", subs);
    }

    public void arenaSubsClear() {
        NBT.remove("subs");
    }

    public int arenaSubsCount() {
        return NBT.getList("subs", Constants.NBT.TAG_STRING).size();
    }

    public ListNBT arenaSubsList() {
        return NBT.getList("subs", Constants.NBT.TAG_STRING);
    }
}
