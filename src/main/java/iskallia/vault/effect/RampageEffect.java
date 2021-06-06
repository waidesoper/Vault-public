package iskallia.vault.effect;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.type.RampageAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;

public class RampageEffect extends Effect {

    public AttributeModifier[] attributeModifiers;

    public RampageEffect(EffectType typeIn, int liquidColorIn, ResourceLocation id) {
        super(typeIn, liquidColorIn);
        //this.attributeModifiers = new AttributeModifier[ModConfigs.ABILITIES.RAMPAGE.getMaxLevel()];

        setRegistryName(id);

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void addAttributeModifiers(LivingEntity livingEntity, AttributeModifierManager attributeMapIn, int amplifier) {
        RampageAbility rampageAbility = ModConfigs.ABILITIES.RAMPAGE.getAbility(amplifier + 1);

        initializeAttributeModifiers();

        if (rampageAbility == null) return;

        int damageIncrease = rampageAbility.getDamageIncrease();
        ModifiableAttributeInstance damage = livingEntity.getAttribute(Attributes.ATTACK_DAMAGE);

        if (damage != null) {
            this.attributeModifiers[amplifier] = new AttributeModifier(this.getRegistryName().toString(), damageIncrease, AttributeModifier.Operation.ADDITION);
            damage.addTransientModifier(this.attributeModifiers[amplifier]);
        }
        super.addAttributeModifiers(livingEntity, attributeMapIn, amplifier);
    }

    private void initializeAttributeModifiers() {
        this.attributeModifiers = new AttributeModifier[ModConfigs.ABILITIES.RAMPAGE.getMaxLevel()];
        for (int i = 0; i < this.attributeModifiers.length; i++) {
            this.attributeModifiers[i] = new AttributeModifier(this.getRegistryName().toString(),
                (i + 1) * 0.2f, AttributeModifier.Operation.ADDITION);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeModifierManager attributeMapIn, int amplifier) {
        ModifiableAttributeInstance damage = livingEntity.getAttribute(Attributes.ATTACK_DAMAGE);

        if (damage != null) {
            damage.removeModifier(this.attributeModifiers[amplifier].getId());
        }

        super.removeAttributeModifiers(livingEntity, attributeMapIn, amplifier);
    }

}
