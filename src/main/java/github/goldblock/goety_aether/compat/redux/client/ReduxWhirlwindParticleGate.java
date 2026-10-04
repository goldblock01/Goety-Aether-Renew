package github.goldblock.goety_aether.compat.redux.client;

import github.goldblock.goety_aether.compat.mod.AetherReduxCompat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class ReduxWhirlwindParticleGate {
    public static boolean hideParticles() {
        if (!AetherReduxCompat.isReduxLoaded()) {
            return false;
        }
        Boolean result = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> ReduxWhirlwindModelState.isModelEnabled());
        return result != null && result;
    }
}
