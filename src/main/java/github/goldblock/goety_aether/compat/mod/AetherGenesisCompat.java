package github.goldblock.goety_aether.compat.mod;

import net.minecraftforge.fml.ModList;

public class AetherGenesisCompat {
    public static final String MOD_ID = "aether_genesis";

    public static boolean isGenesisLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }
}
