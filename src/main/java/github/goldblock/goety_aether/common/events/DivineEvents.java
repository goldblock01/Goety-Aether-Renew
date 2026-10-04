package github.goldblock.goety_aether.common.events;

import com.Polarice3.Goety.utils.CuriosFinder;
import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.items.divine.DivineRobeItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GoetyAether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DivineEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();
        if (!CuriosFinder.hasCurio(victim, stack -> stack.getItem() instanceof DivineRobeItem)) {
            return;
        }
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity living && living.getMobType() == MobType.UNDEAD) {
            event.setAmount(event.getAmount() * (1.0F - 20.0F / 100.0F));
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (CuriosFinder.hasCurio(event.getEntity(), stack -> stack.getItem() instanceof DivineRobeItem)) {
            event.setCanceled(true);
        }
    }
}
