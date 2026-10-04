package github.goldblock.goety_aether.compat.mod;

import net.minecraftforge.fml.ModList;

public class LegendaryMonstersCompat {
    public static final String MOD_ID = "legendary_monsters";

    public static boolean isLegendaryMonstersLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }
}
