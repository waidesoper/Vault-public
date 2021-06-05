package iskallia.vault.block;

import iskallia.vault.client.gui.overlay.VaultRaidOverlay;
import iskallia.vault.entity.EntityScaler;
import iskallia.vault.entity.FighterEntity;
import iskallia.vault.entity.VaultBoss;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
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
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Stream;

public class ObeliskBlock extends Block {

    public static final IntegerProperty COMPLETION = IntegerProperty.create("completion", 0, 4);
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public ObeliskBlock() {
        super(Properties.create(Material.ROCK).sound(SoundType.METAL).hardnessAndResistance(-1.0F, 3600000.0F).noDrops());
        this.setDefaultState(this.stateContainer.getBaseState().with(COMPLETION, 0).with(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER)
            return Stream.of(
                Block.makeCuboidShape(4, 4, 4, 12, 16, 12),
                Block.makeCuboidShape(0, 0, 0, 16, 1, 16),
                Block.makeCuboidShape(1, 1, 1, 15, 2, 15),
                Block.makeCuboidShape(2, 2, 2, 14, 3, 14),
                Block.makeCuboidShape(3, 3, 3, 13, 4, 13)
            ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
            }).get();
        else
            return Stream.of(
                Block.makeCuboidShape(4, 0, 4, 12, 11, 12),
                Block.makeCuboidShape(4.5, 11, 4.5, 11.5, 12, 11.5),
                Block.makeCuboidShape(5, 12, 5, 11, 13, 11),
                Block.makeCuboidShape(5.75, 13, 5.75, 10.25, 14, 10.25),
                Block.makeCuboidShape(6.5, 14, 6.5, 9.5, 15, 9.5),
                Block.makeCuboidShape(7.5, 14, 7.5, 8.5, 16, 8.5)
            ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
            }).get();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack heldStack = player.getHeldItem(hand);

        if (heldStack.getItem() instanceof ObeliskInscriptionItem) {
            if (!player.isCreative()) heldStack.shrink(1);
        } else return ActionResultType.PASS;

        BlockState newState;
        int iComplete = MathHelper.clamp(state.get(COMPLETION) + 1, 0, 4);
        newState = state.with(COMPLETION, iComplete);
        world.setBlockState(pos, newState);

        if (iComplete<4) {
            if (state.get(HALF) == DoubleBlockHalf.LOWER)
                world.setBlockState(pos.up(), world.getBlockState(pos.up()).with(COMPLETION, iComplete));
            else
                world.setBlockState(pos.down(), world.getBlockState(pos.down()).with(COMPLETION, iComplete));

        } else {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            if (state.get(HALF) == DoubleBlockHalf.LOWER)
                world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 3);
            else
                world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), 3);
        }

        if (world.isRemote) {
            if (iComplete == 4) startBossLoop();

            return ActionResultType.SUCCESS;
        }

        this.spawnParticles(world, pos);

        if (iComplete == 4) {
            VaultRaid raid = VaultRaidData.get((ServerWorld) world).getAt(pos);

            if (raid != null) spawnBoss(raid, (ServerWorld) world, pos, EntityScaler.Type.BOSS);
        }

        return ActionResultType.SUCCESS;
    }

    public void spawnBoss(VaultRaid raid, ServerWorld world, BlockPos pos, EntityScaler.Type type) {
        LivingEntity boss;

        if(type == EntityScaler.Type.BOSS) {
            boss = ModConfigs.VAULT_MOBS.getForLevel(raid.level).BOSS_POOL.getRandom(world.getRandom()).create(world);
        } else {
            return;
        }

        if(boss instanceof FighterEntity)((FighterEntity)boss).changeSize(2.0F);
        boss.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 0.2D, pos.getZ() + 0.5D, 0.0F, 0.0F);
        world.summonEntity(boss);

        boss.getTags().add("VaultBoss");
        raid.addBoss(boss);

        if(boss instanceof FighterEntity) {
            ((FighterEntity)boss).bossInfo.setVisible(true);
        }

        if(boss instanceof VaultBoss) {
            ((VaultBoss)boss).getServerBossInfo().setVisible(true);
        }

        EntityScaler.scaleVault(boss, raid.level, new Random(), EntityScaler.Type.BOSS);

        if(raid.playerBossName != null) {
            boss.setCustomName(new StringTextComponent(raid.playerBossName));
        } else {
            boss.setCustomName(new StringTextComponent("Boss"));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void startBossLoop() {
        VaultRaidOverlay.bossSummoned = true;
    }

    private void spawnParticles(World world, BlockPos pos) {
        for (int i = 0; i < 20; ++i) {
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;

            ((ServerWorld) world).spawnParticle(ParticleTypes.POOF,
                    pos.getX() + world.rand.nextDouble() - d0,
                    pos.getY() + world.rand.nextDouble() - d1,
                    pos.getZ() + world.rand.nextDouble() - d2, 10, d0, d1, d2, 1.0D);
        }

        world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(COMPLETION, HALF);
    }


    // #Crimson_Fluff, TileEntity for Tick() so can produce more particles...
    @Override
    public boolean hasTileEntity(BlockState state) { return (state.get(HALF) == DoubleBlockHalf.LOWER); }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER)
            return ModBlocks.OBELISK_TILE_ENTITY.create();

        return null;
    }

//    @Nullable
//    @Override
//    public BlockState getStateForPlacement(BlockItemUseContext context) {
//        BlockPos pos = context.getPos();
//        World world = context.getWorld();
//        if (pos.getY() < 255 && world.getBlockState(pos.up()).isReplaceable(context)) {
//            return this.getDefaultState().with(COMPLETION, 0).with(HALF, DoubleBlockHalf.LOWER);
//        } else {
//            return null;
//        }
//    }

//    @Override
//    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
////        DoubleBlockHalf half = stateIn.get(HALF);
////        if (facing == Direction.UP) {
////            return facingState.isIn(this) && facingState.get(HALF) != half ? stateIn.with(COMPLETION, stateIn.get(COMPLETION)) : Blocks.AIR.getDefaultState();
////        } else {
////            return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
////        }
//        worldIn.setBlockState(facingPos.up(), stateIn.set(LOWER, false));
//    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (! worldIn.isRemote) {
            worldIn.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3);
        }
    }

//    @Override
//    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
//        return stateIn;
//    }
}
