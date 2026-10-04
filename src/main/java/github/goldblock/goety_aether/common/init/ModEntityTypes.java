package github.goldblock.goety_aether.common.init;

import com.aetherteam.aether.entity.monster.AechorPlant;
import com.aetherteam.aether.entity.monster.Cockatrice;
import com.aetherteam.aether.entity.monster.Swet;
import com.aetherteam.aether.entity.monster.dungeon.FireMinion;
import com.aetherteam.aether.entity.monster.dungeon.Sentry;
import com.aetherteam.aether.entity.monster.dungeon.Valkyrie;
import com.aetherteam.aether.entity.monster.dungeon.boss.ValkyrieQueen;
import com.Polarice3.Goety.common.entities.ally.BlackWolf;
import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import github.goldblock.goety_aether.common.entities.ally.mobs.BlueSwetServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.AechorPlantServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.CockatriceServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.CreeperServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.EvilWhirlwindServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.PassiveWhirlwindServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.FireMinionServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.GoldenSwetServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.SentryServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.SkyWolfServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.Slider;
import github.goldblock.goety_aether.common.entities.ally.mobs.SunSpiritServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.ValkyrieServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.ZephyrServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.ZombieValkyrieQueenServant;
import github.goldblock.goety_aether.common.entities.hostile.SkyWolf;
import github.goldblock.goety_aether.common.entities.hostile.illagers.Mountaineer;
import github.goldblock.goety_aether.common.entities.hostile.illagers.WindCaller;
import github.goldblock.goety_aether.common.entities.projectile.FireCrystal;
import github.goldblock.goety_aether.common.entities.projectile.IceCrystal;
import github.goldblock.goety_aether.common.entities.projectile.ThunderCrystal;
import github.goldblock.goety_aether.common.entities.projectile.RainPoisonDart;
import github.goldblock.goety_aether.common.entities.projectile.SentryBomb;
import github.goldblock.goety_aether.compat.deep_aether.DeepAetherCompatManager;
import github.goldblock.goety_aether.compat.genesis.GenesisCompatManager;
import github.goldblock.goety_aether.compat.legendary_monsters.LegendaryMonstersCompatManager;
import github.goldblock.goety_aether.compat.mod.AetherDeepAetherCompat;
import github.goldblock.goety_aether.compat.mod.AetherGenesisCompat;
import github.goldblock.goety_aether.compat.mod.AetherReduxCompat;
import github.goldblock.goety_aether.compat.mod.LegendaryMonstersCompat;
import github.goldblock.goety_aether.compat.redux.ReduxCompatManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GoetyAether.MOD_ID);

    public static final RegistryObject<EntityType<SentryServant>> SENTRY_SERVANT = ENTITY_TYPES.register("sentry_servant",
            () -> EntityType.Builder.of(SentryServant::new, MobCategory.MONSTER).sized(2.0F, 2.0F).clientTrackingRange(10).build("sentry_servant"));

    public static final RegistryObject<EntityType<Slider>> SLIDER = ENTITY_TYPES.register("slider",
            () -> EntityType.Builder.of(Slider::new, MobCategory.MONSTER).sized(2.0F, 2.0F).clientTrackingRange(10).build("slider"));

    public static final RegistryObject<EntityType<ValkyrieServant>> VALKYRIE_SERVANT = ENTITY_TYPES.register("valkyrie_servant",
            () -> EntityType.Builder.of(ValkyrieServant::new, MobCategory.MONSTER).sized(0.8F, 1.95F).clientTrackingRange(8).build("valkyrie_servant"));

    public static final RegistryObject<EntityType<ZombieValkyrieQueenServant>> ZOMBIE_VALKYRIE_QUEEN_SERVANT = ENTITY_TYPES.register("zombie_valkyrie_queen",
            () -> EntityType.Builder.of(ZombieValkyrieQueenServant::new, MobCategory.MONSTER).sized(0.8F, 1.95F).clientTrackingRange(10).build("zombie_valkyrie_queen"));

    public static final RegistryObject<EntityType<FireMinionServant>> FIRE_MINION_SERVANT = ENTITY_TYPES.register("fire_minion_servant",
            () -> EntityType.Builder.of(FireMinionServant::new, MobCategory.MONSTER).sized(1.1F, 1.95F).fireImmune().clientTrackingRange(8).build("fire_minion_servant"));

    public static final RegistryObject<EntityType<WindCaller>> WIND_CALLER = ENTITY_TYPES.register("wind_caller",
            () -> EntityType.Builder.of(WindCaller::new, MobCategory.MONSTER).canSpawnFarFromPlayer().sized(0.6F, 1.95F).clientTrackingRange(10).build("wind_caller"));

    public static final RegistryObject<EntityType<Mountaineer>> MOUNTAINEER = ENTITY_TYPES.register("mountaineer",
            () -> EntityType.Builder.of(Mountaineer::new, MobCategory.MONSTER).canSpawnFarFromPlayer().sized(0.6F, 1.95F).clientTrackingRange(10).build("mountaineer"));

    public static final RegistryObject<EntityType<GoldenSwetServant>> GOLDEN_SWET_SERVANT = ENTITY_TYPES.register("golden_swet_servant",
            () -> EntityType.Builder.of(GoldenSwetServant::new, MobCategory.MONSTER).sized(0.9F, 0.9F).clientTrackingRange(10).build("golden_swet_servant"));

    public static final RegistryObject<EntityType<BlueSwetServant>> BLUE_SWET_SERVANT = ENTITY_TYPES.register("blue_swet_servant",
            () -> EntityType.Builder.of(BlueSwetServant::new, MobCategory.MONSTER).sized(0.9F, 0.9F).clientTrackingRange(10).build("blue_swet_servant"));

    public static final RegistryObject<EntityType<SentryBomb>> SENTRY_BOMB = ENTITY_TYPES.register("sentry_bomb",
            () -> EntityType.Builder.<SentryBomb>of(SentryBomb::new, MobCategory.MISC).sized(0.9F, 0.9F).fireImmune().clientTrackingRange(4).updateInterval(10).build("sentry_bomb"));

    public static final RegistryObject<EntityType<FireCrystal>> FIRE_CRYSTAL = ENTITY_TYPES.register("fire_crystal",
            () -> EntityType.Builder.<FireCrystal>of(FireCrystal::new, MobCategory.MISC).sized(0.85F, 0.85F).fireImmune().clientTrackingRange(4).updateInterval(10).build("fire_crystal"));

    public static final RegistryObject<EntityType<IceCrystal>> ICE_CRYSTAL = ENTITY_TYPES.register("ice_crystal",
            () -> EntityType.Builder.<IceCrystal>of(IceCrystal::new, MobCategory.MISC).sized(1.2F, 1.2F).fireImmune().clientTrackingRange(4).updateInterval(10).build("ice_crystal"));

    public static final RegistryObject<EntityType<ThunderCrystal>> THUNDER_CRYSTAL = ENTITY_TYPES.register("thunder_crystal",
            () -> EntityType.Builder.<ThunderCrystal>of(ThunderCrystal::new, MobCategory.MISC).sized(0.7F, 0.7F).updateInterval(2).build("thunder_crystal"));

    public static final RegistryObject<EntityType<RainPoisonDart>> RAIN_POISON_DART = ENTITY_TYPES.register("rain_poison_dart",
            () -> EntityType.Builder.<RainPoisonDart>of(RainPoisonDart::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build("rain_poison_dart"));

    public static final RegistryObject<EntityType<ZephyrServant>> ZEPHYR_SERVANT = ENTITY_TYPES.register("zephyr_servant",
            () -> EntityType.Builder.of(ZephyrServant::new, MobCategory.MONSTER).sized(4.0F, 4.0F).clientTrackingRange(10).build("zephyr_servant"));

    public static final RegistryObject<EntityType<CockatriceServant>> COCKATRICE_SERVANT = ENTITY_TYPES.register("cockatrice_servant",
            () -> EntityType.Builder.of(CockatriceServant::new, MobCategory.MONSTER).sized(0.9F, 2.15F).clientTrackingRange(10).build("cockatrice_servant"));

    public static final RegistryObject<EntityType<SunSpiritServant>> SUN_SPIRIT_SERVANT = ENTITY_TYPES.register("sun_spirit_servant",
            () -> EntityType.Builder.of(SunSpiritServant::new, MobCategory.CREATURE).sized(2.0F, 2.5F).fireImmune().clientTrackingRange(10).build("sun_spirit_servant"));

    public static final RegistryObject<EntityType<SkyWolfServant>> SKY_WOLF_SERVANT = ENTITY_TYPES.register("sky_wolf_servant",
            () -> EntityType.Builder.of(SkyWolfServant::new, MobCategory.CREATURE).sized(0.6F, 0.8F).clientTrackingRange(10).build("sky_wolf_servant"));

    public static final RegistryObject<EntityType<SkyWolf>> SKY_WOLF = ENTITY_TYPES.register("sky_wolf",
            () -> EntityType.Builder.of(SkyWolf::new, MobCategory.MONSTER).sized(0.6F, 0.8F).clientTrackingRange(10).build("sky_wolf"));

    public static final RegistryObject<EntityType<AechorPlantServant>> AECHOR_PLANT_SERVANT = ENTITY_TYPES.register("aechor_plant_servant",
            () -> EntityType.Builder.of(AechorPlantServant::new, MobCategory.MONSTER).fireImmune().sized(1.0F, 1.0F).clientTrackingRange(8).build("aechor_plant_servant"));

    public static final RegistryObject<EntityType<PassiveWhirlwindServant>> PASSIVE_WHIRLWIND_SERVANT = ENTITY_TYPES.register("passive_whirlwind_servant",
            () -> EntityType.Builder.of(PassiveWhirlwindServant::new, MobCategory.MONSTER).fireImmune().sized(1.0F, 1.0F).clientTrackingRange(8).build("passive_whirlwind_servant"));

    public static final RegistryObject<EntityType<EvilWhirlwindServant>> EVIL_WHIRLWIND_SERVANT = ENTITY_TYPES.register("evil_whirlwind_servant",
            () -> EntityType.Builder.of(EvilWhirlwindServant::new, MobCategory.MONSTER).fireImmune().sized(1.0F, 1.0F).clientTrackingRange(8).build("evil_whirlwind_servant"));

    public static final RegistryObject<EntityType<CreeperServant>> CREEPER_SERVANT = ENTITY_TYPES.register("creeper_servant",
            () -> EntityType.Builder.of(CreeperServant::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8).build("creeper_servant"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(SENTRY_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.SENTRY_SERVANT_HEALTH, GoetyAetherConfig.SENTRY_SERVANT_DAMAGE, Sentry.createMobAttributes()).build());
        event.put(SLIDER.get(), GoetyAetherConfig.servant(GoetyAetherConfig.SLIDER_HEALTH, GoetyAetherConfig.SLIDER_DAMAGE, com.aetherteam.aether.entity.monster.dungeon.boss.Slider.createMobAttributes()).build());
        event.put(VALKYRIE_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.VALKYRIE_SERVANT_HEALTH, GoetyAetherConfig.VALKYRIE_SERVANT_DAMAGE, Valkyrie.createMobAttributes()).build());
        event.put(ZOMBIE_VALKYRIE_QUEEN_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.ZOMBIE_VALKYRIE_QUEEN_SERVANT_HEALTH, GoetyAetherConfig.ZOMBIE_VALKYRIE_QUEEN_SERVANT_DAMAGE, ValkyrieQueen.createMobAttributes()).build());
        event.put(FIRE_MINION_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.FIRE_MINION_SERVANT_HEALTH, GoetyAetherConfig.FIRE_MINION_SERVANT_DAMAGE, FireMinion.createMobAttributes()).build());
        event.put(WIND_CALLER.get(), WindCaller.setCustomAttributes().build());
        event.put(MOUNTAINEER.get(), Mountaineer.setCustomAttributes().build());
        event.put(GOLDEN_SWET_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.GOLDEN_SWET_SERVANT_HEALTH, GoetyAetherConfig.GOLDEN_SWET_SERVANT_DAMAGE, Swet.createMobAttributes()).build());
        event.put(BLUE_SWET_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.BLUE_SWET_SERVANT_HEALTH, GoetyAetherConfig.BLUE_SWET_SERVANT_DAMAGE, Swet.createMobAttributes()).build());
        event.put(ZEPHYR_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.ZEPHYR_SERVANT_HEALTH, GoetyAetherConfig.ZEPHYR_SERVANT_DAMAGE, ZephyrServant.createAttributes()).build());
        event.put(COCKATRICE_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.COCKATRICE_SERVANT_HEALTH, GoetyAetherConfig.COCKATRICE_SERVANT_DAMAGE,
                Cockatrice.createMobAttributes().add(Attributes.ATTACK_DAMAGE, 3.0D)).build());
        event.put(SUN_SPIRIT_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.SUN_SPIRIT_SERVANT_HEALTH, GoetyAetherConfig.SUN_SPIRIT_SERVANT_DAMAGE,
                SunSpiritServant.createAttributes()).build());
        event.put(SKY_WOLF_SERVANT.get(), BlackWolf.setCustomAttributes().build());
        event.put(SKY_WOLF.get(), BlackWolf.setCustomAttributes().build());
        event.put(AECHOR_PLANT_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.AECHOR_PLANT_SERVANT_HEALTH, GoetyAetherConfig.AECHOR_PLANT_SERVANT_DAMAGE, AechorPlantServant.createAttributes()).build());
        event.put(PASSIVE_WHIRLWIND_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.WHIRLWIND_SERVANT_HEALTH, GoetyAetherConfig.WHIRLWIND_SERVANT_DAMAGE, PassiveWhirlwindServant.createAttributes()).build());
        event.put(EVIL_WHIRLWIND_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.WHIRLWIND_SERVANT_HEALTH, GoetyAetherConfig.WHIRLWIND_SERVANT_DAMAGE, EvilWhirlwindServant.createAttributes()).build());
        event.put(CREEPER_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.CREEPER_SERVANT_HEALTH, GoetyAetherConfig.CREEPER_SERVANT_DAMAGE, CreeperServant.createAttributes()).build());
        if (AetherGenesisCompat.isGenesisLoaded()) {
            GenesisCompatManager.registerAttributes(event);
        }
        if (AetherReduxCompat.isReduxLoaded()) {
            ReduxCompatManager.registerAttributes(event);
        }
        if (AetherDeepAetherCompat.isDeepAetherLoaded()) {
            DeepAetherCompatManager.registerAttributes(event);
        }
        if (LegendaryMonstersCompat.isLegendaryMonstersLoaded()) {
            LegendaryMonstersCompatManager.registerAttributes(event);
        }
    }
}
