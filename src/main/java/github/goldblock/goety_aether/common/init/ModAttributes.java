package github.goldblock.goety_aether.common.init;

import com.Polarice3.Goety.common.entities.ai.attributes.SpellAttribute;
import github.goldblock.goety_aether.GoetyAether;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Field;

public class ModAttributes {
    public static final RegistryObject<Attribute> DIVINE_POTENCY = goetyAttributes().register("divine_potency", () -> SpellAttribute.potency(GoetyAether.DIVINE, 0.0D, 0.0D, 2048.0D).setSyncable(true));

    @SuppressWarnings("unchecked")
    private static DeferredRegister<Attribute> goetyAttributes() {
        try {
            Field field = com.Polarice3.Goety.init.ModAttributes.class.getDeclaredField("ATTRIBUTES");
            field.setAccessible(true);
            return (DeferredRegister<Attribute>) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to access Goety ModAttributes.ATTRIBUTES", e);
        }
    }

    public static void init() {
    }
}
