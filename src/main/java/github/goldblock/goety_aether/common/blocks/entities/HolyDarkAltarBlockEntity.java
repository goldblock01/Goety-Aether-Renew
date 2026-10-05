package github.goldblock.goety_aether.common.blocks.entities;

import com.Polarice3.Goety.common.blocks.entities.DarkAltarBlockEntity;
import github.goldblock.goety_aether.common.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HolyDarkAltarBlockEntity extends DarkAltarBlockEntity {
    public HolyDarkAltarBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.DARK_ALTAR_HOLYSTONE.get();
    }
}
