package iskallia.vault.item;

import iskallia.vault.block.VaultPortalSize;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModSounds;
import iskallia.vault.util.VaultRarity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemVaultCrystal extends Item {

    private VaultRarity vaultRarity;

    public ItemVaultCrystal(ItemGroup group, ResourceLocation id, VaultRarity vaultRarity) {
        super(new Properties()
                .group(group)
                .maxStackSize(1));

        this.setRegistryName(id);
        this.vaultRarity = vaultRarity;
    }

    public static ItemStack getRandomCrystal() {
        return getCrystal(VaultRarity.getWeightedRandom());
    }

    public static ItemStack getCrystal(VaultRarity rarity) {
        switch(rarity) {
            case COMMON:
                return new ItemStack(ModItems.VAULT_CRYSTAL_NORMAL);
            case RARE:
                return new ItemStack(ModItems.VAULT_CRYSTAL_RARE);
            case EPIC:
                return new ItemStack(ModItems.VAULT_CRYSTAL_EPIC);
            case OMEGA:
                return new ItemStack(ModItems.VAULT_CRYSTAL_OMEGA);
        }

        return new ItemStack(ModItems.VAULT_CRYSTAL_NORMAL);
    }

    public static ItemStack getCrystalWithBoss(String playerBossName) {
        ItemStack stack = ItemVaultCrystal.getRandomCrystal();
        stack.getOrCreateTag().putString("playerBossName", playerBossName);
        return stack;
    }

    public static ItemStack getCrystalWithBoss(VaultRarity rarity, String playerBossName) {
        ItemStack stack = ItemVaultCrystal.getCrystal(rarity);
        stack.getOrCreateTag().putString("playerBossName", playerBossName);
        return stack;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if(context.getWorld().isRemote)return super.onItemUse(context);

        ItemStack stack = context.getPlayer().getHeldItemMainhand();
        Item item = stack.getItem();

        if(item instanceof ItemVaultCrystal) {
            ItemVaultCrystal crystal = (ItemVaultCrystal) item;

            String playerBossName = "";
            CompoundNBT tag = stack.getOrCreateTag();
            if (tag.keySet().contains("playerBossName")) {
                playerBossName = tag.getString("playerBossName");
            }

            BlockPos pos = context.getPos();
            if (tryCreatePortal(crystal, context.getWorld(), pos, context.getFace(), playerBossName, getData(stack))) {
                context.getWorld().playSound(null,
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        ModSounds.VAULT_PORTAL_OPEN,
                        SoundCategory.BLOCKS,
                        1f, 1f
                );

                context.getItem().shrink(1);

            // #Crimson_Fluff
            // Added Translations. Added vowel check like in VaultRaid.java
                IFormattableTextComponent text = new StringTextComponent("");
                text.append(new StringTextComponent(context.getPlayer().getName().getString()).mergeStyle(TextFormatting.GREEN));

                String rarityName = crystal.getRarity().name();//.toLowerCase();
                char c = rarityName.charAt(0);
                AtomicBoolean startsWithVowel = new AtomicBoolean(false);
                startsWithVowel.set(c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U');
                text.append(new TranslationTextComponent(startsWithVowel.get() ? "tip.the_vault.created_an" : "tip.the_vault.created_a"));

                text.append(new StringTextComponent(rarityName).mergeStyle(crystal.getRarity().color));
                text.append(new TranslationTextComponent("tip.the_vault.vault"));
            // #Crimson_Fluff END

                context.getWorld().getServer().getPlayerList().func_232641_a_(text, ChatType.CHAT, context.getPlayer().getUniqueID());

                return ActionResultType.SUCCESS;
            }

        }
        return super.onItemUse(context);
    }

    private boolean tryCreatePortal(ItemVaultCrystal crystal, World world, BlockPos pos, Direction facing, String playerBossName, CrystalData data) {
        if (world.getDimensionKey() != World.OVERWORLD) return false;

        Optional<VaultPortalSize> optional = VaultPortalSize.getPortalSize(world, pos.offset(facing), Direction.Axis.X);
        if (optional.isPresent()) {
            optional.get().placePortalBlocks(crystal, playerBossName, data);
            return true;
        }
        return false;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (stack.getItem() instanceof ItemVaultCrystal) {
            ItemVaultCrystal item = (ItemVaultCrystal) stack.getItem();

            CompoundNBT tag = stack.getOrCreateTag();
            if (tag.keySet().contains("playerBossName")) {
                return new TranslationTextComponent("tip.the_vault.crystal")
                    .mergeStyle(item.getRarity().color)
                    .append(new StringTextComponent(" (" + tag.getString("playerBossName") + ")"));
            }

            switch (item.getRarity()) {
                case COMMON:
                    return new TranslationTextComponent("tip.the_vault.crystal_common").mergeStyle(item.getRarity().color);
                case RARE:
                    return new TranslationTextComponent("tip.the_vault.crystal_rare").mergeStyle(item.getRarity().color);
                case EPIC:
                    return new TranslationTextComponent("tip.the_vault.crystal_epic").mergeStyle(item.getRarity().color);
                case OMEGA:
                    return new TranslationTextComponent("tip.the_vault.crystal_omega").mergeStyle(item.getRarity().color);
            }
        }

        return super.getDisplayName(stack);
    }

    public VaultRarity getRarity() {
        return vaultRarity;
    }

    public static CrystalData getData(ItemStack stack) {
        return new CrystalData(stack);
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        getData(stack).addInformation(world, tooltip, flag);
        super.addInformation(stack, world, tooltip, flag);
    }

}
