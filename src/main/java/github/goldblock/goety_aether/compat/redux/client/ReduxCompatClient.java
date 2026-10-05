package github.goldblock.goety_aether.compat.redux.client;

import github.goldblock.goety_aether.client.render.VanillaSwetServantRenderer;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import github.goldblock.goety_aether.compat.redux.ReduxCompatManager;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;

public class ReduxCompatClient {
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ReduxCompatManager.VANILLA_SWET_SERVANT.get(), VanillaSwetServantRenderer::new);
        event.registerEntityRenderer(ReduxCompatManager.BLIGHTBUNNY_SERVANT.get(), BlightbunnyServantRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.PASSIVE_WHIRLWIND_SERVANT.get(), ReduxWhirlwindServantRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.EVIL_WHIRLWIND_SERVANT.get(), ReduxWhirlwindServantRenderer::new);
    }

    public static void registerEvents() {
        MinecraftForge.EVENT_BUS.register(ReduxWhirlwindRenderListener.class);
    }
}
