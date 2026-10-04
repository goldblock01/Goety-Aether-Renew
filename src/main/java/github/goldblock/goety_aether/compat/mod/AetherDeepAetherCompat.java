package github.goldblock.goety_aether.compat.mod;

import net.minecraftforge.fml.ModList;

public class AetherDeepAetherCompat {
    public static final String MOD_ID = "deep_aether";

    public static boolean isDeepAetherLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }
}
