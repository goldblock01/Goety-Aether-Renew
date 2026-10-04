package github.goldblock.goety_aether.compat.mod;

import net.minecraftforge.fml.ModList;

public class AetherReduxCompat {
    public static final String MOD_ID = "aether_redux";

    public static boolean isReduxLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }
}
