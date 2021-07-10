package iskallia.vault.world.raid.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class TimerModifier extends VaultModifier {

    @Expose
    private final int timerAddend;

    public TimerModifier(String name, ResourceLocation icon, int timerAddend) {
        super(name, icon);
        this.timerAddend = timerAddend;

        Vault.LOGGER.info("TRANS: " + new TranslationTextComponent("tip.the_vault.modifier_nothing").getString());

        if (timerAddend == 0)
            this.format(this.getColor(), new TranslationTextComponent("tip.the_vault.modifier_nothing").getString());
        else
            this.format(this.getColor(), new TranslationTextComponent(this.timerAddend > 0
                ? "tip.the_vault.modifier_timer_add"
                : "tip.the_vault.modifier_timer_remove"
                , (this.timerAddend / 20)).getString());

//        if (this.timerAddend > 0) {
//            this.format(this.getColor(), new TranslationTextComponent("item.the_vault.modifier_timer_add", (this.timerAddend / 20)).getString());
//        } else if (this.timerAddend < 0) {
//            this.format(this.getColor(), new TranslationTextComponent("item.the_vault.modifier_timer_remove", (this.timerAddend / 20)).getString());
//        } else {
//            this.format(this.getColor(), new TranslationTextComponent("item.the_vault.modifier_nothing").getString());
//        }
    }

    @Override
    public void apply(VaultRaid raid) {
        raid.sTickLeft += this.timerAddend;
        raid.ticksLeft += this.timerAddend;
    }

}
