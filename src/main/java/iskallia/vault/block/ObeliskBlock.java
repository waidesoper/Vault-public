package iskallia.vault.block;

import iskallia.vault.client.gui.overlay.VaultRaidOverlay;
import iskallia.vault.entity.EntityScaler;
import iskallia.vault.entity.FighterEntity;
import iskallia.vault.entity.VaultBoss;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModEntities;
import iskallia.vault.item.ObeliskInscriptionItem;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;
import java.util.stream.Stream;

public class ObeliskBlock extends Block {
    private static final IntegerProperty COMPLETION = IntegerProperty.create("completion", 0, 4);
    private static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public ObeliskBlock() {
        super(Properties.of(Material.STONE).sound(SoundType.METAL).strength(- 1.0F, 3600000.0F).noDrops());
        this.registerDefaultState(this.stateDefinition.any().setValue(COMPLETION, 0).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
            return Stream.of(
                Block.box(4, 4, 4, 12, 16, 12),
                Block.box(0, 0, 0, 16, 1, 16),
                Block.box(1, 1, 1, 15, 2, 15),
                Block.box(2, 2, 2, 14, 3, 14),
                Block.box(3, 3, 3, 13, 4, 13)
            ).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);
            }).get();
        else
            return Stream.of(
                Block.box(4, 0, 4, 12, 11, 12),
                Block.box(4.5, 11, 4.5, 11.5, 12, 11.5),
                Block.box(5, 12, 5, 11, 13, 11),
                Block.box(5.75, 13, 5.75, 10.25, 14, 10.25),
                Block.box(6.5, 14, 6.5, 9.5, 15, 9.5),
                Block.box(7.5, 14, 7.5, 8.5, 16, 8.5)
            ).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);
            }).get();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);


    // #Crimson_Fluff
        // NOTE: because of lag, if click a half rendered block (top block does not yet exist) it will crash
        // TODO: default bosses spawn at level 10, so if no boss then just exit
        if (heldStack.getItem() instanceof ObeliskInscriptionItem) {
            if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
                if (world.getBlockState(pos.above()).getBlock() == Blocks.AIR) return ActionResultType.FAIL;

            if (! player.isCreative()) heldStack.shrink(1);
        } else
            return ActionResultType.PASS;


        BlockState newState;
        int iComplete = MathHelper.clamp(state.getValue(COMPLETION) + 1, 0, 4);
        newState = state.setValue(COMPLETION, iComplete);
        world.setBlockAndUpdate(pos, newState);

        if (iComplete<4) {
            if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
                world.setBlockAndUpdate(pos.above(), world.getBlockState(pos.above()).setValue(COMPLETION, iComplete));
            else
                world.setBlockAndUpdate(pos.below(), world.getBlockState(pos.below()).setValue(COMPLETION, iComplete));

        } else {
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
                world.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
            else
                world.setBlockAndUpdate(pos.below(), Blocks.AIR.defaultBlockState());
        }
    // #Crimson_Fluff END

        if (world.isClientSide) {
            if (newState.getValue(COMPLETION) == 4)
                startBossLoop();

            return ActionResultType.SUCCESS;
        }

        spawnParticles(world, pos);

        if (newState.getValue(COMPLETION) == 4) {
            VaultRaid raid = VaultRaidData.get((ServerWorld) world).getAt(pos);

            if (raid != null) {
                spawnBoss(raid, (ServerWorld) world, pos, EntityScaler.Type.BOSS);
            }

            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }

        return ActionResultType.SUCCESS;
    }

    public static void spawnBoss(VaultRaid raid, ServerWorld world, BlockPos pos, EntityScaler.Type type) {
        LivingEntity boss;

        // #Crimson_Fluff, if crystal has a boss name then summon textured fighter
        // TODO: Triple check if bosses are scaling correctly, I think they not
        if (type == EntityScaler.Type.BOSS) {
            if (! raid.playerBossName.isEmpty()) {
                boss = ModEntities.VAULT_FIGHTER.create(world);
                boss.setCustomName(new StringTextComponent(raid.playerBossName));
            }
            else if (ModConfigs.VAULT_MOBS.getForLevel(raid.level).BOSS_POOL.size() != 0)        // #Crimson_Fluff, just in case
                boss = ModConfigs.VAULT_MOBS.getForLevel(raid.level).BOSS_POOL.getRandom(world.getRandom()).create(world);

            else
                return;
        } else
            return;

        boss.moveTo(pos.getX() + 0.5D, pos.getY() + 0.2D, pos.getZ() + 0.5D, 0.0F, 0.0F);
        boss.getTags().add("VaultBoss");
        raid.addBoss(boss);

        if (boss instanceof FighterEntity) {
            ((FighterEntity) boss).bossInfo.setVisible(true);
            ((FighterEntity) boss).changeSize(2.0F);
        }
        else if (boss instanceof VaultBoss)
            ((VaultBoss) boss).getServerBossInfo().setVisible(true);

        EntityScaler.scaleVault(boss, raid.level, new Random(), EntityScaler.Type.BOSS);

        world.addWithUUID(boss);
    }

    @OnlyIn(Dist.CLIENT)
    private void startBossLoop() {
        VaultRaidOverlay.bossSummoned = true;
    }

    public static void spawnParticles(World world, BlockPos pos) {
        for (int i = 0; i < 10; ++ i) {
            double d0 = world.random.nextGaussian() * 0.02D;
            double d1 = world.random.nextGaussian() * 0.02D;
            double d2 = world.random.nextGaussian() * 0.02D;

            ((ServerWorld) world).sendParticles(ParticleTypes.POOF,
                pos.getX() + world.random.nextDouble() - d0,
                pos.getY() + world.random.nextDouble() - d1,
                pos.getZ() + world.random.nextDouble() - d2, 10, d0, d1, d2, 0.2D);
        }

        world.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(COMPLETION, HALF);
    }

    // #Crimson_Fluff, TileEntity for Tick() so can produce more particles...
    @Override
    public boolean hasTileEntity(BlockState state) { return (state.getValue(HALF) == DoubleBlockHalf.LOWER); }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
            return ModBlocks.OBELISK_BLOCK_TILE_ENTITY.create();

        return null;
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (! worldIn.isClientSide) worldIn.setBlockAndUpdate(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER));
    }
}
