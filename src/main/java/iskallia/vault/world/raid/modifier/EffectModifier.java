package iskallia.vault.world.raid.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.type.EffectAbility;
import iskallia.vault.skill.talent.type.EffectTalent;
import iskallia.vault.util.RomanNumber;
import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class EffectModifier extends VaultModifier {

    @Expose
    private final String effect;
    @Expose
    private final int value;
    @Expose
    private final String operator;
    @Expose
    private final String type;

    public EffectModifier(String name, ResourceLocation icon, Effect effect, int value, String operator, EffectAbility.Type type) {
        this(name, icon, ForgeRegistries.POTIONS.getKey(effect).toString(), value, operator, type.toString());
    }

    public EffectModifier(String name, ResourceLocation icon, String effect, int value, String operator, String type) {
        super(name, icon);
        this.effect = effect;
        this.value = value;
        this.operator = operator;
        this.type = type;

        switch (this.operator) {
            case "MULTIPLY":
                this.format(this.getColor(), "Multiplies the current " + new ResourceLocation(this.effect).getPath() + " amplifier by " + this.value + ".");
                break;

            case "ADD":
                this.format(this.getColor(), "Adds " + this.value + " to the current " + new ResourceLocation(this.effect).getPath() + " amplifier.");
                break;

            case "SET":
                this.format(this.getColor(), "Gives " + new ResourceLocation(this.effect).getPath() + " " + RomanNumber.toRoman(this.value) + ".");
                break;

            default:
                this.format(this.getColor(), new TranslationTextComponent("tip.the_vault.modifier_nothing").getString());
                break;
        }
    }

    public Effect getEffect() { return ForgeRegistries.POTIONS.getValue(new ResourceLocation(this.effect)); }

    public int getAmplifier(int oldValue) {
        switch (this.operator) {
            case "MULTIPLY":
                return this.value * oldValue;

            case "ADD":
                return this.value + oldValue;

            case "SET":
                return this.value;

            default:
                return oldValue;
        }
    }

    public EffectTalent.Type getType() {
        return EffectTalent.Type.fromString(this.type);
    }

    @Override
    public void apply(VaultRaid raid) { }

    @Override
    public void tick(ServerWorld world, PlayerEntity player) {
        Tuple<Integer, EffectTalent> data = EffectTalent.getData(player, world, this.getEffect());
        if (data.getA() < 0) return;

        EffectInstance activeEffect = player.getEffect(this.getEffect());
        int newAmplifier = this.getAmplifier(data.getA());

        if (newAmplifier >= 0) {
            EffectInstance newEffect = new EffectInstance(this.getEffect(), 100, newAmplifier,
                false, data.getB().getType().showParticles, data.getB().getType().showIcon);

            player.addEffect(newEffect);
        } else if (activeEffect != null) {
            player.removeEffect(activeEffect.getEffect());
        }
    }

    public enum Type {
        HIDDEN("hidden", false, false),
        PARTICLES_ONLY("particles_only", true, false),
        ICON_ONLY("icon_only", false, true),
        ALL("all", true, true);

        private static final Map<String, Type> STRING_TO_TYPE = Arrays.stream(values())
            .collect(Collectors.toMap(Type::toString, o -> o));

        private final String name;
        public final boolean showParticles;
        public final boolean showIcon;

        Type(String name, boolean showParticles, boolean showIcon) {
            this.name = name;
            this.showParticles = showParticles;
            this.showIcon = showIcon;
        }

        public static Type fromString(String type) {
            return STRING_TO_TYPE.get(type);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
