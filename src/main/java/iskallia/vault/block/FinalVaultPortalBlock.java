package iskallia.vault.block;

import iskallia.vault.Vault;
import iskallia.vault.block.entity.VaultPortalTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.CrystalData;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FinalVaultPortalBlock extends NetherPortalBlock {

    public FinalVaultPortalBlock() {
        super(Properties.copy(Blocks.NETHER_PORTAL));
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    protected static BlockPos getSpawnPoint(ServerWorld p_241092_0_, int p_241092_1_, int p_241092_2_, boolean p_241092_3_) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(p_241092_1_, 0, p_241092_2_);
        Biome biome = p_241092_0_.getBiome(blockpos$mutable);
        boolean flag = p_241092_0_.dimensionType().hasCeiling();
        BlockState blockstate = biome.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
        if (p_241092_3_ && ! blockstate.getBlock().is(BlockTags.VALID_SPAWN)) {
            return null;
        } else {
            Chunk chunk = p_241092_0_.getChunk(p_241092_1_ >> 4, p_241092_2_ >> 4);
            int i = flag ? p_241092_0_.getChunkSource().getGenerator().getSpawnHeight() : chunk.getHeight(Heightmap.Type.MOTION_BLOCKING, p_241092_1_ & 15, p_241092_2_ & 15);
            if (i < 0) {
                return null;
            } else {
                int j = chunk.getHeight(Heightmap.Type.WORLD_SURFACE, p_241092_1_ & 15, p_241092_2_ & 15);
                if (j <= i && j > chunk.getHeight(Heightmap.Type.OCEAN_FLOOR, p_241092_1_ & 15, p_241092_2_ & 15)) {
                    return null;
                } else {
                    for (int k = i + 1; k >= 0; -- k) {
                        blockpos$mutable.set(p_241092_1_, k, p_241092_2_);
                        BlockState blockstate1 = p_241092_0_.getBlockState(blockpos$mutable);
                        if (! blockstate1.getFluidState().isEmpty()) {
                            break;
                        }

                        if (blockstate1.equals(blockstate)) {
                            return blockpos$mutable.above().immutable();
                        }
                    }

                    return null;
                }
            }
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.VAULT_PORTAL_TILE_ENTITY.create();
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        //Yeet piglin spawns.
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        World world = null;
        if (worldIn instanceof World)
            world = (World) worldIn;


        //if in overworld, allow the portal to break when frame is broken. like a nether portal.
        if (world != null) {
            if (world.dimension() == World.OVERWORLD) {
                Direction.Axis direction$axis = facing.getAxis();
                Direction.Axis direction$axis1 = stateIn.getValue(AXIS);
                boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
                return ! flag && ! facingState.is(this) && ! (new VaultPortalSize(worldIn, currentPos, direction$axis1)).validatePortal() ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);

            }
        }

        // otherwise, as commented before: yeet auto-connections
        return stateIn;

    }

    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClientSide || ! (entity instanceof PlayerEntity)) return;
        if (entity.isPassenger() || entity.isVehicle() || ! entity.canChangeDimensions()) return;

        ServerPlayerEntity player = (ServerPlayerEntity) entity;
        VoxelShape playerVoxel = VoxelShapes.create(player.getBoundingBox().move(- pos.getX(), - pos.getY(), - pos.getZ()));

        VaultPortalTileEntity portal = getPortalTileEntity(world, pos);
        String playerBossName = portal == null ? "" : portal.getPlayerBossName();

        if (VoxelShapes.joinIsNotEmpty(playerVoxel, state.getShape(world, pos), IBooleanFunction.AND)) {
            RegistryKey<World> worldKey = world.dimension() == Vault.VAULT_KEY ? World.OVERWORLD : Vault.VAULT_KEY;
            ServerWorld destination = ((ServerWorld) world).getServer().getLevel(worldKey);

            if (destination == null) return;

            //Reset cooldown.
            if (player.isOnPortalCooldown()) {
                player.setPortalCooldown();
                return;
            }

            world.getServer().submit(() -> {
                if (worldKey == World.OVERWORLD) {
                    ServerPlayerEntity playerEntity = (ServerPlayerEntity) entity;
                    StringTextComponent text = new StringTextComponent("Ha! No...");
                    text.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_FF0000)));
                    playerEntity.displayClientMessage(text, true);
                } else if (worldKey == Vault.VAULT_KEY) {
                    List<ServerPlayerEntity> players = new ArrayList<>(world.getServer().getPlayerList().getPlayers());

                    if (portal.getData() == null) {
                        portal.setCrystalData(new CrystalData(ItemStack.EMPTY));
                    }

                    VaultRaidData.get(destination).startNew(players, Collections.emptyList(),
                        VaultRarity.OMEGA.ordinal(), playerBossName, portal.getData(), true);
                }
            });

            if (worldKey == Vault.VAULT_KEY) {
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }

            player.setPortalCooldown();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        for (int i = 0; i < 4; ++ i) {
            double d0 = (double) pos.getX() + rand.nextDouble();
            double d1 = (double) pos.getY() + rand.nextDouble();
            double d2 = (double) pos.getZ() + rand.nextDouble();
            double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
            double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
            double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
            int j = rand.nextInt(2) * 2 - 1;

            if (! world.getBlockState(pos.west()).is(this) && ! world.getBlockState(pos.east()).is(this)) {
                d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
                d3 = rand.nextFloat() * 2.0F * (float) j;
            } else {
                d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
                d5 = rand.nextFloat() * 2.0F * (float) j;
            }

            world.addParticle(ParticleTypes.ASH, d0, d1, d2, d3, d4, d5);
        }

    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }


    private VaultPortalTileEntity getPortalTileEntity(World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getBlockEntity(pos);
        return te instanceof VaultPortalTileEntity ? (VaultPortalTileEntity) te : null;
    }

}
