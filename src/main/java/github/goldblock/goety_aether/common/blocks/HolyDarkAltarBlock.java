package github.goldblock.goety_aether.common.blocks;

import com.Polarice3.Goety.common.blocks.DarkAltarBlock;
import github.goldblock.goety_aether.common.blocks.entities.HolyDarkAltarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class HolyDarkAltarBlock extends DarkAltarBlock {
    public HolyDarkAltarBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HolyDarkAltarBlockEntity(pos, state);
    }
}
