package iskallia.vault.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class ItemSkillOrbFrame extends Item {

    public ItemSkillOrbFrame(ItemGroup group, ResourceLocation id) {
        super(new Properties()
            .tab(group)
            .stacksTo(64));

        this.setRegistryName(id);
    }

}
