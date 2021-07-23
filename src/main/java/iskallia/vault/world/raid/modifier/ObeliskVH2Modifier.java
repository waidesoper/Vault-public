package iskallia.vault.world.raid.modifier;

import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class ObeliskVH2Modifier extends VaultModifier {
	public ObeliskVH2Modifier(String name, ResourceLocation icon) {
		super(name, icon);
		this.format(this.getColor(), "Go find some Obelisks, VH2 style !");
	}

	@Override
	public void apply(VaultRaid raid) {
		raid.obelisksNeeded = new Random().nextInt(4) + 1;
		raid.obelisksActivated = 0;
	}
}
