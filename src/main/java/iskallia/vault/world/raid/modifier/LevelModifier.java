package iskallia.vault.world.raid.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class LevelModifier extends VaultModifier {

    @Expose
    private final int levelAddend;

    public LevelModifier(String name, ResourceLocation icon, int levelAddend) {
        super(name, icon);
        this.levelAddend = levelAddend;

        if (this.levelAddend > 0) {
            this.format(this.getColor(), "Pushes the vault " + this.levelAddend + (this.levelAddend == 1 ? " level higher." : " levels higher."));
        } else if (this.levelAddend < 0) {
            this.format(this.getColor(), "Pushes the vault " + - this.levelAddend + (- this.levelAddend == 1 ? " level lower." : " levels lower."));
        } else {
            this.format(this.getColor(), new TranslationTextComponent("tip.the_vault.modifier_nothing").getString());
        }
    }

    @Override
    public void apply(VaultRaid raid) {
        raid.level += this.levelAddend;
    }

}
