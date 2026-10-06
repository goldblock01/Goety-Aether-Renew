package github.goldblock.goety_aether.compat.genesis;

import com.Polarice3.Goety.common.items.ServantSpawnEggItem;
import com.Polarice3.Goety.common.items.magic.MagicFocus;
import com.aetherteam.aether.entity.monster.Swet;
import github.goldblock.goety_aether.common.entities.ally.mobs.BattleSentryServant;
import github.goldblock.goety_aether.common.entities.projectile.TempestThunderBall;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import github.goldblock.goety_aether.common.entities.ally.mobs.DarkSwetServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.SentryGolem;
import github.goldblock.goety_aether.common.entities.ally.mobs.SentryGuardian;
import github.goldblock.goety_aether.common.entities.ally.mobs.TempestServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.TrackingGolem;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import github.goldblock.goety_aether.common.init.ModItems;
import github.goldblock.goety_aether.common.magic.spells.BattleSentryFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.SentryGolemFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.TempestFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.TrackingGolemFocusSpell;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.RegistryObject;

public class GenesisCompatManager {
    public static RegistryObject<EntityType<BattleSentryServant>> BATTLE_SENTRY_SERVANT;
    public static RegistryObject<ServantSpawnEggItem> BATTLE_SENTRY_SERVANT_SPAWN_EGG;
    public static RegistryObject<EntityType<TrackingGolem>> TRACKING_GOLEM;
    public static RegistryObject<ServantSpawnEggItem> TRACKING_GOLEM_SPAWN_EGG;
    public static RegistryObject<EntityType<SentryGuardian>> SENTRY_GUARDIAN;
    public static RegistryObject<ServantSpawnEggItem> SENTRY_GUARDIAN_SPAWN_EGG;
    public static RegistryObject<EntityType<DarkSwetServant>> DARK_SWET_SERVANT;
    public static RegistryObject<ServantSpawnEggItem> DARK_SWET_SERVANT_SPAWN_EGG;
    public static RegistryObject<EntityType<SentryGolem>> SENTRY_GOLEM;
    public static RegistryObject<ServantSpawnEggItem> SENTRY_GOLEM_SPAWN_EGG;
    public static RegistryObject<EntityType<TempestServant>> TEMPEST_SERVANT;
    public static RegistryObject<ServantSpawnEggItem> TEMPEST_SERVANT_SPAWN_EGG;
    public static RegistryObject<EntityType<TempestThunderBall>> TEMPEST_THUNDERBALL;
    public static RegistryObject<Item> TEMPEST_FOCUS;
    public static RegistryObject<MagicFocus> BATTLE_SENTRY_FOCUS;
    public static RegistryObject<MagicFocus> TRACKING_GOLEM_FOCUS;
    public static RegistryObject<MagicFocus> SENTRY_GOLEM_FOCUS;

