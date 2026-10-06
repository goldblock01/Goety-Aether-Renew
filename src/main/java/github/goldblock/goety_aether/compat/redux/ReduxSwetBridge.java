package github.goldblock.goety_aether.compat.redux;

import net.minecraftforge.common.capabilities.Capability;
import net.zepalesque.redux.capability.ReduxCapabilities;

public class ReduxSwetBridge {

    public static boolean isSwetMass(Capability<?> cap) {
        return cap == ReduxCapabilities.SWET_MASS;
    }
}
