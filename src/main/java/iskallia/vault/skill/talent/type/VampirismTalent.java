package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.set.SetNode;
import iskallia.vault.skill.set.SetTree;
import iskallia.vault.skill.set.VampirismSet;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.world.data.PlayerSetsData;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class VampirismTalent extends PlayerTalent {

    @Expose private final float leechRatio;

    public VampirismTalent(int cost, float leechRatio) {
        super(cost);
        this.leechRatio = leechRatio;
    }

    public float getLeechRatio() {
        return this.leechRatio;
    }

    // #Crimson_Fluff, duplicated method ?
//    public void onDamagedEntity(PlayerEntity player, LivingHurtEvent event) {
//        player.heal(event.getAmount() * this.getLeechRatio());
//
//        if (player.getRNG().nextFloat() <= 0.2) {
//            float pitch = MathUtilities.randomFloat(1f, 1.5f);
//            player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(),
//                    ModSounds.VAMPIRE_HISSING_SFX, SoundCategory.MASTER, 0.2f * 0.1f, pitch);
//            player.playSound(ModSounds.VAMPIRE_HISSING_SFX, SoundCategory.MASTER, 0.2f * 0.1f, pitch);
//        }
//    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if(event.getSource() == null) return;
        if (!(event.getSource().getTrueSource() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getSource().getTrueSource();
        TalentTree abilities = PlayerTalentsData.get(player.getServerWorld()).getTalents(player);

        float leech = 0f;
        for (TalentNode<?> node : abilities.getNodes()) {
            if (!(node.getTalent() instanceof VampirismTalent)) continue;
            VampirismTalent vampirism = (VampirismTalent) node.getTalent();
            leech += vampirism.getLeechRatio();
        }
        SetTree sets = PlayerSetsData.get(player.getServerWorld()).getSets(player);

        for (SetNode<?> node : sets.getNodes()) {
            if (!(node.getSet() instanceof VampirismSet)) continue;
            VampirismSet set = (VampirismSet) node.getSet();
            Vault.LOGGER.info("Set: " + set.getLeechRatio());
            leech += set.getLeechRatio();
        }

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = player.getItemStackFromSlot(slot);
            leech += ModAttributes.EXTRA_LEECH_RATIO.getOrDefault(stack, 0.0F).getValue(stack);
        }

        if(leech <= 0.0f) return;
        onDamagedEntity(event, player, leech);

    }

    private static void onDamagedEntity(LivingHurtEvent event, ServerPlayerEntity player, float leech) {
        player.heal(event.getAmount() * leech);

        // #Crimson_Fluff, addStat
        player.addStat(Vault.STAT_DAMAGE_VAMPIRED, (int)(event.getAmount() * leech));

        if (player.getRNG().nextFloat() <= 0.2) {
            float pitch = MathUtilities.randomFloat(1f, 1.5f);
            player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(),
                    ModSounds.VAMPIRE_HISSING_SFX, SoundCategory.MASTER, 0.2f * 0.1f, pitch);
        }
    }

}
