package github.goldblock.goety_aether.compat.lost_aether.client;

import github.goldblock.goety_aether.compat.lost_aether.LostAetherCompatManager;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class LostAetherCompatClient {
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(LostAetherCompatManager.HARM_CLOUD.get(),
                net.minecraft.client.renderer.entity.NoopRenderer::new);
    }
}
