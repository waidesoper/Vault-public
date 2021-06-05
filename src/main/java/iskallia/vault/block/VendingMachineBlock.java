package iskallia.vault.block;

import iskallia.vault.block.entity.VendingMachineTileEntity;
import iskallia.vault.container.VendingMachineContainer;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModSounds;
import iskallia.vault.item.ItemTraderCore;
import iskallia.vault.vending.TraderCore;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class VendingMachineBlock extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public VendingMachineBlock() {
        this(Properties.create(Material.IRON, MaterialColor.IRON)
                .hardnessAndResistance(2.0F, 3600000.0F)
                .sound(SoundType.METAL)
                .notSolid());

    }

    public VendingMachineBlock(Properties properties) {
        super(properties);

        this.setDefaultState(this.stateContainer.getBaseState()
                .with(FACING, Direction.NORTH)
                .with(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER)
            return ModBlocks.VENDING_MACHINE_TILE_ENTITY.create();

        return null;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getPos();
        World world = context.getWorld();
        if (pos.getY() < 255 && world.getBlockState(pos.up()).isReplaceable(context)) {
            return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing()).with(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HALF);
        builder.add(FACING);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isRemote && player.isCreative()) {
            DoubleBlockHalf half = state.get(HALF);
            if (half == DoubleBlockHalf.UPPER) {
                BlockPos blockpos = pos.down();
                BlockState blockstate = worldIn.getBlockState(blockpos);
                if (blockstate.getBlock() == state.getBlock() && blockstate.get(HALF) == DoubleBlockHalf.LOWER) {
                    worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
                    worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
                }
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        DoubleBlockHalf half = stateIn.get(HALF);
        if (facing.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (facing == Direction.UP)) {
            return facingState.isIn(this) && facingState.get(HALF) != half ? stateIn.with(FACING, facingState.get(FACING)) : Blocks.AIR.getDefaultState();
        } else {
            return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        worldIn.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (worldIn.isRemote) return;
        if (!newState.isAir()) return;

        VendingMachineTileEntity machine = (VendingMachineTileEntity) getBlockTileEntity(worldIn, pos, state);
        if (machine == null) return;

        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            ItemStack stack = new ItemStack(getBlock());
            CompoundNBT machineNBT = machine.serializeNBT();
            CompoundNBT stackNBT = new CompoundNBT();
            stackNBT.put("BlockEntityTag", machineNBT);

            stack.setTag(stackNBT);
            dropVendingMachine(stack, worldIn, pos);
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    private void dropVendingMachine(ItemStack stack, World world, BlockPos pos) {
        ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        world.addEntity(entity);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack heldStack = player.getHeldItem(hand);

        VendingMachineTileEntity machine = (VendingMachineTileEntity) getBlockTileEntity(world, pos, state);
        if (machine == null) return ActionResultType.SUCCESS;

        if (!world.isRemote() && player.isSneaking()) {
            ItemStack core = machine.getTraderCoreStack();
            playOpenSound();    // #Crimson_Fluff a sound indication that trader card is inserted/removed
            if (!player.addItemStackToInventory(core)) {
                player.dropItem(core, false);
            }
            machine.sendUpdates();
            return ActionResultType.SUCCESS;
        }

        if (heldStack.getItem() instanceof ItemTraderCore) {
            TraderCore lastCore = machine.getLastCore();
            TraderCore coreToInsert = ItemTraderCore.getCoreFromStack(heldStack);
            if(coreToInsert == null || coreToInsert.getTrade() == null) return ActionResultType.FAIL;
            if (lastCore == null || lastCore.getName().equalsIgnoreCase(coreToInsert.getName())) {
                machine.addCore(coreToInsert);
                playOpenSound();                    // #Crimson_Fluff a sound indication that trader card is inserted/removed
                heldStack.shrink(1);        // #Crimson_Fluff TODO: Not in Creative ?
            } else {
                TranslationTextComponent text = new TranslationTextComponent("tip.the_vault.vending_occupied");
                text.setStyle(Style.EMPTY.setColor(Color.fromInt(0xFF_ffd800)));
                player.sendStatusMessage(text, true);
            }

            return ActionResultType.SUCCESS;

        } else {
            if (world.isRemote) {
//                playOpenSound();      // #Crimson_Fluff Don't be silly we don't need an opening sound
                return ActionResultType.SUCCESS;
            }

            NetworkHooks.openGui(
                    (ServerPlayerEntity) player,
                    new INamedContainerProvider() {
                        @Override
                        public ITextComponent getDisplayName() {
                            return new TranslationTextComponent("block.the_vault.vending_machine");
                        }

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
        }
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }

    @OnlyIn(Dist.CLIENT)
    public static void playOpenSound() {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getSoundHandler().play(SimpleSound.master(
                ModSounds.VENDING_MACHINE_SFX, 1f, 1f
        ));
    }

    public static BlockPos getTileEntityPos(BlockState state, BlockPos pos) {
        return state.get(HALF) == DoubleBlockHalf.UPPER
                ? pos.down() : pos;
    }

    public static TileEntity getBlockTileEntity(World world, BlockPos pos, BlockState state) {
        BlockPos vendingMachinePos = getTileEntityPos(state, pos);

        return world.getTileEntity(vendingMachinePos);
    }

}
