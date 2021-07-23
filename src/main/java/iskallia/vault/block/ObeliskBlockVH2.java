package iskallia.vault.block;

import iskallia.vault.entity.EntityScaler;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.ObeliskVH2OverlayUpdate;
import iskallia.vault.network.message.StartBossLoopMessage;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.raid.VaultRaid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.stream.Stream;

public class ObeliskBlockVH2 extends Block {
    private static final IntegerProperty COMPLETION = IntegerProperty.create("completion", 0, 1);
    private static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public ObeliskBlockVH2() {
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
            ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
        else
            return Stream.of(
                Block.box(4, 0, 4, 12, 11, 12),
                Block.box(4.5, 11, 4.5, 11.5, 12, 11.5)
            ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isClientSide) return ActionResultType.SUCCESS;

        if (state.getValue(COMPLETION) == 1) return ActionResultType.FAIL;

        world.setBlockAndUpdate(pos, state.setValue(COMPLETION,1));
        world.setBlockAndUpdate(state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos.above() : pos.below(), world.getBlockState(state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos.above() : pos.below()).setValue(COMPLETION, 1));

        ObeliskBlock.spawnParticles(world, pos);
        VaultRaid raid = VaultRaidData.get((ServerWorld) world).getAt(pos);

        if (raid != null) {
            raid.obelisksActivated++;

            ModNetwork.CHANNEL.sendTo(new ObeliskVH2OverlayUpdate(raid.obelisksActivated, raid.obelisksNeeded), ((ServerPlayerEntity) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);

            //player.displayClientMessage(new StringTextComponent("Obelisk: " + raid.obelisksActivated + " / " + raid.obelisksNeeded), false);

            if (raid.obelisksActivated == raid.obelisksNeeded) {
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                world.setBlockAndUpdate(state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos.above() : pos.below(), Blocks.AIR.defaultBlockState());

                // send message client side to start the boss raid
                ModNetwork.CHANNEL.sendTo(new StartBossLoopMessage(), ((ServerPlayerEntity) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);

                ObeliskBlock.spawnBoss(raid, (ServerWorld) world, pos, EntityScaler.Type.BOSS);
            }
            //else player.displayClientMessage(new StringTextComponent("Obelisk: " + raid.obelisksActivated + " / " + raid.obelisksNeeded), false);
        }

        return ActionResultType.SUCCESS;
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
            return ModBlocks.OBELISK_BLOCK_VH2_TILE_ENTITY.create();

        return null;
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (! worldIn.isClientSide) worldIn.setBlockAndUpdate(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER));
    }
}
