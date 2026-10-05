package github.goldblock.goety_aether.common.items.bucket;

import com.aetherteam.aether.item.miscellaneous.bucket.SkyrootBucketItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.ForgeEventFactory;

public class UnboundedWaterBucketItem extends SkyrootBucketItem {
    public UnboundedWaterBucketItem(Item.Properties properties) {
        super(() -> Fluids.WATER, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        InteractionResultHolder<ItemStack> interactionResult = ForgeEventFactory.onBucketUse(player, level, heldStack, blockHitResult);
        if (interactionResult != null) {
            return interactionResult;
        }
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(heldStack);
        }
        BlockPos blockPos = blockHitResult.getBlockPos();
        Direction direction = blockHitResult.getDirection();
        BlockPos relativePos = blockPos.relative(direction);
        if (level.mayInteract(player, blockPos) && player.mayUseItemAt(relativePos, direction, heldStack)) {
            BlockState state = level.getBlockState(blockPos);
            if (getFluid() == Fluids.WATER && state.getBlock() instanceof BucketPickup bucketPickup) {
                ItemStack picked = bucketPickup.pickupBlock(level, blockPos, state);
                if (!picked.isEmpty()) {
                    SoundEvent sound = bucketPickup.getPickupSound(state).orElse(null);
                    if (sound != null) {
                        player.playSound(sound, 1.0F, 1.0F);
                    }
                    level.gameEvent(player, net.minecraft.world.level.gameevent.GameEvent.FLUID_PICKUP, blockPos);
                    return InteractionResultHolder.success(heldStack);
                }
            }
            BlockPos newPos = canBlockContainFluid(level, blockPos, state) ? blockPos : relativePos;
            if (emptyContents(player, level, newPos, blockHitResult, heldStack)) {
                player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(this));
                return InteractionResultHolder.success(heldStack);
            }
        }
        return InteractionResultHolder.fail(heldStack);
    }
}
