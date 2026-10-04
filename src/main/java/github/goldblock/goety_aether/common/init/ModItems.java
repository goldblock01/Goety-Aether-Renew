package github.goldblock.goety_aether.common.init;

import com.Polarice3.Goety.common.items.ServantSpawnEggItem;
import com.Polarice3.Goety.common.items.magic.MagicFocus;
import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.items.divine.DivineCrownItem;
import github.goldblock.goety_aether.common.items.revive.ArkenzusCodex;
import github.goldblock.goety_aether.common.items.revive.ReviveServantItem;
import github.goldblock.goety_aether.common.items.revive.SwollenSun;
import github.goldblock.goety_aether.common.items.magic.DivineStaff;
import github.goldblock.goety_aether.common.items.divine.DivineRobeItem;
import github.goldblock.goety_aether.common.magic.spells.BattleSentryFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.FireCrystalFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.IceCrystalFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.PickaxeAttackSpell;
import github.goldblock.goety_aether.common.magic.spells.PoisonDartRainSpell;
import github.goldblock.goety_aether.common.magic.spells.SentryFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.SentryGolemFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.SkyWolfFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.SolarFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.ThunderCrystalSpell;
import github.goldblock.goety_aether.common.magic.spells.TrackingGolemFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.CockatriceFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.ValkyrieFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.WindCrystalSpell;
import github.goldblock.goety_aether.common.magic.spells.ZephyrFocusSpell;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GoetyAether.MOD_ID);

    public static final RegistryObject<MagicFocus> SENTRY_FOCUS = ITEMS.register("sentry_focus",
            () -> new MagicFocus(new SentryFocusSpell()));

    public static final RegistryObject<MagicFocus> BATTLE_SENTRY_FOCUS = ITEMS.register("battle_sentry_focus",
            () -> new MagicFocus(new BattleSentryFocusSpell()));

    public static final RegistryObject<MagicFocus> TRACKING_GOLEM_FOCUS = ITEMS.register("tracking_golem_focus",
            () -> new MagicFocus(new TrackingGolemFocusSpell()));

    public static final RegistryObject<MagicFocus> SENTRY_GOLEM_FOCUS = ITEMS.register("sentry_golem_focus",
            () -> new MagicFocus(new SentryGolemFocusSpell()));

    public static final RegistryObject<MagicFocus> PICKAXE_ATTACK_FOCUS = ITEMS.register("pickaxe_attack_focus",
            () -> new MagicFocus(new PickaxeAttackSpell()));
    public static final RegistryObject<MagicFocus> THUNDER_CRYSTAL_FOCUS = ITEMS.register("thunder_crystal_focus",
            () -> new MagicFocus(new ThunderCrystalSpell()));
    public static final RegistryObject<MagicFocus> FIRE_CRYSTAL_FOCUS = ITEMS.register("fire_crystal_focus",
            () -> new MagicFocus(new FireCrystalFocusSpell()));
    public static final RegistryObject<MagicFocus> ICE_CRYSTAL_FOCUS = ITEMS.register("ice_crystal_focus",
            () -> new MagicFocus(new IceCrystalFocusSpell()));

    public static final RegistryObject<MagicFocus> ZEPHYR_FOCUS = ITEMS.register("zephyr_focus",
            () -> new MagicFocus(new ZephyrFocusSpell()));

    public static final RegistryObject<MagicFocus> SKY_WOLF_FOCUS = ITEMS.register("sky_hunter_focus",
            () -> new MagicFocus(new SkyWolfFocusSpell()));

    public static final RegistryObject<MagicFocus> VALKYRIE_FOCUS = ITEMS.register("valkyrie_focus",
            () -> new MagicFocus(new ValkyrieFocusSpell()));

    public static final RegistryObject<MagicFocus> COCKATRICE_FOCUS = ITEMS.register("cockatrice_focus",
            () -> new MagicFocus(new CockatriceFocusSpell()));

    public static final RegistryObject<MagicFocus> POISON_DART_RAIN_FOCUS = ITEMS.register("poison_dart_rain_focus",
            () -> new MagicFocus(new PoisonDartRainSpell()));

    public static final RegistryObject<ServantSpawnEggItem> COCKATRICE_SERVANT_SPAWN_EGG = ITEMS.register("cockatrice_servant_spawn_egg",
            () -> new ServantSpawnEggItem(ModEntityTypes.COCKATRICE_SERVANT, 7123292, 7100317, new Item.Properties()));

    public static final RegistryObject<ServantSpawnEggItem> SUN_SPIRIT_SERVANT_SPAWN_EGG = ITEMS.register("sun_spirit_servant_spawn_egg",
            () -> new ServantSpawnEggItem(ModEntityTypes.SUN_SPIRIT_SERVANT, 16708864, 16739585, new Item.Properties()));

    public static final RegistryObject<ReviveServantItem> ARKENZUS_CODEX = ITEMS.register("arkenzus_codex",
            () -> new ArkenzusCodex());

    public static final RegistryObject<ReviveServantItem> SWOLLEN_SUN = ITEMS.register("swollen_sun",
            () -> new SwollenSun());

    public static final RegistryObject<DivineCrownItem> DIVINE_CROWN = ITEMS.register("divine_crown",
            () -> new DivineCrownItem());

    public static final RegistryObject<DivineRobeItem> DIVINE_ROBE = ITEMS.register("divine_robe",
            () -> new DivineRobeItem());

    public static final RegistryObject<DivineStaff> DIVINE_STAFF = ITEMS.register("divine_staff",
            () -> new DivineStaff(7.0D));

    public static final RegistryObject<MagicFocus> SOLAR_FOCUS = ITEMS.register("solar_focus",
            () -> new MagicFocus(new SolarFocusSpell()));

    public static final RegistryObject<ServantSpawnEggItem> SENTRY_SERVANT_SPAWN_EGG = ITEMS.register("sentry_servant_spawn_egg",
            () -> new ServantSpawnEggItem(ModEntityTypes.SENTRY_SERVANT, 0x808080, 0x3A8AEC, new Item.Properties()));

    public static final RegistryObject<ServantSpawnEggItem> SLIDER_SPAWN_EGG = ITEMS.register("slider_spawn_egg",
            () -> new ServantSpawnEggItem(ModEntityTypes.SLIDER, 0xA7A7A7, 0x5C9FF2, new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> WIND_CALLER_SPAWN_EGG = ITEMS.register("wind_caller_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.WIND_CALLER, 0x8D837D, 0xE8E6E1, new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> MOUNTAINEER_SPAWN_EGG = ITEMS.register("mountaineer_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.MOUNTAINEER, 0x5A6E7F, 0xD8C7A8, new Item.Properties()));

    public static final RegistryObject<ServantSpawnEggItem> SKY_WOLF_SERVANT_SPAWN_EGG = ITEMS.register("sky_wolf_servant_spawn_egg",
            () -> new ServantSpawnEggItem(ModEntityTypes.SKY_WOLF_SERVANT, 1053254, 1977648, new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> SKY_WOLF_SPAWN_EGG = ITEMS.register("sky_wolf_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.SKY_WOLF, 1977648, 1053254, new Item.Properties()));

    public static final RegistryObject<ServantSpawnEggItem> GOLDEN_SWET_SERVANT_SPAWN_EGG = ITEMS.register("golden_swet_servant_spawn_egg",
            () -> new ServantSpawnEggItem(ModEntityTypes.GOLDEN_SWET_SERVANT, 13490767, 5222874, new Item.Properties()));

    public static final RegistryObject<ServantSpawnEggItem> BLUE_SWET_SERVANT_SPAWN_EGG = ITEMS.register("blue_swet_servant_spawn_egg",
            () -> new ServantSpawnEggItem(ModEntityTypes.BLUE_SWET_SERVANT, 5222874, 13490767, new Item.Properties()));

    public static final RegistryObject<ServantSpawnEggItem> ZEPHYR_SERVANT_SPAWN_EGG = ITEMS.register("zephyr_servant_spawn_egg",
            () -> new ServantSpawnEggItem(ModEntityTypes.ZEPHYR_SERVANT, 14671839, 10080232, new Item.Properties()));

    public static final RegistryObject<ServantSpawnEggItem> AECHOR_PLANT_SERVANT_SPAWN_EGG = ITEMS.register("aechor_plant_servant_spawn_egg",
            () -> new ServantSpawnEggItem(ModEntityTypes.AECHOR_PLANT_SERVANT, 0x4C8B2B, 0x2E1A4E, new Item.Properties()));

    public static final RegistryObject<ServantSpawnEggItem> PASSIVE_WHIRLWIND_SERVANT_SPAWN_EGG = ITEMS.register("passive_whirlwind_servant_spawn_egg",
            () -> new ServantSpawnEggItem(ModEntityTypes.PASSIVE_WHIRLWIND_SERVANT, 0xFFFFFF, 0xC8D8E8, new Item.Properties()));

    public static final RegistryObject<ServantSpawnEggItem> EVIL_WHIRLWIND_SERVANT_SPAWN_EGG = ITEMS.register("evil_whirlwind_servant_spawn_egg",
            () -> new ServantSpawnEggItem(ModEntityTypes.EVIL_WHIRLWIND_SERVANT, 0x2B2B3C, 0x8B2BE2, new Item.Properties()));

    public static final RegistryObject<ServantSpawnEggItem> CREEPER_SERVANT_SPAWN_EGG = ITEMS.register("creeper_servant_spawn_egg",
            () -> new ServantSpawnEggItem(ModEntityTypes.CREEPER_SERVANT, 0x0DA70B, 0x1E2B1A, new Item.Properties()));

    public static RegistryObject<ServantSpawnEggItem> VALKYRIE_SERVANT_SPAWN_EGG;
    public static RegistryObject<ServantSpawnEggItem> ZOMBIE_VALKYRIE_QUEEN_SERVANT_SPAWN_EGG;
    public static RegistryObject<ServantSpawnEggItem> FIRE_MINION_SERVANT_SPAWN_EGG;

    public static void registerValkyrieEgg() {
        VALKYRIE_SERVANT_SPAWN_EGG = ITEMS.register("valkyrie_servant_spawn_egg",
                () -> new ServantSpawnEggItem(ModEntityTypes.VALKYRIE_SERVANT, 0xF9F5E3, 0xF2D200, new Item.Properties()));
        ZOMBIE_VALKYRIE_QUEEN_SERVANT_SPAWN_EGG = ITEMS.register("zombie_valkyrie_queen_spawn_egg",
                () -> new ServantSpawnEggItem(ModEntityTypes.ZOMBIE_VALKYRIE_QUEEN_SERVANT, 0x4C5F44, 0x273327, new Item.Properties()));
    }

    public static void registerFireMinionEgg() {
        FIRE_MINION_SERVANT_SPAWN_EGG = ITEMS.register("fire_minion_servant_spawn_egg",
                () -> new ServantSpawnEggItem(ModEntityTypes.FIRE_MINION_SERVANT, 0xFF6D01, 0xFEF500, new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
