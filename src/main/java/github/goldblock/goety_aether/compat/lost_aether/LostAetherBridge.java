package github.goldblock.goety_aether.compat.lost_aether;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class LostAetherBridge {
    public static final String MOD_ID = "lost_aether_content";

    public static SoundEvent sound(String path) {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(MOD_ID, path));
    }
}
