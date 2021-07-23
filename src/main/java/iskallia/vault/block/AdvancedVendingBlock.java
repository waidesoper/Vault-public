package iskallia.vault.block;

import iskallia.vault.block.entity.AdvancedVendingTileEntity;
import iskallia.vault.container.VendingMachineContainer;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class AdvancedVendingBlock extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public AdvancedVendingBlock() {
        super(Properties.of(Material.METAL, MaterialColor.METAL)
            .strength(2.0F, 3600000.0F)
            .sound(SoundType.METAL)
            .noOcclusion());

        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
            return ModBlocks.ADVANCED_VENDING_MACHINE_TILE_ENTITY.create();

        return null;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        World world = context.getLevel();
        if (pos.getY() < 255 && world.getBlockState(pos.above()).canBeReplaced(context)) {
            return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HALF);
        builder.add(FACING);
    }

    @Override
    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (! worldIn.isClientSide && player.isCreative()) {
            DoubleBlockHalf half = state.getValue(HALF);
            if (half == DoubleBlockHalf.UPPER) {
                BlockPos blockpos = pos.below();
                BlockState blockstate = worldIn.getBlockState(blockpos);
                if (blockstate.getBlock() == state.getBlock() && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
                    worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                    worldIn.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
                }
            }
        }

        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        DoubleBlockHalf half = stateIn.getValue(HALF);
        if (facing.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (facing == Direction.UP)) {
            return facingState.is(this) && facingState.getValue(HALF) != half ? stateIn.setValue(FACING, facingState.getValue(FACING)) : Blocks.AIR.defaultBlockState();
        } else {
            return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && ! stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        worldIn.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (worldIn.isClientSide) return;
        if (! newState.isAir()) return;

        AdvancedVendingTileEntity machine = (AdvancedVendingTileEntity) getBlockTileEntity(worldIn, pos, state);
        if (machine == null) return;

        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            ItemStack stack = new ItemStack(getBlock());
            CompoundNBT machineNBT = machine.serializeNBT();
            CompoundNBT stackNBT = new CompoundNBT();
            stackNBT.put("BlockEntityTag", machineNBT);

            stack.setTag(stackNBT);
            dropVendingMachine(stack, worldIn, pos);
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    private void dropVendingMachine(ItemStack stack, World world, BlockPos pos) {
        ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        world.addFreshEntity(entity);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isClientSide) { return ActionResultType.SUCCESS; }

        ItemStack heldStack = player.getItemInHand(hand);
        if (! heldStack.isEmpty()) return ActionResultType.FAIL;

        AdvancedVendingTileEntity machine = (AdvancedVendingTileEntity) getBlockTileEntity(world, pos, state);
        if (machine == null) return ActionResultType.SUCCESS;

        if (player.isShiftKeyDown()) {
            ItemStack core = machine.getTraderCoreStack();

            if (! core.isEmpty()) {
                // #Crimson_Fluff a sound indication that trader card is inserted/removed
                world.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 1f, 1f);

                if (! player.addItem(core)) player.drop(core, false);
                machine.sendUpdates();
            }

            return ActionResultType.SUCCESS;
        }

        NetworkHooks.openGui(
            (ServerPlayerEntity) player,
            new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() { return new TranslationTextComponent("block.the_vault.advanced_vending_machine"); }

                @Nullable
                @Override
                public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    BlockState blockState = world.getBlockState(pos);
                    BlockPos vendingMachinePos = getTileEntityPos(blockState, pos);
                    return new VendingMachineContainer(windowId, world, vendingMachinePos, playerInventory, playerEntity);
                }
            },
            (buffer) -> {
                BlockState blockState = world.getBlockState(pos);
                buffer.writeBlockPos(getTileEntityPos(blockState, pos));
            }
        );

        return super.use(state, world, pos, player, hand, hit);
    }

    public static BlockPos getTileEntityPos(BlockState state, BlockPos pos) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
    }

    public static TileEntity getBlockTileEntity(World world, BlockPos pos, BlockState state) {
        BlockPos vendingMachinePos = getTileEntityPos(state, pos);

        return world.getBlockEntity(vendingMachinePos);
    }
}
