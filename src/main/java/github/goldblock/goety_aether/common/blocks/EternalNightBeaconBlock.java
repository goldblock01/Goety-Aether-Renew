package github.goldblock.goety_aether.common.blocks;

import github.goldblock.goety_aether.common.blocks.entities.EternalNightBeaconBlockEntity;
import github.goldblock.goety_aether.common.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nullable;

public class EternalNightBeaconBlock extends BaseEntityBlock implements BeaconBeamBlock {
    public EternalNightBeaconBlock() {
        super(Properties.of()
                .mapColor(MapColor.COLOR_BLACK)
                .strength(3.0F)
                .lightLevel((state) -> 15)
                .noOcclusion()
                .isRedstoneConductor(EternalNightBeaconBlock::never));
    }

    private static boolean never(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof EternalNightBeaconBlockEntity beacon) {
            beacon.setDayLight(level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT));
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EternalNightBeaconBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.ETERNAL_NIGHT_BEACON.get(), EternalNightBeaconBlockEntity::tick);
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.BLACK;
    }
}
