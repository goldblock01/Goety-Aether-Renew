package github.goldblock.goety_aether.compat.redux;

import com.Polarice3.Goety.common.items.ServantSpawnEggItem;
import com.aetherteam.aether.entity.monster.Swet;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import github.goldblock.goety_aether.common.entities.ally.mobs.VanillaSwetServant;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import github.goldblock.goety_aether.common.init.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.RegistryObject;

public class ReduxCompatManager {
    public static RegistryObject<EntityType<VanillaSwetServant>> VANILLA_SWET_SERVANT;
    public static RegistryObject<ServantSpawnEggItem> VANILLA_SWET_SERVANT_SPAWN_EGG;

    public static void init() {
        VANILLA_SWET_SERVANT = ModEntityTypes.ENTITY_TYPES.register("vanilla_swet_servant",
                () -> EntityType.Builder.of(VanillaSwetServant::new, MobCategory.MONSTER).sized(0.9F, 0.9F).clientTrackingRange(10).build("vanilla_swet_servant"));
        VANILLA_SWET_SERVANT_SPAWN_EGG = ModItems.ITEMS.register("vanilla_swet_servant_spawn_egg",
                () -> new ServantSpawnEggItem(VANILLA_SWET_SERVANT, 16249827, 13879995, new Item.Properties()));
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(VANILLA_SWET_SERVANT.get(), GoetyAetherConfig.servant(GoetyAetherConfig.VANILLA_SWET_SERVANT_HEALTH, GoetyAetherConfig.VANILLA_SWET_SERVANT_DAMAGE, Swet.createMobAttributes()).build());
    }
}
