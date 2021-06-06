package iskallia.vault.world.raid.modifier;

import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class NoExitModifier extends VaultModifier {

    public NoExitModifier(String name, ResourceLocation icon) {
        super(name, icon);
        this.format(this.getColor(), new TranslationTextComponent("tip.the_vault.modifier_noexit").getString());
    }

    @Override
    public void apply(VaultRaid raid) {
        raid.cannotExit = true;
    }

}
