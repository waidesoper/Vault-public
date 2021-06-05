package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.server.ServerWorld;

public class ObeliskBlockTileEntity extends TileEntity implements ITickableTileEntity {
    public ObeliskBlockTileEntity() { super(ModBlocks.OBELISK_TILE_ENTITY); }

    @Override
    public void tick() {
        if (! world.isRemote) {
            if (world.getGameTime() % 5 == 0) {
                ((ServerWorld) world).spawnParticle(ParticleTypes.POOF,
                    this.getPos().getX() + 0.5,
                    this.getPos().getY() + 4,
                    this.getPos().getZ() + 0.5,
                    10,
                    0d,
                    6d,
                    0d,
                    0d);
            }
        }
    }
}
