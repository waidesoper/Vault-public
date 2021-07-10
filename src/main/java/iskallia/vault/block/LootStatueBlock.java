package iskallia.vault.block;

import iskallia.vault.block.entity.LootStatueTileEntity;
import iskallia.vault.container.RenamingContainer;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.RenameType;
import iskallia.vault.util.StatueType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class LootStatueBlock extends Block {

    public static final VoxelShape SHAPE_GIFT_NORMAL = Block.box(1, 0, 1, 15, 5, 15);
    public static final VoxelShape SHAPE_GIFT_MEGA = Block.box(1, 0, 1, 15, 13, 15);
    public static final VoxelShape SHAPE_PLAYER_STATUE = Block.box(1, 0, 1, 15, 5, 15);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public StatueType type;

    public LootStatueBlock(StatueType type) {
        super(Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(1.0F, 3600000.0F)
            .noOcclusion()
            .noCollission());
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.SOUTH));

        this.type = type;

    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (world.isClientSide) return ActionResultType.SUCCESS;

        TileEntity te = world.getBlockEntity(pos);
        if (! (te instanceof LootStatueTileEntity)) return ActionResultType.SUCCESS;

        LootStatueTileEntity statue = (LootStatueTileEntity) te;

        // remove a chip
        if (player.isShiftKeyDown()) {
            ItemStack chip = statue.removeChip();
            if (chip != ItemStack.EMPTY) {
                if (! player.addItem(chip)) {
                    player.drop(chip, false);
                }
            }
            return ActionResultType.SUCCESS;
        }

        ItemStack heldItem = player.getMainHandItem();
        // rename the player in the statue
//        if (heldItem == ItemStack.EMPTY) {

        // #Crimson_Fluff, Issue #275, some reason some empty hotbar slots don't allow you to change the name of the statue. Confirmed by Me !
        if (heldItem.isEmpty()) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("RenameType", RenameType.PLAYER_STATUE.ordinal());
            nbt.put("Data", statue.serializeNBT());

            NetworkHooks.openGui(
                (ServerPlayerEntity) player,
                new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() { return null; }       // #Crimson_Fluff, name not actually used in gui screen

                    @Nullable
                    @Override
                    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                        return new RenamingContainer(windowId, nbt);
                    }
                },
                (buffer) -> {
                    buffer.writeNbt(nbt);
                }
            );
            return ActionResultType.SUCCESS;
            // add chip
        } else if (heldItem.getItem() == ModItems.ACCELERATION_CHIP) {
            if (statue.addChip()) {
                if (! player.isCreative())
                    heldItem.shrink(1);     // #Crimson_Fluff, shrink()
                return ActionResultType.SUCCESS;
            }
        }

        return super.use(state, world, pos, player, handIn, hit);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity tileEntity = world.getBlockEntity(pos);

        if (tileEntity instanceof LootStatueTileEntity) {
            LootStatueTileEntity lootStatue = (LootStatueTileEntity) tileEntity;
            if (stack.hasTag()) {
                CompoundNBT nbt = stack.getTag();
                CompoundNBT blockEntityTag = nbt.getCompound("BlockEntityTag");
                String playerNickname = blockEntityTag.getString("PlayerNickname");
                lootStatue.setInterval(blockEntityTag.getInt("Interval"));
                lootStatue.setLootItem(ItemStack.of(blockEntityTag.getCompound("LootItem")));
                lootStatue.setStatueType(StatueType.values()[blockEntityTag.getInt("StatueType")]);
                lootStatue.setCurrentTick(blockEntityTag.getInt("CurrentTick"));
                lootStatue.setHasCrown(blockEntityTag.getBoolean("HasCrown"));
                lootStatue.getSkin().updateSkin(playerNickname);
            }
        }
        super.setPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (! world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            ItemStack itemStack = new ItemStack(getBlock());

            if (tileEntity instanceof LootStatueTileEntity) {
                LootStatueTileEntity statueTileEntity = (LootStatueTileEntity) tileEntity;

                CompoundNBT statueNBT = statueTileEntity.serializeNBT();
                CompoundNBT stackNBT = new CompoundNBT();
                stackNBT.put("BlockEntityTag", statueNBT);
                itemStack.setTag(stackNBT);
            }

            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
            itemEntity.setDefaultPickUpDelay();
            world.addFreshEntity(itemEntity);
        }

        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.LOOT_STATUE_TILE_ENTITY.create();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        World world = context.getLevel();
        if (pos.getY() < 255 && world.getBlockState(pos.above()).canBeReplaced(context)) {
            return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
        } else {
            return null;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (this.getType()) {
            case GIFT_NORMAL:
                return SHAPE_GIFT_NORMAL;
            case GIFT_MEGA:
                return SHAPE_GIFT_MEGA;
            case VAULT_BOSS:
                return SHAPE_PLAYER_STATUE;
        }
        return Block.box(0, 0, 0, 16, 16, 16);
    }

    public StatueType getType() {
        return type;
    }
}
