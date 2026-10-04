package github.goldblock.goety_aether.compat.legendary_monsters;

import com.Polarice3.Goety.common.items.ServantSpawnEggItem;
import com.Polarice3.Goety.common.items.magic.MagicFocus;
import github.goldblock.goety_aether.common.entities.ally.mobs.CloudGolemServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.HoveringHurricaneServant;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import github.goldblock.goety_aether.common.init.ModItems;
import github.goldblock.goety_aether.common.magic.spells.CloudFallingSpell;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.RegistryObject;

public class LegendaryMonstersCompatManager {
    public static RegistryObject<EntityType<HoveringHurricaneServant>> HURRICANE_SERVANT;
    public static RegistryObject<ServantSpawnEggItem> HURRICANE_SERVANT_SPAWN_EGG;
    public static RegistryObject<EntityType<CloudGolemServant>> CLOUD_GOLEM;
    public static RegistryObject<ServantSpawnEggItem> CLOUD_GOLEM_SPAWN_EGG;
    public static RegistryObject<Item> CLOUD_FALLING_FOCUS;

    public static void init() {
        HURRICANE_SERVANT = ModEntityTypes.ENTITY_TYPES.register("hovering_hurricane_servant",
                () -> EntityType.Builder.of(HoveringHurricaneServant::new, MobCategory.MONSTER).sized(1.0F, 2.0F).clientTrackingRange(10).build("hovering_hurricane_servant"));
        HURRICANE_SERVANT_SPAWN_EGG = ModItems.ITEMS.register("hovering_hurricane_servant_spawn_egg",
                () -> new ServantSpawnEggItem(HURRICANE_SERVANT, -6035969, -11635812, new Item.Properties()));
        CLOUD_GOLEM = ModEntityTypes.ENTITY_TYPES.register("cloud_golem",
                () -> EntityType.Builder.of(CloudGolemServant::new, MobCategory.MONSTER).sized(1.5F, 2.5F).clientTrackingRange(10).build("cloud_golem"));
        CLOUD_GOLEM_SPAWN_EGG = ModItems.ITEMS.register("cloud_golem_spawn_egg",
                () -> new ServantSpawnEggItem(CLOUD_GOLEM, -1, -3342337, new Item.Properties()));
        CLOUD_FALLING_FOCUS = ModItems.ITEMS.register("cloud_falling_focus",
                () -> new MagicFocus(new CloudFallingSpell()));
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(HURRICANE_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.HURRICANE_SERVANT_HEALTH, GoetyAetherConfig.HURRICANE_SERVANT_DAMAGE, HoveringHurricaneServant.createAttributes()).build());
        event.put(CLOUD_GOLEM.get(), CloudGolemServant.createAttributes().build());
    }
}
