package github.goldblock.goety_aether.compat.lost_aether;

import com.Polarice3.Goety.common.items.magic.MagicFocus;
import github.goldblock.goety_aether.common.entities.util.HarmCloud;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import github.goldblock.goety_aether.common.init.ModItems;
import github.goldblock.goety_aether.common.magic.spells.CloudBreathSpell;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class LostAetherCompatManager {
    public static RegistryObject<EntityType<HarmCloud>> HARM_CLOUD;
    public static RegistryObject<Item> CLOUD_BREATH_FOCUS;

    public static void init() {
        HARM_CLOUD = ModEntityTypes.ENTITY_TYPES.register("harm_cloud",
                () -> EntityType.Builder.<HarmCloud>of(HarmCloud::new, MobCategory.MISC).sized(6.0F, 0.5F).fireImmune().clientTrackingRange(10).build("harm_cloud"));
        CLOUD_BREATH_FOCUS = ModItems.ITEMS.register("cloud_breath_focus",
                () -> new MagicFocus(new CloudBreathSpell()));
    }
}
