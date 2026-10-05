package github.goldblock.goety_aether.common.blocks;

import com.Polarice3.Goety.common.blocks.PedestalBlock;
import github.goldblock.goety_aether.common.blocks.entities.HolyPedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class HolyPedestalBlock extends PedestalBlock {
    public HolyPedestalBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HolyPedestalBlockEntity(pos, state);
    }
}
