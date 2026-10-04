package github.goldblock.goety_aether.common.events;

import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.init.ModEffects;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GoetyAether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PickaxeAttackEvents {
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        BlockState state = event.getState();
        if (player != null && player.hasEffect(ModEffects.PICKAXE_ATTACK.get())
                && state.is(BlockTags.MINEABLE_WITH_PICKAXE)
                && !player.getMainHandItem().isCorrectToolForDrops(state)) {
            event.setNewSpeed(event.getNewSpeed() * Tiers.IRON.getSpeed());
        }
    }

    @SubscribeEvent
    public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        Player player = event.getEntity();
        BlockState state = event.getTargetBlock();
        if (player != null && player.hasEffect(ModEffects.PICKAXE_ATTACK.get())
                && state.is(BlockTags.MINEABLE_WITH_PICKAXE)
                && TierSortingRegistry.isCorrectTierForDrops(Tiers.IRON, state)) {
            event.setCanHarvest(true);
        }
    }
}
