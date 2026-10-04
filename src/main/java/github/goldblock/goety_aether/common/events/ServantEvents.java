package github.goldblock.goety_aether.common.events;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.utils.MobUtil;
import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.entities.ally.mobs.CloudGolemServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.CockatriceServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.SentryServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.Slider;
import github.goldblock.goety_aether.common.entities.ally.mobs.ZombieValkyrieQueenServant;
import github.goldblock.goety_aether.compat.mod.LegendaryMonstersCompat;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GoetyAether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServantEvents {
    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        if (event.getExplosion().getExploder() instanceof SentryServant) {
            event.getAffectedBlocks().clear();
        }
    }

    @SubscribeEvent
    public static void onMobGriefing(EntityMobGriefingEvent event) {
        if (event.getEntity() instanceof ZombieValkyrieQueenServant) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (LegendaryMonstersCompat.isLegendaryMonstersLoaded() && event.getEntity() instanceof CloudGolemServant golem) {
            golem.addDamage(event.getAmount(), event.getSource());
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Slider sliderServant
                && MobUtil.areAllies(sliderServant, event.getEntity())) {
            event.setCanceled(true);
        }
        if (LegendaryMonstersCompat.isLegendaryMonstersLoaded()
                && event.getSource().getEntity() instanceof CloudGolemServant golem && !golem.level().isClientSide()) {
            LivingEntity victim = event.getEntity();
            boolean ally = MobUtil.areAllies(golem, victim)
                    || (victim instanceof IOwned owned && MobUtil.ownerStack(golem, owned));
            if (ally) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            github.goldblock.goety_aether.common.entities.ally.mobs.CreeperReplacementBridge.tryReplace(event.getEntity());
            GoetyAetherConfig.matchAndApply(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onMountEntity(EntityMountEvent event) {
        if (event.isDismounting() && event.getEntityBeingMounted() instanceof CockatriceServant cockatrice) {
            Entity rider = event.getEntityMounting();
            if (rider.isCrouching() && !cockatrice.onGround() && !cockatrice.isInFluidType()) {
                event.setCanceled(true);
            }
        }
    }
}
