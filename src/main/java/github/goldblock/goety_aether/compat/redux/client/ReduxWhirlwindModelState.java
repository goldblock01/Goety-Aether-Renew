package github.goldblock.goety_aether.compat.redux.client;

import net.zepalesque.redux.config.ReduxConfig;

public class ReduxWhirlwindModelState {
    public static boolean isModelEnabled() {
        return ReduxConfig.CLIENT.improved_whirlwinds.get();
    }
}
