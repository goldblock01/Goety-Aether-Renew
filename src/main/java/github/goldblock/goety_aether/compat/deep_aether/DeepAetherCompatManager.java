package github.goldblock.goety_aether.compat.deep_aether;

import com.Polarice3.Goety.common.items.ServantSpawnEggItem;
import com.Polarice3.Goety.common.items.magic.MagicFocus;
import github.goldblock.goety_aether.common.entities.ally.mobs.BabyZephyrServant;
import github.goldblock.goety_aether.common.magic.spells.BabyZephyrFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.SuperstormFocusSpell;
import github.goldblock.goety_aether.common.magic.spells.WindCrystalSpell;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import github.goldblock.goety_aether.common.entities.ally.mobs.EOTSSController;
import github.goldblock.goety_aether.common.entities.ally.mobs.EOTSServantSegment;
import github.goldblock.goety_aether.common.entities.projectile.WindCrystal;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import github.goldblock.goety_aether.common.init.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.RegistryObject;

public class DeepAetherCompatManager {
    public static RegistryObject<EntityType<BabyZephyrServant>> BABY_ZEPHYR_SERVANT;
    public static RegistryObject<ServantSpawnEggItem> BABY_ZEPHYR_SERVANT_SPAWN_EGG;
    public static RegistryObject<EntityType<EOTSSController>> EOTSS_CONTROLLER;
    public static RegistryObject<EntityType<EOTSServantSegment>> EOTSSERVANT_SEGMENT;
    public static RegistryObject<ServantSpawnEggItem> EOTSSERVANT_SPAWN_EGG;
    public static RegistryObject<EntityType<WindCrystal>> WIND_CRYSTAL;
    public static RegistryObject<Item> WIND_CRYSTAL_FOCUS;
    public static RegistryObject<Item> BABY_ZEPHYR_FOCUS;
    public static RegistryObject<Item> SUPERSTORM_FOCUS;

    public static void init() {
        BABY_ZEPHYR_SERVANT = ModEntityTypes.ENTITY_TYPES.register("baby_zephyr_servant",
                () -> EntityType.Builder.of(BabyZephyrServant::new, MobCategory.MONSTER).sized(1.5F, 1.0F).clientTrackingRange(10).build("baby_zephyr_servant"));
        BABY_ZEPHYR_SERVANT_SPAWN_EGG = ModItems.ITEMS.register("baby_zephyr_servant_spawn_egg",
                () -> new ServantSpawnEggItem(BABY_ZEPHYR_SERVANT, 0xDCE8F5, 0x8FA8C8, new Item.Properties()));
        EOTSS_CONTROLLER = ModEntityTypes.ENTITY_TYPES.register("eotss_controller",
                () -> EntityType.Builder.of(EOTSSController::new, MobCategory.MISC).sized(3.0F, 3.0F).clientTrackingRange(10).build("eotss_controller"));
        EOTSSERVANT_SEGMENT = ModEntityTypes.ENTITY_TYPES.register("eotsservant_segment",
                () -> EntityType.Builder.of((EntityType<EOTSServantSegment> type, net.minecraft.world.level.Level level) -> new EOTSServantSegment(type, level), MobCategory.MONSTER).sized(1.0F, 1.0F).clientTrackingRange(10).build("eotsservant_segment"));
        EOTSSERVANT_SPAWN_EGG = ModItems.ITEMS.register("eotsservant_spawn_egg",
                () -> new ServantSpawnEggItem(EOTSS_CONTROLLER, 0xBADEE6, 0xEBF9FC, new Item.Properties()));
        WIND_CRYSTAL = ModEntityTypes.ENTITY_TYPES.register("wind_crystal",
                () -> EntityType.Builder.of((EntityType<WindCrystal> type, net.minecraft.world.level.Level level) -> new WindCrystal(type, level), MobCategory.MISC).sized(0.85F, 0.85F).clientTrackingRange(4).updateInterval(10).fireImmune().build("wind_crystal"));
        WIND_CRYSTAL_FOCUS = ModItems.ITEMS.register("wind_crystal_focus",
                () -> new MagicFocus(new WindCrystalSpell()));
        BABY_ZEPHYR_FOCUS = ModItems.ITEMS.register("baby_zephyr_focus",
                () -> new MagicFocus(new BabyZephyrFocusSpell()));
        SUPERSTORM_FOCUS = ModItems.ITEMS.register("superstorm_focus",
                () -> new MagicFocus(new SuperstormFocusSpell()));
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(BABY_ZEPHYR_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.BABY_ZEPHYR_SERVANT_HEALTH, GoetyAetherConfig.BABY_ZEPHYR_SERVANT_DAMAGE, BabyZephyrServant.createAttributes()).build());
        event.put(EOTSS_CONTROLLER.get(), GoetyAetherConfig.servant(GoetyAetherConfig.EOTS_CONTROLLER_HEALTH, GoetyAetherConfig.EOTS_CONTROLLER_DAMAGE, EOTSSController.createAttributes()).build());
        event.put(EOTSSERVANT_SEGMENT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.EOTS_SERVANT_SEGMENT_HEALTH, GoetyAetherConfig.EOTS_SERVANT_SEGMENT_DAMAGE, EOTSServantSegment.createAttributes()).build());
    }
}