    public static void init() {
        BATTLE_SENTRY_SERVANT = ModEntityTypes.ENTITY_TYPES.register("battle_sentry_servant",
                () -> EntityType.Builder.of(BattleSentryServant::new, MobCategory.MONSTER).sized(2.0F, 2.0F).clientTrackingRange(10).build("battle_sentry_servant"));
        BATTLE_SENTRY_SERVANT_SPAWN_EGG = ModItems.ITEMS.register("battle_sentry_servant_spawn_egg",
                () -> new ServantSpawnEggItem(BATTLE_SENTRY_SERVANT, 0x808080, 0x79D06A, new Item.Properties()));
        TRACKING_GOLEM = ModEntityTypes.ENTITY_TYPES.register("tracking_golem",
                () -> EntityType.Builder.of(TrackingGolem::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build("tracking_golem"));
        TRACKING_GOLEM_SPAWN_EGG = ModItems.ITEMS.register("tracking_golem_spawn_egg",
                () -> new ServantSpawnEggItem(TRACKING_GOLEM, 0x555561, 0x5CBEFF, new Item.Properties()));
        SENTRY_GUARDIAN = ModEntityTypes.ENTITY_TYPES.register("sentry_guardian",
                () -> EntityType.Builder.of(SentryGuardian::new, MobCategory.MONSTER).sized(2.25F, 2.5F).fireImmune().clientTrackingRange(10).build("sentry_guardian"));
        SENTRY_GUARDIAN_SPAWN_EGG = ModItems.ITEMS.register("sentry_guardian_spawn_egg",
                () -> new ServantSpawnEggItem(SENTRY_GUARDIAN, 0x555561, 0x81E3FF, new Item.Properties()));
        DARK_SWET_SERVANT = ModEntityTypes.ENTITY_TYPES.register("dark_swet_servant",
                () -> EntityType.Builder.of(DarkSwetServant::new, MobCategory.MONSTER).sized(0.9F, 0.9F).clientTrackingRange(10).build("dark_swet_servant"));
        DARK_SWET_SERVANT_SPAWN_EGG = ModItems.ITEMS.register("dark_swet_servant_spawn_egg",
                () -> new ServantSpawnEggItem(DARK_SWET_SERVANT, 9731524, 5222874, new Item.Properties()));
        SENTRY_GOLEM = ModEntityTypes.ENTITY_TYPES.register("sentry_golem",
                () -> EntityType.Builder.of(SentryGolem::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build("sentry_golem"));
        SENTRY_GOLEM_SPAWN_EGG = ModItems.ITEMS.register("sentry_golem_spawn_egg",
                () -> new ServantSpawnEggItem(SENTRY_GOLEM, 0x555561, 0xB9FFA3, new Item.Properties()));
        TEMPEST_SERVANT = ModEntityTypes.ENTITY_TYPES.register("tempest_servant",
                () -> EntityType.Builder.of(TempestServant::new, MobCategory.MONSTER).sized(2.5F, 1.75F).fireImmune().clientTrackingRange(10).build("tempest_servant"));
        TEMPEST_SERVANT_SPAWN_EGG = ModItems.ITEMS.register("tempest_servant_spawn_egg",
                () -> new ServantSpawnEggItem(TEMPEST_SERVANT, 0x3C464C, 0xC3E6F0, new Item.Properties()));
        TEMPEST_THUNDERBALL = ModEntityTypes.ENTITY_TYPES.register("tempest_thunderball",
                () -> EntityType.Builder.<TempestThunderBall>of(TempestThunderBall::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build("tempest_thunderball"));
        TEMPEST_FOCUS = ModItems.ITEMS.register("tempest_focus",
                () -> new MagicFocus(new TempestFocusSpell()));
        BATTLE_SENTRY_FOCUS = ModItems.ITEMS.register("battle_sentry_focus",
                () -> new MagicFocus(new BattleSentryFocusSpell()));
        TRACKING_GOLEM_FOCUS = ModItems.ITEMS.register("tracking_golem_focus",
                () -> new MagicFocus(new TrackingGolemFocusSpell()));
        SENTRY_GOLEM_FOCUS = ModItems.ITEMS.register("sentry_golem_focus",
                () -> new MagicFocus(new SentryGolemFocusSpell()));
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(BATTLE_SENTRY_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.BATTLE_SENTRY_SERVANT_HEALTH, GoetyAetherConfig.BATTLE_SENTRY_SERVANT_DAMAGE,
                net.minecraft.world.entity.Mob.createMobAttributes()
                        .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 10.0D)
                        .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.8D)
                        .add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 4.0D)).build());
        event.put(TRACKING_GOLEM.get(), GoetyAetherConfig.servant(GoetyAetherConfig.TRACKING_GOLEM_HEALTH, GoetyAetherConfig.TRACKING_GOLEM_DAMAGE, TrackingGolem.createAttributes()).build());
        event.put(SENTRY_GUARDIAN.get(), GoetyAetherConfig.servant(GoetyAetherConfig.SENTRY_GUARDIAN_HEALTH, GoetyAetherConfig.SENTRY_GUARDIAN_DAMAGE, SentryGuardian.createAttributes()).build());
        event.put(DARK_SWET_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.DARK_SWET_SERVANT_HEALTH, GoetyAetherConfig.DARK_SWET_SERVANT_DAMAGE, Swet.createMobAttributes()).build());
        event.put(SENTRY_GOLEM.get(), GoetyAetherConfig.servant(GoetyAetherConfig.SENTRY_GOLEM_HEALTH, GoetyAetherConfig.SENTRY_GOLEM_DAMAGE, SentryGolem.createAttributes()).build());
        event.put(TEMPEST_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.TEMPEST_SERVANT_HEALTH, GoetyAetherConfig.TEMPEST_SERVANT_DAMAGE, TempestServant.createAttributes()).build());
    }
}
