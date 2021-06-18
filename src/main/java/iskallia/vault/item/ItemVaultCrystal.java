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
            .tab(group)
            .stacksTo(1));

        this.setRegistryName(id);
        this.vaultRarity = vaultRarity;
    }

    public static ItemStack getRandomCrystal() {
        return getCrystal(VaultRarity.getWeightedRandom());
    }

    public static ItemStack getCrystal(VaultRarity rarity) {
        switch (rarity) {
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
    public ActionResultType useOn(ItemUseContext context) {
        if (context.getLevel().isClientSide) return super.useOn(context);

        ItemStack stack = context.getPlayer().getMainHandItem();
        Item item = stack.getItem();

        if (item instanceof ItemVaultCrystal) {
            ItemVaultCrystal crystal = (ItemVaultCrystal) item;

            String playerBossName = "";
            CompoundNBT tag = stack.getOrCreateTag();
            if (tag.getAllKeys().contains("playerBossName")) {
                playerBossName = tag.getString("playerBossName");
            }

            BlockPos pos = context.getClickedPos();
            if (tryCreatePortal(crystal, context.getLevel(), pos, context.getClickedFace(), playerBossName, getData(stack))) {
                context.getLevel().playSound(null,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    ModSounds.VAULT_PORTAL_OPEN,
                    SoundCategory.BLOCKS,
                    1f, 1f
                );

                context.getItemInHand().shrink(1);

                // #Crimson_Fluff
                // Added Translations. Added vowel check like in VaultRaid.java
                IFormattableTextComponent text = new StringTextComponent("");
                text.append(new StringTextComponent(context.getPlayer().getName().getString()).withStyle(TextFormatting.GREEN));

                String rarityName = crystal.getRarity().name();//.toLowerCase();
                char c = rarityName.charAt(0);
                AtomicBoolean startsWithVowel = new AtomicBoolean(false);
                startsWithVowel.set(c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U');
                text.append(new TranslationTextComponent(startsWithVowel.get() ? "tip.the_vault.created_an" : "tip.the_vault.created_a"));

                text.append(new StringTextComponent(rarityName).withStyle(crystal.getRarity().color));
                text.append(new TranslationTextComponent("tip.the_vault.vault"));
                // #Crimson_Fluff END


//                IFormattableTextComponent text = new StringTextComponent("");
//                text.append(new StringTextComponent(context.getPlayer().getName().getString()).withStyle(TextFormatting.GREEN));
//                text.append(new StringTextComponent(" has created a "));
//                String rarityName = crystal.getRarity().name().toLowerCase();
//                rarityName = rarityName.substring(0, 1).toUpperCase() + rarityName.substring(1);
//
//                text.append(new StringTextComponent(rarityName).withStyle(crystal.getRarity().color));
//                text.append(new StringTextComponent(" Vault!"));

                context.getLevel().getServer().getPlayerList().broadcastMessage(
                    text, ChatType.CHAT, context.getPlayer().getUUID()
                );

                return ActionResultType.SUCCESS;
            }

        }
        return super.useOn(context);
    }

    private boolean tryCreatePortal(ItemVaultCrystal crystal, World world, BlockPos pos, Direction facing, String playerBossName, CrystalData data) {
        if (world.dimension() != World.OVERWORLD)
            return false;

        Optional<VaultPortalSize> optional = VaultPortalSize.getPortalSize(world, pos.relative(facing), Direction.Axis.X);
        if (optional.isPresent()) {
            optional.get().placePortalBlocks(crystal, playerBossName, data);
            return true;
        }
        return false;
    }

    @Override
    public ITextComponent getName(ItemStack stack) {
//        if (stack.getItem() instanceof ItemVaultCrystal) {
            ItemVaultCrystal item = (ItemVaultCrystal) stack.getItem();
            IFormattableTextComponent name = null;

            switch (item.getRarity()) {
                case COMMON:
                    name = new TranslationTextComponent("tip.the_vault.crystal_common").withStyle(item.getRarity().color);
                    break;

                case RARE:
                    name = new TranslationTextComponent("tip.the_vault.crystal_rare").withStyle(item.getRarity().color);
                    break;

                case EPIC:
                    name = new TranslationTextComponent("tip.the_vault.crystal_epic").withStyle(item.getRarity().color);
                    break;

                case OMEGA:
                    name = new TranslationTextComponent("tip.the_vault.crystal_omega").withStyle(item.getRarity().color);
            }

            CompoundNBT tag = stack.getOrCreateTag();
            if (tag.getAllKeys().contains("playerBossName")) {
                return name.append(new StringTextComponent(" (" + tag.getString("playerBossName") + ")").withStyle(item.getRarity().color));
            }
//        }

//        return super.getName(stack);
            return name;
}

    public VaultRarity getRarity() {
        return vaultRarity;
    }

    public static CrystalData getData(ItemStack stack) {
        return new CrystalData(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        getData(stack).addInformation(world, tooltip, flag);
        super.appendHoverText(stack, world, tooltip, flag);
    }

}
