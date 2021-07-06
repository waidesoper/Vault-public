package iskallia.vault.item;

import iskallia.vault.Vault;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

// #Crimson_Fluff,
// TODO: Scale food damage to player total heart count?

public class ItemVaultFruit extends Item {

    protected int extraVaultTicks;
    protected DamageSource damageSource = new DamageSource("fruit").bypassArmor();

    public ItemVaultFruit(ItemGroup group, ResourceLocation id, int extraVaultTicks) {
        super(new Properties()
            .tab(group)
            .food(new Food.Builder().saturationMod(0).nutrition(0).fast().alwaysEat().build())
            .stacksTo(64));

        this.setRegistryName(id);

        this.extraVaultTicks = extraVaultTicks;
    }

    public int getExtraVaultTicks() {
        return extraVaultTicks;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getItemInHand(handIn);
        if (playerIn.level.dimension() != Vault.VAULT_KEY) return ActionResult.fail(itemStack);

        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent(""));
        TranslationTextComponent comp = new TranslationTextComponent("tip.the_vault.food_dim");
        comp.withStyle(TextFormatting.RED).withStyle(TextFormatting.ITALIC);           //setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_FF0000)).withItalic(true));
        tooltip.add(comp);

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ITextComponent getName(ItemStack stack) {
        IFormattableTextComponent displayName = (IFormattableTextComponent) super.getName(stack);
        return displayName.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_fcbd00)));
    }

    public static class BitterLemon extends ItemVaultFruit {
        public BitterLemon(ItemGroup group, ResourceLocation id, int extraVaultTicks) {
            super(group, id, extraVaultTicks);
        }

        @Override
        public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
            if (! worldIn.isClientSide && entityLiving instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
//                VaultRaid raid = VaultRaidData.get((ServerWorld) worldIn).getActiveFor(player);

                // #Crimson_Fluff, apply new timer to ALL players in Vault dimension
                // TODO: what happens if they are spectators? does this ever happen? do they have RaidData?
                player.getServer().getPlayerList().getPlayers().forEach(coop -> {
                    VaultRaid raid = VaultRaidData.get((ServerWorld) worldIn).getActiveFor(coop);
                    raid.ticksLeft += getExtraVaultTicks();
                    raid.sTickLeft += this.getExtraVaultTicks();

                    worldIn.playSound(null,
                        coop.getX(),
                        coop.getY(),
                        coop.getZ(),
                        SoundEvents.CONDUIT_ACTIVATE,
                        SoundCategory.MASTER,
                        1.0F, 1.0F);

                });

                player.hurt(this.damageSource, 6);
            }

            return super.finishUsingItem(stack, worldIn, entityLiving);
        }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            TranslationTextComponent comp;

            tooltip.add(new StringTextComponent(""));
            comp = new TranslationTextComponent("tip.the_vault.food_lemon");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_BEEBEE)).withItalic(true));
            tooltip.add(comp);
            comp = new TranslationTextComponent("tip.the_vault.food_grown");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_BEEBEE)).withItalic(true));
            tooltip.add(comp);

            tooltip.add(new StringTextComponent(""));
            comp = new TranslationTextComponent("tip.the_vault.food_minus", 3);
            comp.withStyle(TextFormatting.RED).withStyle(TextFormatting.ITALIC);       //setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_FF0000)));
            tooltip.add(comp);
            comp = new TranslationTextComponent("tip.the_vault.food_add", 30);
            comp.withStyle(TextFormatting.GREEN).withStyle(TextFormatting.ITALIC);       //setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_FF0000)));
            tooltip.add(comp);

            super.appendHoverText(stack, worldIn, tooltip, flagIn);
        }
    }

    public static class SourOrange extends ItemVaultFruit {
        public SourOrange(ItemGroup group, ResourceLocation id, int extraVaultTicks) {
            super(group, id, extraVaultTicks);
        }

        @Override
        public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
            if (! worldIn.isClientSide && entityLiving instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
//                VaultRaid raid = VaultRaidData.get((ServerWorld) worldIn).getActiveFor(player);

                // #Crimson_Fluff, apply new timer to ALL players in Vault dimension
                // TODO: what happens if they are spectators? does this ever happen? do they have RaidData?
                player.getServer().getPlayerList().getPlayers().forEach(coop -> {
                    VaultRaid raid = VaultRaidData.get((ServerWorld) worldIn).getActiveFor(coop);
                    raid.ticksLeft += getExtraVaultTicks();
                    raid.sTickLeft += this.getExtraVaultTicks();

                    worldIn.playSound(null,
                        coop.getX(),
                        coop.getY(),
                        coop.getZ(),
                        SoundEvents.CONDUIT_ACTIVATE,
                        SoundCategory.MASTER,
                        1.0F, 1.0F);
                });

                player.hurt(this.damageSource, 10);
            }

            return super.finishUsingItem(stack, worldIn, entityLiving);
        }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            TranslationTextComponent comp;

            tooltip.add(new StringTextComponent(""));
            comp = new TranslationTextComponent("tip.the_vault.food_orange");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_BEEBEE)).withItalic(true));
            tooltip.add(comp);
            comp = new TranslationTextComponent("tip.the_vault.food_grown");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_BEEBEE)).withItalic(true));
            tooltip.add(comp);

            tooltip.add(new StringTextComponent(""));
            comp = new TranslationTextComponent("tip.the_vault.food_minus", 5);
            comp.withStyle(TextFormatting.RED).withStyle(TextFormatting.ITALIC);       //setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_FF0000)));
            tooltip.add(comp);
            comp = new TranslationTextComponent("tip.the_vault.food_add", 60);
            comp.withStyle(TextFormatting.GREEN).withStyle(TextFormatting.ITALIC);       //setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_FF0000)));
            tooltip.add(comp);

            super.appendHoverText(stack, worldIn, tooltip, flagIn);
        }
    }

    public static class MysticPear extends ItemVaultFruit {
        public MysticPear(ItemGroup group, ResourceLocation id, int extraVaultTicks) {
            super(group, id, extraVaultTicks);
        }

        @Override
        public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
            if (! worldIn.isClientSide && entityLiving instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
//                VaultRaid raid = VaultRaidData.get((ServerWorld) worldIn).getActiveFor(player);

                // #Crimson_Fluff, apply new timer to ALL players in Vault dimension
                // TODO: what happens if they are spectators? does this ever happen? do they have RaidData?
                player.getServer().getPlayerList().getPlayers().forEach(coop -> {
                    VaultRaid raid = VaultRaidData.get((ServerWorld) worldIn).getActiveFor(coop);
                    raid.ticksLeft += getExtraVaultTicks();
                    raid.sTickLeft += this.getExtraVaultTicks();

                    worldIn.playSound(null,
                        coop.getX(),
                        coop.getY(),
                        coop.getZ(),
                        SoundEvents.CONDUIT_ACTIVATE,
                        SoundCategory.MASTER,
                        1.0F, 1.0F);
                });

                player.hurt(this.damageSource, MathUtilities.getRandomInt(10, 20));

                if (MathUtilities.randomFloat(0, 100) <= 50)
                    player.addEffect(new EffectInstance(Effects.POISON, 30 * 20));
                else
                    player.addEffect(new EffectInstance(Effects.WITHER, 30 * 20));
            }

            return super.finishUsingItem(stack, worldIn, entityLiving);
        }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            TranslationTextComponent comp;

            tooltip.add(new StringTextComponent(""));
            comp = new TranslationTextComponent("tip.the_vault.food_pear");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_BEEBEE)).withItalic(true));
            tooltip.add(comp);
            comp = new TranslationTextComponent("tip.the_vault.food_grown");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_BEEBEE)).withItalic(true));
            tooltip.add(comp);

            tooltip.add(new StringTextComponent(""));
            comp = new TranslationTextComponent("tip.the_vault.food_wipe");
            comp.withStyle(TextFormatting.RED).withStyle(TextFormatting.ITALIC);       //setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_FF0000)));
            tooltip.add(comp);
            comp = new TranslationTextComponent("tip.the_vault.food_bad");
            comp.withStyle(TextFormatting.RED).withStyle(TextFormatting.ITALIC);       //setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_FF0000)));
            tooltip.add(comp);
            comp = new TranslationTextComponent("tip.the_vault.food_addmin", 5);
            comp.withStyle(TextFormatting.GREEN).withStyle(TextFormatting.ITALIC);       //setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_FF0000)));
            tooltip.add(comp);

            super.appendHoverText(stack, worldIn, tooltip, flagIn);
        }
    }

    public static class SweetKiwi extends ItemVaultFruit {
        public SweetKiwi(ItemGroup group, ResourceLocation id, int extraVaultTicks) {
            super(group, id, extraVaultTicks);
        }

        @Override
        public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
            if (! worldIn.isClientSide && entityLiving instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
//                VaultRaid raid = VaultRaidData.get((ServerWorld) worldIn).getActiveFor(player);

                // #Crimson_Fluff, apply new timer to ALL players in Vault dimension
                // TODO: what happens if they are spectators? does this ever happen? do they have RaidData?
                player.getServer().getPlayerList().getPlayers().forEach(coop -> {
                    VaultRaid raid = VaultRaidData.get((ServerWorld) worldIn).getActiveFor(coop);
                    raid.ticksLeft += getExtraVaultTicks();
                    raid.sTickLeft += this.getExtraVaultTicks();

                    worldIn.playSound(null,
                        coop.getX(),
                        coop.getY(),
                        coop.getZ(),
                        SoundEvents.CONDUIT_ACTIVATE,
                        SoundCategory.MASTER,
                        1.0F, 1.0F);
                });
            }

            return super.finishUsingItem(stack, worldIn, entityLiving);
        }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            tooltip.add(new StringTextComponent(""));
            TranslationTextComponent comp = new TranslationTextComponent("tip.the_vault.food_add", 10);
            comp.withStyle(TextFormatting.RED).withStyle(TextFormatting.ITALIC);       //setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_00FF00)));
            tooltip.add(comp);

            super.appendHoverText(stack, worldIn, tooltip, flagIn);
        }
    }
}
