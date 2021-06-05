package iskallia.vault.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class ItemSkellingtonKey extends Item {
    public ItemSkellingtonKey(ItemGroup group, ResourceLocation id) {
        super(new Properties()
            .group(group)
            .maxStackSize(1));

        this.setRegistryName(id);
    }
}
