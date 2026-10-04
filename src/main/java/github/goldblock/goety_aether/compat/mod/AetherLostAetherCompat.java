package github.goldblock.goety_aether.compat.mod;

import net.minecraftforge.fml.ModList;

public class AetherLostAetherCompat {
    public static final String MOD_ID = "lost_aether_content";

    public static boolean isLostAetherLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }
}
