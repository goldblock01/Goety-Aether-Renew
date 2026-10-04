package github.goldblock.goety_aether.common.init;

import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.effects.PickaxeAttackEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, GoetyAether.MOD_ID);

    public static final RegistryObject<MobEffect> PICKAXE_ATTACK = EFFECTS.register("pickaxe_attack", PickaxeAttackEffect::new);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
