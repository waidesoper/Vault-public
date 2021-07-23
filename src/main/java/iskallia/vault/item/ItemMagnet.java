package iskallia.vault.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class ItemMagnet extends Item {
    public ItemMagnet(ResourceLocation id, Properties properties) {
        super(properties);

        this.setRegistryName(id);
    }

    private int tick = 0;

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.sameItem(new ItemStack(Items.IRON_INGOT)) || super.isValidRepairItem(toRepair, repair);
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (worldIn.isClientSide) return ActionResult.consume(stack);

        boolean active = !stack.getOrCreateTag().getBoolean("enabled");
        stack.getTag().putBoolean("enabled", active);

//        if (active)
//            playerIn.displayClientMessage(new TranslationTextComponent("tip." + Vault.MOD_ID + ".enabled").withStyle(TextFormatting.GRAY).append(new TranslationTextComponent("tip." + CrimsonMagnet.MOD_ID + ".active").mergeStyle(TextFormatting.GREEN)), true);
//        else
//            playerIn.displayClientMessage(new TranslationTextComponent("tip." + Vault.MOD_ID + ".enabledno").withStyle(TextFormatting.GRAY).append(new TranslationTextComponent("tip." + CrimsonMagnet.MOD_ID + ".inactive").mergeStyle(TextFormatting.RED)), true);

        playerIn.displayClientMessage(stack.getDisplayName().copy().append(": is now ").append(active
            ? new StringTextComponent("Active").withStyle(TextFormatting.GREEN)
            : new StringTextComponent("Inactive").withStyle(TextFormatting.RED)), true);

        playerIn.level.playSound(null, playerIn.blockPosition(), SoundEvents.NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1f, (active) ? 0.9f : 0.1f);

        return ActionResult.success(stack);
    }

    @Override
    public boolean isFoil(ItemStack stack) { return (stack.getOrCreateTag().getBoolean("enabled")); }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (! worldIn.isClientSide) {
            if (! (entityIn instanceof PlayerEntity)) return;        // stop zombies and the like using the magnet, lol

            if (stack.getOrCreateTag().getBoolean("enabled")) {
                PlayerEntity playerIn = (PlayerEntity) entityIn;

                double x = entityIn.getX();
                double y = entityIn.getY();
                double z = entityIn.getZ();

                //int r = (CrimsonMagnet.CONFIGURATION.magnetRange.get());
                int r = 10;
                AxisAlignedBB area = new AxisAlignedBB(x - r, y - r, z - r, x + r, y + r, z + r);
                List<ItemEntity> items = worldIn.getEntitiesOfClass(ItemEntity.class, area);

                boolean shouldBreak = false;

                if (items.size() != 0) {
                    for (ItemEntity itemIE : items) {
                        ((ServerWorld) worldIn).sendParticles(ParticleTypes.POOF, itemIE.getX(), itemIE.getY(), itemIE.getZ(), 2, 0D, 0D, 0D, 0D);

                        //r = itemIE.getItem().getCount();
                        itemIE.setNoPickUpDelay();
                        //if (r != 0) {
                            if (itemIE.distanceTo(playerIn) > 1.5f) shouldBreak = true;
                            itemIE.setPos(x, y, z);
                        //}
                    }
                }

                // Handle the XP
                List<ExperienceOrbEntity> orbs = worldIn.getEntitiesOfClass(ExperienceOrbEntity.class, area);

                if (orbs.size() != 0) {
                    shouldBreak = true;
                    worldIn.playSound(null, x, y, z, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1f, 1f);

                    ArrayList<ItemStack> MendingItems = new ArrayList<>();
                    ItemStack stacks;

                    // getRandomEquippedWithEnchantment only works with offhand, main hand, armor slots
                    // so make a list of valid items, add magnet to it, then randomly choose an item to repair
                    for (int a = 36; a < 41; a++) {
                        stacks = playerIn.inventory.getItem(a);
                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING, stacks) > 0)
                            if (stacks.isDamaged()) MendingItems.add(stacks);
                    }

                    // if Magnet is MENDING then add to list
                    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING, stack) > 0)
                        if (stack.isDamaged()) MendingItems.add(stack);

                    for (ExperienceOrbEntity orb : orbs) {
                        ((ServerWorld) worldIn).sendParticles(ParticleTypes.POOF, orb.blockPosition().getX(), orb.blockPosition().getY(), orb.blockPosition().getZ(), 2, 0D, 0D, 0D, 0D);

                        // Choose random item from MendingItems list
                        if (MendingItems.size() > 0) {
                            r = worldIn.random.nextInt(MendingItems.size());
                            stacks = MendingItems.get(r);

                            int i = Math.min((int) (orb.value * stacks.getXpRepairRatio()), stacks.getDamageValue());
                            orb.value -= i / 2;     //orb.durabilityToXp(i);
                            stacks.setDamageValue(stacks.getDamageValue() - i);

                            if (stacks.getDamageValue() == 0) MendingItems.remove(r);
                        }

                        if (orb.value > 0) playerIn.giveExperiencePoints(orb.value);
                        orb.remove();
                    }
                }

                // NOTE: DamageItem checks Creative mode !
                if (shouldBreak) {
                    stack.hurtAndBreak(1, playerIn, item -> { item.broadcastBreakEvent(EquipmentSlotType.MAINHAND); });
                    stack.setPopTime(5);
                }
            }
        }
    }
}
