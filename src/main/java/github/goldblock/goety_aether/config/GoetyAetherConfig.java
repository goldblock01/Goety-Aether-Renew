package github.goldblock.goety_aether.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import github.goldblock.goety_aether.compat.deep_aether.DeepAetherCompatManager;
import github.goldblock.goety_aether.compat.genesis.GenesisCompatManager;
import github.goldblock.goety_aether.compat.redux.ReduxCompatManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.io.File;
import java.util.List;

@Mod.EventBusSubscriber(modid = GoetyAether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GoetyAetherConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue SENTRY_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue SENTRY_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue SENTRY_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue BATTLE_SENTRY_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue BATTLE_SENTRY_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue BATTLE_SENTRY_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue TRACKING_GOLEM_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue TRACKING_GOLEM_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue TRACKING_GOLEM_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue SENTRY_GOLEM_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue SENTRY_GOLEM_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue SENTRY_GOLEM_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue PICKAXE_ATTACK_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue PICKAXE_ATTACK_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue PICKAXE_ATTACK_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue WIND_CRYSTAL_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue WIND_CRYSTAL_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue WIND_CRYSTAL_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue ZEPHYR_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue ZEPHYR_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue ZEPHYR_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue ZEPHYR_FOCUS_SUMMON_DOWN;
    public static final ForgeConfigSpec.IntValue BABY_ZEPHYR_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue BABY_ZEPHYR_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue BABY_ZEPHYR_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue BABY_ZEPHYR_FOCUS_SUMMON_DOWN;
    public static final ForgeConfigSpec.IntValue SKY_WOLF_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue SKY_WOLF_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue SKY_WOLF_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue SKY_WOLF_FOCUS_SUMMON_DOWN;
    public static final ForgeConfigSpec.IntValue TEMPEST_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue SUPERSTORM_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue SUPERSTORM_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue SUPERSTORM_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue SUPERSTORM_FOCUS_SUMMON_DOWN;
    public static final ForgeConfigSpec.IntValue TEMPEST_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue TEMPEST_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue TEMPEST_FOCUS_SUMMON_DOWN;
    public static final ForgeConfigSpec.IntValue THUNDER_CRYSTAL_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue THUNDER_CRYSTAL_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue THUNDER_CRYSTAL_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue FIRE_CRYSTAL_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue FIRE_CRYSTAL_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue FIRE_CRYSTAL_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue ICE_CRYSTAL_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue ICE_CRYSTAL_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue ICE_CRYSTAL_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue VALKYRIE_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue VALKYRIE_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue VALKYRIE_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue VALKYRIE_FOCUS_SUMMON_DOWN;
    public static final ForgeConfigSpec.IntValue COCKATRICE_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue COCKATRICE_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue COCKATRICE_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue COCKATRICE_FOCUS_SUMMON_DOWN;
    public static final ForgeConfigSpec.IntValue SOLAR_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue SOLAR_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue SOLAR_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue SOLAR_FOCUS_SUMMON_DOWN;
    public static final ForgeConfigSpec.IntValue CLOUD_BREATH_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue CLOUD_BREATH_FOCUS_SHOTS;
    public static final ForgeConfigSpec.IntValue CLOUD_BREATH_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue CLOUD_FALLING_FOCUS_COST;
    public static final ForgeConfigSpec.IntValue CLOUD_FALLING_FOCUS_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue CLOUD_FALLING_FOCUS_COOLDOWN;
    public static final ForgeConfigSpec.IntValue POISON_DART_RAIN_COST;
    public static final ForgeConfigSpec.IntValue POISON_DART_RAIN_CAST_DURATION;
    public static final ForgeConfigSpec.IntValue POISON_DART_RAIN_COOLDOWN;

    public static final ForgeConfigSpec.DoubleValue SENTRY_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue SENTRY_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue SLIDER_HEALTH;
    public static final ForgeConfigSpec.DoubleValue SLIDER_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue VALKYRIE_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue VALKYRIE_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue COCKATRICE_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue COCKATRICE_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue SUN_SPIRIT_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue SUN_SPIRIT_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue AECHOR_PLANT_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue AECHOR_PLANT_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue WHIRLWIND_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue WHIRLWIND_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue CREEPER_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue CREEPER_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue ZOMBIE_VALKYRIE_QUEEN_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue ZOMBIE_VALKYRIE_QUEEN_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue FIRE_MINION_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue FIRE_MINION_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue GOLDEN_SWET_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue GOLDEN_SWET_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue BLUE_SWET_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue BLUE_SWET_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue VANILLA_SWET_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue VANILLA_SWET_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue DARK_SWET_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue DARK_SWET_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue ZEPHYR_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue ZEPHYR_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue BABY_ZEPHYR_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue BABY_ZEPHYR_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue TEMPEST_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue TEMPEST_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue HURRICANE_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue HURRICANE_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue BATTLE_SENTRY_SERVANT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue BATTLE_SENTRY_SERVANT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue TRACKING_GOLEM_HEALTH;
    public static final ForgeConfigSpec.DoubleValue TRACKING_GOLEM_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue SENTRY_GOLEM_HEALTH;
    public static final ForgeConfigSpec.DoubleValue SENTRY_GOLEM_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue SENTRY_GUARDIAN_HEALTH;
    public static final ForgeConfigSpec.DoubleValue SENTRY_GUARDIAN_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue EOTS_CONTROLLER_HEALTH;
    public static final ForgeConfigSpec.DoubleValue EOTS_CONTROLLER_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue EOTS_SERVANT_SEGMENT_HEALTH;
    public static final ForgeConfigSpec.DoubleValue EOTS_SERVANT_SEGMENT_DAMAGE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Focus spell settings (ticks; these are the base values before Goety's casting speed / soul discount modifiers)").push("spells");
        SENTRY_FOCUS_COST = builder.comment("Sentry Focus soul cost").defineInRange("sentryFocusCost", 24, 1, 1000);
        SENTRY_FOCUS_CAST_DURATION = builder.comment("Sentry Focus cast duration").defineInRange("sentryFocusCastDuration", 20, 1, 1200);
        SENTRY_FOCUS_COOLDOWN = builder.comment("Sentry Focus cooldown").defineInRange("sentryFocusCooldown", 240, 0, 72000);
        BATTLE_SENTRY_FOCUS_COST = builder.comment("Battle Sentry Focus soul cost").defineInRange("battleSentryFocusCost", 16, 1, 1000);
        BATTLE_SENTRY_FOCUS_CAST_DURATION = builder.comment("Battle Sentry Focus cast duration").defineInRange("battleSentryFocusCastDuration", 20, 1, 1200);
        BATTLE_SENTRY_FOCUS_COOLDOWN = builder.comment("Battle Sentry Focus cooldown").defineInRange("battleSentryFocusCooldown", 240, 0, 72000);
        TRACKING_GOLEM_FOCUS_COST = builder.comment("Tracking Golem Focus soul cost").defineInRange("trackingGolemFocusCost", 24, 1, 1000);
        TRACKING_GOLEM_FOCUS_CAST_DURATION = builder.comment("Tracking Golem Focus cast duration").defineInRange("trackingGolemFocusCastDuration", 20, 1, 1200);
        TRACKING_GOLEM_FOCUS_COOLDOWN = builder.comment("Tracking Golem Focus cooldown").defineInRange("trackingGolemFocusCooldown", 300, 0, 72000);
        SENTRY_GOLEM_FOCUS_COST = builder.comment("Sentry Golem Focus soul cost").defineInRange("sentryGolemFocusCost", 24, 1, 1000);
        SENTRY_GOLEM_FOCUS_CAST_DURATION = builder.comment("Sentry Golem Focus cast duration").defineInRange("sentryGolemFocusCastDuration", 20, 1, 1200);
        SENTRY_GOLEM_FOCUS_COOLDOWN = builder.comment("Sentry Golem Focus cooldown").defineInRange("sentryGolemFocusCooldown", 300, 0, 72000);
        PICKAXE_ATTACK_FOCUS_COST = builder.comment("Pickaxe Attack Focus soul cost").defineInRange("pickaxeAttackFocusCost", 64, 1, 1000);
        PICKAXE_ATTACK_FOCUS_CAST_DURATION = builder.comment("Pickaxe Attack Focus cast duration").defineInRange("pickaxeAttackFocusCastDuration", 100, 1, 1200);
        PICKAXE_ATTACK_FOCUS_COOLDOWN = builder.comment("Pickaxe Attack Focus cooldown").defineInRange("pickaxeAttackFocusCooldown", 600, 0, 72000);
        WIND_CRYSTAL_FOCUS_COST = builder.comment("Wind Crystal Focus soul cost").defineInRange("windCrystalFocusCost", 12, 1, 1000);
        WIND_CRYSTAL_FOCUS_CAST_DURATION = builder.comment("Wind Crystal Focus cast duration (0 = instant cast)").defineInRange("windCrystalFocusCastDuration", 0, 0, 1200);
        WIND_CRYSTAL_FOCUS_COOLDOWN = builder.comment("Wind Crystal Focus cooldown").defineInRange("windCrystalFocusCooldown", 20, 0, 72000);
        ZEPHYR_FOCUS_COST = builder.comment("Zephyr Focus soul cost").defineInRange("zephyrFocusCost", 8, 1, 1000);
        ZEPHYR_FOCUS_CAST_DURATION = builder.comment("Zephyr Focus cast duration").defineInRange("zephyrFocusCastDuration", 60, 0, 1200);
        ZEPHYR_FOCUS_COOLDOWN = builder.comment("Zephyr Focus cooldown").defineInRange("zephyrFocusCooldown", 200, 0, 72000);
        ZEPHYR_FOCUS_SUMMON_DOWN = builder.comment("Zephyr Focus summon down duration (servants summoned while this effect is active are sapped)").defineInRange("zephyrFocusSummonDown", 100, 0, 72000);
        BABY_ZEPHYR_FOCUS_COST = builder.comment("Baby Zephyr Focus soul cost").defineInRange("babyZephyrFocusCost", 20, 1, 1000);
        BABY_ZEPHYR_FOCUS_CAST_DURATION = builder.comment("Baby Zephyr Focus cast duration").defineInRange("babyZephyrFocusCastDuration", 60, 0, 1200);
        BABY_ZEPHYR_FOCUS_COOLDOWN = builder.comment("Baby Zephyr Focus cooldown").defineInRange("babyZephyrFocusCooldown", 280, 0, 72000);
        BABY_ZEPHYR_FOCUS_SUMMON_DOWN = builder.comment("Baby Zephyr Focus summon down duration (servants summoned while this effect is active are sapped)").defineInRange("babyZephyrFocusSummonDown", 100, 0, 72000);
        SKY_WOLF_FOCUS_COST = builder.comment("Sky Hunter Focus soul cost").defineInRange("skyWolfFocusCost", 10, 1, 1000);
        SKY_WOLF_FOCUS_CAST_DURATION = builder.comment("Sky Hunter Focus cast duration (0 = instant cast)").defineInRange("skyWolfFocusCastDuration", 20, 0, 1200);
        SKY_WOLF_FOCUS_COOLDOWN = builder.comment("Sky Hunter Focus cooldown").defineInRange("skyWolfFocusCooldown", 200, 0, 72000);
        SKY_WOLF_FOCUS_SUMMON_DOWN = builder.comment("Sky Hunter Focus summon down duration (servants summoned while this effect is active are sapped)").defineInRange("skyWolfFocusSummonDown", 100, 0, 72000);
        TEMPEST_FOCUS_COST = builder.comment("Tempest Focus soul cost").defineInRange("tempestFocusCost", 32, 1, 1000);
        TEMPEST_FOCUS_CAST_DURATION = builder.comment("Tempest Focus cast duration").defineInRange("tempestFocusCastDuration", 60, 0, 1200);
        TEMPEST_FOCUS_COOLDOWN = builder.comment("Tempest Focus cooldown").defineInRange("tempestFocusCooldown", 200, 0, 72000);
        TEMPEST_FOCUS_SUMMON_DOWN = builder.comment("Tempest Focus summon down duration (servants summoned while this effect is active are sapped)").defineInRange("tempestFocusSummonDown", 100, 0, 72000);
        SUPERSTORM_FOCUS_COST = builder.comment("Superstorm Focus soul cost").defineInRange("superstormFocusCost", 500, 1, 1000);
        SUPERSTORM_FOCUS_CAST_DURATION = builder.comment("Superstorm Focus cast duration").defineInRange("superstormFocusCastDuration", 400, 0, 1200);
        SUPERSTORM_FOCUS_COOLDOWN = builder.comment("Superstorm Focus cooldown").defineInRange("superstormFocusCooldown", 2400, 0, 72000);
        SUPERSTORM_FOCUS_SUMMON_DOWN = builder.comment("Superstorm Focus summon down duration (servants summoned while this effect is active are sapped)").defineInRange("superstormFocusSummonDown", 400, 0, 72000);
        THUNDER_CRYSTAL_FOCUS_COST = builder.comment("Storm Thunder Focus soul cost").defineInRange("thunderCrystalFocusCost", 14, 1, 1000);
        THUNDER_CRYSTAL_FOCUS_CAST_DURATION = builder.comment("Storm Thunder Focus cast duration (0 = instant cast)").defineInRange("thunderCrystalFocusCastDuration", 0, 0, 1200);
        THUNDER_CRYSTAL_FOCUS_COOLDOWN = builder.comment("Storm Thunder Focus cooldown").defineInRange("thunderCrystalFocusCooldown", 80, 0, 72000);
        FIRE_CRYSTAL_FOCUS_COST = builder.comment("Fire Crystal Focus soul cost").defineInRange("fireCrystalFocusCost", 12, 1, 1000);
        FIRE_CRYSTAL_FOCUS_CAST_DURATION = builder.comment("Fire Crystal Focus cast duration (0 = instant cast)").defineInRange("fireCrystalFocusCastDuration", 0, 0, 1200);
        FIRE_CRYSTAL_FOCUS_COOLDOWN = builder.comment("Fire Crystal Focus cooldown").defineInRange("fireCrystalFocusCooldown", 80, 0, 72000);
        ICE_CRYSTAL_FOCUS_COST = builder.comment("Ice Crystal Focus soul cost").defineInRange("iceCrystalFocusCost", 12, 1, 1000);
        ICE_CRYSTAL_FOCUS_CAST_DURATION = builder.comment("Ice Crystal Focus cast duration (0 = instant cast)").defineInRange("iceCrystalFocusCastDuration", 0, 0, 1200);
        ICE_CRYSTAL_FOCUS_COOLDOWN = builder.comment("Ice Crystal Focus cooldown").defineInRange("iceCrystalFocusCooldown", 80, 0, 72000);
        VALKYRIE_FOCUS_COST = builder.comment("Valkyrie Focus soul cost").defineInRange("valkyrieFocusCost", 48, 1, 1000);
        VALKYRIE_FOCUS_CAST_DURATION = builder.comment("Valkyrie Focus cast duration").defineInRange("valkyrieFocusCastDuration", 60, 0, 1200);
        VALKYRIE_FOCUS_COOLDOWN = builder.comment("Valkyrie Focus cooldown").defineInRange("valkyrieFocusCooldown", 600, 0, 72000);
        VALKYRIE_FOCUS_SUMMON_DOWN = builder.comment("Valkyrie Focus summon down duration (servants summoned while this effect is active are sapped)").defineInRange("valkyrieFocusSummonDown", 200, 0, 72000);
        COCKATRICE_FOCUS_COST = builder.comment("Cockatrice Focus soul cost").defineInRange("cockatriceFocusCost", 24, 1, 1000);
        COCKATRICE_FOCUS_CAST_DURATION = builder.comment("Cockatrice Focus cast duration").defineInRange("cockatriceFocusCastDuration", 120, 0, 1200);
        COCKATRICE_FOCUS_COOLDOWN = builder.comment("Cockatrice Focus cooldown").defineInRange("cockatriceFocusCooldown", 260, 0, 72000);
        COCKATRICE_FOCUS_SUMMON_DOWN = builder.comment("Cockatrice Focus summon down duration (servants summoned while this effect is active are sapped)").defineInRange("cockatriceFocusSummonDown", 80, 0, 72000);
        SOLAR_FOCUS_COST = builder.comment("Solar Focus soul cost").defineInRange("solarFocusCost", 48, 1, 1000);
        SOLAR_FOCUS_CAST_DURATION = builder.comment("Solar Focus cast duration").defineInRange("solarFocusCastDuration", 60, 0, 1200);
        SOLAR_FOCUS_COOLDOWN = builder.comment("Solar Focus cooldown").defineInRange("solarFocusCooldown", 800, 0, 72000);
        CLOUD_BREATH_FOCUS_COST = builder.comment("Cloud Breath Focus soul cost").defineInRange("cloudBreathFocusCost", 3, 1, 1000);
        CLOUD_BREATH_FOCUS_SHOTS = builder.comment("Cloud Breath Focus breathing shots").defineInRange("cloudBreathFocusShots", 100, 0, 3600);
        CLOUD_BREATH_FOCUS_COOLDOWN = builder.comment("Cloud Breath Focus cooldown").defineInRange("cloudBreathFocusCooldown", 100, 0, 72000);
        CLOUD_FALLING_FOCUS_COST = builder.comment("Cloud Falling Focus soul cost").defineInRange("cloudFallingFocusCost", 40, 1, 1000);
        CLOUD_FALLING_FOCUS_CAST_DURATION = builder.comment("Cloud Falling Focus cast duration in ticks (0 = instant cast)").defineInRange("cloudFallingFocusCastDuration", 100, 0, 1200);
        CLOUD_FALLING_FOCUS_COOLDOWN = builder.comment("Cloud Falling Focus cooldown").defineInRange("cloudFallingFocusCooldown", 200, 0, 72000);
        SOLAR_FOCUS_SUMMON_DOWN = builder.comment("Solar Focus summon down duration (servants summoned while this effect is active are sapped)").defineInRange("solarFocusSummonDown", 200, 0, 72000);
        POISON_DART_RAIN_COST = builder.comment("Poison Dart Rain Focus soul cost").defineInRange("poisonDartRainCost", 5, 1, 1000);
        POISON_DART_RAIN_CAST_DURATION = builder.comment("Poison Dart Rain Focus cast up duration").defineInRange("poisonDartRainCastDuration", 20, 1, 1200);
        POISON_DART_RAIN_COOLDOWN = builder.comment("Poison Dart Rain Focus cooldown").defineInRange("poisonDartRainCooldown", 400, 0, 72000);
        builder.pop();

        builder.comment("Servant stats; set to 0 to use the built-in value").push("servants");
        SENTRY_SERVANT_HEALTH = builder.defineInRange("sentryServantHealth", 0.0D, 0.0D, 1024.0D);
        SENTRY_SERVANT_DAMAGE = builder.defineInRange("sentryServantDamage", 0.0D, 0.0D, 1024.0D);
        SLIDER_HEALTH = builder.defineInRange("sliderServantHealth", 0.0D, 0.0D, 1024.0D);
        SLIDER_DAMAGE = builder.defineInRange("sliderServantDamage", 0.0D, 0.0D, 1024.0D);
        VALKYRIE_SERVANT_HEALTH = builder.defineInRange("valkyrieServantHealth", 0.0D, 0.0D, 1024.0D);
        VALKYRIE_SERVANT_DAMAGE = builder.defineInRange("valkyrieServantDamage", 0.0D, 0.0D, 1024.0D);
        COCKATRICE_SERVANT_HEALTH = builder.defineInRange("cockatriceServantHealth", 0.0D, 0.0D, 1024.0D);
        COCKATRICE_SERVANT_DAMAGE = builder.defineInRange("cockatriceServantDamage", 0.0D, 0.0D, 1024.0D);
        SUN_SPIRIT_SERVANT_HEALTH = builder.defineInRange("sunSpiritServantHealth", 0.0D, 0.0D, 1024.0D);
        SUN_SPIRIT_SERVANT_DAMAGE = builder.defineInRange("sunSpiritServantDamage", 0.0D, 0.0D, 1024.0D);
        AECHOR_PLANT_SERVANT_HEALTH = builder.defineInRange("aechorPlantServantHealth", 0.0D, 0.0D, 1024.0D);
        AECHOR_PLANT_SERVANT_DAMAGE = builder.defineInRange("aechorPlantServantDamage", 0.0D, 0.0D, 1024.0D);
        WHIRLWIND_SERVANT_HEALTH = builder.defineInRange("whirlwindServantHealth", 0.0D, 0.0D, 1024.0D);
        WHIRLWIND_SERVANT_DAMAGE = builder.defineInRange("whirlwindServantDamage", 0.0D, 0.0D, 1024.0D);
        CREEPER_SERVANT_HEALTH = builder.defineInRange("creeperServantHealth", 0.0D, 0.0D, 1024.0D);
        CREEPER_SERVANT_DAMAGE = builder.defineInRange("creeperServantDamage", 0.0D, 0.0D, 1024.0D);
        ZOMBIE_VALKYRIE_QUEEN_SERVANT_HEALTH = builder.defineInRange("zombieValkyrieQueenServantHealth", 0.0D, 0.0D, 1024.0D);
        ZOMBIE_VALKYRIE_QUEEN_SERVANT_DAMAGE = builder.defineInRange("zombieValkyrieQueenServantDamage", 0.0D, 0.0D, 1024.0D);
        FIRE_MINION_SERVANT_HEALTH = builder.defineInRange("fireMinionServantHealth", 0.0D, 0.0D, 1024.0D);
        FIRE_MINION_SERVANT_DAMAGE = builder.defineInRange("fireMinionServantDamage", 0.0D, 0.0D, 1024.0D);
        GOLDEN_SWET_SERVANT_HEALTH = builder.defineInRange("goldenSwetServantHealth", 0.0D, 0.0D, 1024.0D);
        GOLDEN_SWET_SERVANT_DAMAGE = builder.defineInRange("goldenSwetServantDamage", 0.0D, 0.0D, 1024.0D);
        BLUE_SWET_SERVANT_HEALTH = builder.defineInRange("blueSwetServantHealth", 0.0D, 0.0D, 1024.0D);
        BLUE_SWET_SERVANT_DAMAGE = builder.defineInRange("blueSwetServantDamage", 0.0D, 0.0D, 1024.0D);
        VANILLA_SWET_SERVANT_HEALTH = builder.defineInRange("vanillaSwetServantHealth", 0.0D, 0.0D, 1024.0D);
        VANILLA_SWET_SERVANT_DAMAGE = builder.defineInRange("vanillaSwetServantDamage", 0.0D, 0.0D, 1024.0D);
        DARK_SWET_SERVANT_HEALTH = builder.defineInRange("darkSwetServantHealth", 0.0D, 0.0D, 1024.0D);
        DARK_SWET_SERVANT_DAMAGE = builder.defineInRange("darkSwetServantDamage", 0.0D, 0.0D, 1024.0D);
        ZEPHYR_SERVANT_HEALTH = builder.defineInRange("zephyrServantHealth", 0.0D, 0.0D, 1024.0D);
        ZEPHYR_SERVANT_DAMAGE = builder.defineInRange("zephyrServantDamage", 0.0D, 0.0D, 1024.0D);
        BABY_ZEPHYR_SERVANT_HEALTH = builder.defineInRange("babyZephyrServantHealth", 0.0D, 0.0D, 1024.0D);
        BABY_ZEPHYR_SERVANT_DAMAGE = builder.defineInRange("babyZephyrServantDamage", 0.0D, 0.0D, 1024.0D);
        TEMPEST_SERVANT_HEALTH = builder.defineInRange("tempestServantHealth", 0.0D, 0.0D, 1024.0D);
        TEMPEST_SERVANT_DAMAGE = builder.defineInRange("tempestServantDamage", 0.0D, 0.0D, 1024.0D);
        HURRICANE_SERVANT_HEALTH = builder.defineInRange("hurricaneServantHealth", 0.0D, 0.0D, 1024.0D);
        HURRICANE_SERVANT_DAMAGE = builder.defineInRange("hurricaneServantDamage", 0.0D, 0.0D, 1024.0D);
        BATTLE_SENTRY_SERVANT_HEALTH = builder.defineInRange("battleSentryServantHealth", 0.0D, 0.0D, 1024.0D);
        BATTLE_SENTRY_SERVANT_DAMAGE = builder.defineInRange("battleSentryServantDamage", 0.0D, 0.0D, 1024.0D);
        TRACKING_GOLEM_HEALTH = builder.defineInRange("trackingGolemServantHealth", 0.0D, 0.0D, 1024.0D);
        TRACKING_GOLEM_DAMAGE = builder.defineInRange("trackingGolemServantDamage", 0.0D, 0.0D, 1024.0D);
        SENTRY_GOLEM_HEALTH = builder.defineInRange("sentryGolemServantHealth", 0.0D, 0.0D, 1024.0D);
        SENTRY_GOLEM_DAMAGE = builder.defineInRange("sentryGolemServantDamage", 0.0D, 0.0D, 1024.0D);
        SENTRY_GUARDIAN_HEALTH = builder.defineInRange("sentryGuardianServantHealth", 0.0D, 0.0D, 1024.0D);
        SENTRY_GUARDIAN_DAMAGE = builder.defineInRange("sentryGuardianServantDamage", 0.0D, 0.0D, 1024.0D);
        EOTS_CONTROLLER_HEALTH = builder.defineInRange("eotsControllerHealth", 0.0D, 0.0D, 1024.0D);
        EOTS_CONTROLLER_DAMAGE = builder.defineInRange("eotsControllerDamage", 0.0D, 0.0D, 1024.0D);
        EOTS_SERVANT_SEGMENT_HEALTH = builder.defineInRange("eotsServantSegmentHealth", 0.0D, 0.0D, 1024.0D);
        EOTS_SERVANT_SEGMENT_DAMAGE = builder.defineInRange("eotsServantSegmentDamage", 0.0D, 0.0D, 1024.0D);
        builder.pop();

        SPEC = builder.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, String path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(new File(path))
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();
        spec.setConfig(configData);
    }

    public static int spellValue(ForgeConfigSpec.IntValue value, int fallback) {
        try {
            return value.get();
        } catch (IllegalStateException | NullPointerException e) {
            return fallback;
        }
    }

    public static AttributeSupplier.Builder servant(ForgeConfigSpec.DoubleValue health, ForgeConfigSpec.DoubleValue damage, AttributeSupplier.Builder builder) {
        try {
            double h = health.get();
            if (h > 0.0D) {
                builder.add(Attributes.MAX_HEALTH, h);
            }
            double d = damage.get();
            if (d > 0.0D) {
                builder.add(Attributes.ATTACK_DAMAGE, d);
            }
        } catch (IllegalStateException | NullPointerException ignored) {
        }
        return builder;
    }

    private record ServantEntry(java.util.function.Supplier<EntityType<?>> type, ForgeConfigSpec.DoubleValue health, ForgeConfigSpec.DoubleValue damage) {
    }

    private static final List<ServantEntry> SERVANTS = List.of(
            new ServantEntry(() -> ModEntityTypes.SENTRY_SERVANT.get(), SENTRY_SERVANT_HEALTH, SENTRY_SERVANT_DAMAGE),
            new ServantEntry(() -> ModEntityTypes.SLIDER.get(), SLIDER_HEALTH, SLIDER_DAMAGE),
            new ServantEntry(() -> ModEntityTypes.VALKYRIE_SERVANT.get(), VALKYRIE_SERVANT_HEALTH, VALKYRIE_SERVANT_DAMAGE),
            new ServantEntry(() -> ModEntityTypes.COCKATRICE_SERVANT.get(), COCKATRICE_SERVANT_HEALTH, COCKATRICE_SERVANT_DAMAGE),
            new ServantEntry(() -> ModEntityTypes.SUN_SPIRIT_SERVANT.get(), SUN_SPIRIT_SERVANT_HEALTH, SUN_SPIRIT_SERVANT_DAMAGE),
            new ServantEntry(() -> ModEntityTypes.AECHOR_PLANT_SERVANT.get(), AECHOR_PLANT_SERVANT_HEALTH, AECHOR_PLANT_SERVANT_DAMAGE),
            new ServantEntry(() -> ModEntityTypes.PASSIVE_WHIRLWIND_SERVANT.get(), WHIRLWIND_SERVANT_HEALTH, WHIRLWIND_SERVANT_DAMAGE),
            new ServantEntry(() -> ModEntityTypes.EVIL_WHIRLWIND_SERVANT.get(), WHIRLWIND_SERVANT_HEALTH, WHIRLWIND_SERVANT_DAMAGE),
            new ServantEntry(() -> ModEntityTypes.CREEPER_SERVANT.get(), CREEPER_SERVANT_HEALTH, CREEPER_SERVANT_DAMAGE),
            new ServantEntry(() -> ModEntityTypes.ZOMBIE_VALKYRIE_QUEEN_SERVANT.get(), ZOMBIE_VALKYRIE_QUEEN_SERVANT_HEALTH, ZOMBIE_VALKYRIE_QUEEN_SERVANT_DAMAGE),
            new ServantEntry(() -> ModEntityTypes.FIRE_MINION_SERVANT.get(), FIRE_MINION_SERVANT_HEALTH, FIRE_MINION_SERVANT_DAMAGE),
            new ServantEntry(() -> ModEntityTypes.GOLDEN_SWET_SERVANT.get(), GOLDEN_SWET_SERVANT_HEALTH, GOLDEN_SWET_SERVANT_DAMAGE),
            new ServantEntry(() -> ModEntityTypes.BLUE_SWET_SERVANT.get(), BLUE_SWET_SERVANT_HEALTH, BLUE_SWET_SERVANT_DAMAGE),
            new ServantEntry(() -> ModEntityTypes.ZEPHYR_SERVANT.get(), ZEPHYR_SERVANT_HEALTH, ZEPHYR_SERVANT_DAMAGE),
            new ServantEntry(() -> GenesisCompatManager.BATTLE_SENTRY_SERVANT.get(), BATTLE_SENTRY_SERVANT_HEALTH, BATTLE_SENTRY_SERVANT_DAMAGE),
            new ServantEntry(() -> GenesisCompatManager.TRACKING_GOLEM.get(), TRACKING_GOLEM_HEALTH, TRACKING_GOLEM_DAMAGE),
            new ServantEntry(() -> GenesisCompatManager.SENTRY_GUARDIAN.get(), SENTRY_GUARDIAN_HEALTH, SENTRY_GUARDIAN_DAMAGE),
            new ServantEntry(() -> GenesisCompatManager.DARK_SWET_SERVANT.get(), DARK_SWET_SERVANT_HEALTH, DARK_SWET_SERVANT_DAMAGE),
            new ServantEntry(() -> GenesisCompatManager.SENTRY_GOLEM.get(), SENTRY_GOLEM_HEALTH, SENTRY_GOLEM_DAMAGE),
            new ServantEntry(() -> GenesisCompatManager.TEMPEST_SERVANT.get(), TEMPEST_SERVANT_HEALTH, TEMPEST_SERVANT_DAMAGE),
            new ServantEntry(() -> ReduxCompatManager.VANILLA_SWET_SERVANT.get(), VANILLA_SWET_SERVANT_HEALTH, VANILLA_SWET_SERVANT_DAMAGE),
            new ServantEntry(() -> DeepAetherCompatManager.BABY_ZEPHYR_SERVANT.get(), BABY_ZEPHYR_SERVANT_HEALTH, BABY_ZEPHYR_SERVANT_DAMAGE),
            new ServantEntry(() -> DeepAetherCompatManager.EOTSS_CONTROLLER.get(), EOTS_CONTROLLER_HEALTH, EOTS_CONTROLLER_DAMAGE),
            new ServantEntry(() -> DeepAetherCompatManager.EOTSSERVANT_SEGMENT.get(), EOTS_SERVANT_SEGMENT_HEALTH, EOTS_SERVANT_SEGMENT_DAMAGE)
    );

    public static void applyTo(LivingEntity living, ForgeConfigSpec.DoubleValue health, ForgeConfigSpec.DoubleValue damage) {
        try {
            double h = health.get();
            if (h > 0.0D) {
                AttributeInstance attr = living.getAttribute(Attributes.MAX_HEALTH);
                if (attr != null && attr.getBaseValue() != h) {
                    float ratio = living.getHealth() / (float) attr.getValue();
                    attr.setBaseValue(h);
                    living.setHealth(Math.min((float) h, Math.max(ratio * (float) h, 1.0F)));
                }
            }
            double d = damage.get();
            if (d > 0.0D) {
                AttributeInstance attr = living.getAttribute(Attributes.ATTACK_DAMAGE);
                if (attr != null && attr.getBaseValue() != d) {
                    attr.setBaseValue(d);
                }
            }
        } catch (IllegalStateException | NullPointerException ignored) {
        }
    }

    public static void matchAndApply(Entity entity) {
        if (!(entity instanceof LivingEntity living) || entity.level().isClientSide()) {
            return;
        }
        for (ServantEntry entry : SERVANTS) {
            EntityType<?> type;
            try {
                type = entry.type().get();
            } catch (IllegalStateException | NullPointerException e) {
                continue;
            }
            if (type != null && type == living.getType()) {
                applyTo(living, entry.health(), entry.damage());
                return;
            }
        }
    }

    @Mod.EventBusSubscriber(modid = GoetyAether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void onReload(ModConfigEvent.Reloading event) {
            if (event.getConfig().getSpec() != SPEC) {
                return;
            }
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                for (ServerLevel level : server.getAllLevels()) {
                    for (Entity entity : level.getAllEntities()) {
                        matchAndApply(entity);
                    }
                }
            }
        }
    }
}
