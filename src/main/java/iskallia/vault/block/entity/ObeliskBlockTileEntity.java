package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.server.ServerWorld;

public class ObeliskBlockTileEntity extends TileEntity implements ITickableTileEntity {
    public ObeliskBlockTileEntity() { super(ModBlocks.OBELISK_BLOCK_TILE_ENTITY); }

    @Override
    public void tick() {
        if (! level.isClientSide) {
            if (level.getGameTime() % 5 == 0) {
                ((ServerWorld) level).sendParticles(ParticleTypes.POOF,
                    this.getBlockPos().getX() + 0.5,
                    this.getBlockPos().getY() + level.random.nextInt(10) + 4,
                    this.getBlockPos().getZ() + 0.5,
                    10,
                    0d,
                    2d,
                    0d,
                    0d);
            }
        }
    }
}
