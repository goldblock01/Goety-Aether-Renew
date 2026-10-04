package github.goldblock.goety_aether.compat.legendary_monsters.client;

import github.goldblock.goety_aether.client.render.CloudGolemServantRenderer;
import github.goldblock.goety_aether.client.render.GoetyAetherModelLayers;
import github.goldblock.goety_aether.client.render.HoveringHurricaneServantModel;
import github.goldblock.goety_aether.client.render.HoveringHurricaneServantRenderer;
import github.goldblock.goety_aether.compat.legendary_monsters.LegendaryMonstersCompatManager;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class LegendaryMonstersCompatClient {
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(LegendaryMonstersCompatManager.HURRICANE_SERVANT.get(), HoveringHurricaneServantRenderer::new);
        event.registerEntityRenderer(LegendaryMonstersCompatManager.CLOUD_GOLEM.get(), CloudGolemServantRenderer::new);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GoetyAetherModelLayers.HURRICANE_SERVANT, HoveringHurricaneServantModel::createBodyLayer);
    }
}
