package iskallia.vault.item;

import iskallia.vault.Vault;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ItemSkillOrb extends Item {

    public ItemSkillOrb(ItemGroup group) {
        super(new Properties()
            .tab(group)
            .stacksTo(64));

        this.setRegistryName(Vault.id("skill_orb"));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack heldItemStack = player.getItemInHand(hand);

        world.playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.PLAYER_LEVELUP, SoundCategory.NEUTRAL,
            0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if (! world.isClientSide) {
            PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerWorld) world);
            statsData.addSkillPoint(((ServerPlayerEntity) player), 1);

            // #Crimson_Fluff
            ((ServerWorld) world).sendParticles(new ItemParticleData(ParticleTypes.ITEM, heldItemStack), player.blockPosition().getX(),player.blockPosition().getY() + 1, player.blockPosition().getZ(), 250, 1D, 1D , 1D, 0d);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (! player.abilities.instabuild) {
            heldItemStack.shrink(1);
        }

        return ActionResult.sidedSuccess(heldItemStack, world.isClientSide());
    }

}
