package github.goldblock.goety_aether.common.blocks.entities;

import com.Polarice3.Goety.common.blocks.entities.PedestalBlockEntity;
import github.goldblock.goety_aether.common.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class HolyPedestalBlockEntity extends PedestalBlockEntity {
    public HolyPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PEDESTAL_HOLYSTONE.get(), pos, state);
    }
}
