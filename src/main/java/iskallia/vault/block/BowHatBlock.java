package iskallia.vault.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BowHatBlock extends Block {

    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 6, 14);

    public BowHatBlock() {
        super(Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(1.0F, 3600000.0F)
            .noOcclusion()
            .noCollission());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

}
