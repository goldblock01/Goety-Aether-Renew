package github.goldblock.goety_aether.compat.deep_aether;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class DeepAetherBridge {
    public static final String MOD_ID = "deep_aether";

    public static SoundEvent sound(String path) {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(MOD_ID, path));
    }

    public static ParticleType<?> particle(String path) {
        return ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(MOD_ID, path));
    }
}
