package github.goldblock.goety_aether.common.events;

import com.Polarice3.Goety.utils.CuriosFinder;
import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.init.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GoetyAether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AetherAccessRingEvents {
    private static final ThreadLocal<Float> ORIGINAL_DAMAGE = new ThreadLocal<>();

    private static boolean hasRing(Player player) {
        return CuriosFinder.hasCurio(player, ModItems.AETHER_ACCESS_RING.get());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (hasRing(player)) {
            event.setNewSpeed(event.getOriginalSpeed());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDamageHighest(LivingDamageEvent event) {
        LivingEntity target = event.getEntity();
        Entity sourceEntity = event.getSource().getDirectEntity();
        if (sourceEntity instanceof Player || target instanceof Player) {
            ORIGINAL_DAMAGE.set(event.getAmount());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDamageLowest(LivingDamageEvent event) {
        LivingEntity target = event.getEntity();
        Entity sourceEntity = event.getSource().getDirectEntity();
        Player player = null;
        if (sourceEntity instanceof Player) {
            player = (Player) sourceEntity;
        } else if (target instanceof Player) {
            player = (Player) target;
        }
        if (player != null && hasRing(player)) {
            Float original = ORIGINAL_DAMAGE.get();
            if (original != null) {
                event.setAmount(original);
            }
        }
        ORIGINAL_DAMAGE.remove();
    }
}
