package iskallia.vault.skill.ability.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VeinMinerAbility extends PlayerAbility {

    @Expose
    private final int blockLimit;

    public VeinMinerAbility(int cost, int blockLimit) {
        super(cost, Behavior.HOLD_TO_ACTIVATE);
        this.blockLimit = blockLimit;
    }

    public int getBlockLimit() {
        return blockLimit;
    }

    @SubscribeEvent
    public static void onBlockMined(BlockEvent.BreakEvent event) {
        if (! event.getWorld().isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

            // #Crimson_Fluff, Break Chest/Barrel without need to open first
            ServerWorld world = (ServerWorld) event.getWorld();
            BlockPos pos = event.getPos();
            BlockState blockState = world.getBlockState(pos);

            if (! player.isCreative()) {
                if (blockState.getBlock() == Blocks.CHEST || blockState.getBlock() == Blocks.BARREL) {
                    TileEntity tile = world.getBlockEntity(pos);
//                    player.sendStatusMessage(new StringTextComponent("NBT: " + tile.getTileData().toString()), false);
//                    if (tile.getTileData().contains("LootTable", 8)) {
                    if (tile != null) ((LockableLootTileEntity) tile).unpackLootTable(player);
                }
            }


            AbilityTree abilityTree = PlayerAbilitiesData.get((ServerWorld) event.getWorld()).getAbilities(player);
            if (! abilityTree.isActive()) return;

            AbilityNode<?> focusedAbilityNode = abilityTree.getFocusedAbility();

            if (focusedAbilityNode != null) {
                PlayerAbility focusedAbility = focusedAbilityNode.getAbility();

                if (focusedAbility instanceof VeinMinerAbility) {
                    VeinMinerAbility veinMinerAbility = (VeinMinerAbility) focusedAbility;

                    if (floodMine(player, world, blockState.getBlock(), pos, veinMinerAbility.getBlockLimit(), focusedAbilityNode.getLevel())) {
                        event.setCanceled(true);
                    }

                    abilityTree.setSwappingLocked(true);
                }
            }
        }
    }

    // "Forest Fire Algorithm" from https://en.wikipedia.org/wiki/Flood_fill
    public static boolean floodMine(ServerPlayerEntity player, ServerWorld world, Block targetBlock, BlockPos pos, int limit, int vmLevel) {
        if (world.getBlockState(pos).getBlock() != targetBlock) return false;

        ItemStack heldItem = player.getItemInHand(player.getUsedItemHand());

//        if (heldItem.isDamageableItem()) {
//            int usesLeft = heldItem.getMaxDamage() - heldItem.getDamageValue();
//            if (usesLeft <= 1) return false; // Tool will break anyways, let the event handle that
//        }

        // #Crimson_Fluff, what if XP is larger than Int.MAX_VALUE
        // maybe spawn as separate xp orbs ?
        int bonusLevel = 0;
        int silkLevel = 0;
        int XP = 0;
        if (vmLevel > 2) {
            bonusLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, player);
            silkLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player);
        }


        int traversedBlocks = 0;
        List<ItemStack> itemDrops = new LinkedList<>();
        Queue<BlockPos> positionQueue = new LinkedList<>();

//        itemDrops.addAll(destroyBlockAs(world, pos, player));

        if (destroyBlockAs(world, pos, player, itemDrops)) {
            if (vmLevel > 2) XP = world.getBlockState(pos).getExpDrop(world, pos, bonusLevel, silkLevel);
            world.destroyBlock(pos, false, player);
        }
        else return false;

        positionQueue.add(pos);
        traversedBlocks++;

        floodLoop:
        while (! positionQueue.isEmpty()) {
            BlockPos headPos = positionQueue.poll();

            for (int x = - 1; x <= 1; x++) {
                for (int y = - 1; y <= 1; y++) {
                    for (int z = - 1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;
                        if (traversedBlocks >= limit) break floodLoop;

                        BlockPos curPos = headPos.offset(x, y, z);
                        BlockState state = world.getBlockState(curPos);

                        if (world.getBlockState(curPos).getBlock() == targetBlock) {
//                            itemDrops.addAll(destroyBlockAs(world, curPos, player));
                            if (destroyBlockAs(world, curPos, player, itemDrops)) {
                                if (vmLevel > 2) XP += state.getExpDrop(world, curPos, bonusLevel, silkLevel);
                                world.destroyBlock(curPos, false, player);
                            }

                            positionQueue.add(curPos);
                            traversedBlocks++;
                        }
                    }
                }
            }
        }

        itemDrops.forEach(stack -> Block.popResource(world, pos, stack));
        if (vmLevel > 2)
            if (XP > 0) {
                world.addFreshEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), XP));    // #Crimson_Fluff
//                player.displayClientMessage(new StringTextComponent("XP: " + XP), false);
            }

        return true;
    }

    // TODO: #Crimson_Fluff, check if using valid item to break blocks
    // vanilla using shield to break item=no durability, veinminer=shield takes durability
    public static boolean destroyBlockAs(ServerWorld world, BlockPos pos, PlayerEntity player, List<ItemStack> lst) {
        ItemStack heldItem = player.getItemInHand(player.getUsedItemHand());

        if (heldItem.isDamageableItem()) {
            int usesLeft = heldItem.getMaxDamage() - heldItem.getDamageValue();
            if (usesLeft <= 1) return false;

            heldItem.hurtAndBreak(1, player, playerEntity -> {});
        }

        lst.addAll(Block.getDrops(world.getBlockState(pos), world, pos, world.getBlockEntity(pos), player, heldItem));
        player.causeFoodExhaustion(0.005f);     // #Crimson_Fluff, because.

        return true;
    }

}
