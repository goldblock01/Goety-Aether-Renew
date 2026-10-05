package github.goldblock.goety_aether.common.events;

import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = GoetyAether.MOD_ID, bus = Bus.FORGE)
public class UnboundedBucketEvents {
    private static final ResourceLocation SKYROOT_WATER_BUCKET_ID = new ResourceLocation("aether", "skyroot_water_bucket");
    private static final ResourceLocation HAUNTED_JUG_ID = new ResourceLocation("goety", "haunted_jug");
    private static final ResourceLocation THE_AETHER_ID = new ResourceLocation("aether", "the_aether");

    @SubscribeEvent
    public static void onRightClickBlock(RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        if (player == null || level.isClientSide) {
            return;
        }
        ResourceLocation dimensionId = level.dimension().location();
        if (dimensionId == null || !dimensionId.equals(THE_AETHER_ID)) {
            return;
        }
        ItemStack stack = event.getItemStack();
        BlockPos pos = event.getPos();
        Block block = level.getBlockState(pos).getBlock();
        if (isBlock(block, HAUNTED_JUG_ID)) {
            if (isItem(stack, SKYROOT_WATER_BUCKET_ID)) {
                ItemStack unbounded = new ItemStack(ModItems.UNBOUNDED_WATER_BUCKET.get());
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                if (!player.addItem(unbounded)) {
                    player.drop(unbounded, false);
                }
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            } else if (isItem(stack, ModItems.UNBOUNDED_WATER_BUCKET.getId())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.PASS);
            }
        }
    }

    private static boolean isItem(ItemStack stack, ResourceLocation id) {
        if (stack.isEmpty()) {
            return false;
        }
        ResourceLocation stackId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return stackId != null && stackId.equals(id);
    }

    private static boolean isBlock(Block block, ResourceLocation id) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
        return blockId != null && blockId.equals(id);
    }
}
