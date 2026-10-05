package github.goldblock.goety_aether.compat.deep_aether.client;

import github.goldblock.goety_aether.client.render.BabyZephyrServantRenderer;
import github.goldblock.goety_aether.client.render.EOTSSControllerRenderer;
import github.goldblock.goety_aether.client.render.EOTSServantSegmentRenderer;
import github.goldblock.goety_aether.client.render.GoetyAetherModelLayers;
import github.goldblock.goety_aether.client.render.WindBallRenderer;
import github.goldblock.goety_aether.common.entities.projectile.WindCrystal;
import github.goldblock.goety_aether.compat.deep_aether.DeepAetherCompatManager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class DeepAetherCompatClient {
    @SuppressWarnings("unchecked")
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(DeepAetherCompatManager.BABY_ZEPHYR_SERVANT.get(), BabyZephyrServantRenderer::new);
        event.registerEntityRenderer(DeepAetherCompatManager.EOTSS_CONTROLLER.get(), EOTSSControllerRenderer::new);
        event.registerEntityRenderer(DeepAetherCompatManager.EOTSSERVANT_SEGMENT.get(), EOTSServantSegmentRenderer::new);
        event.registerEntityRenderer(DeepAetherCompatManager.VENOMITE_SERVANT.get(), VenomiteServantRenderer::new);
        event.registerEntityRenderer(DeepAetherCompatManager.WIND_CRYSTAL.get(),
                (EntityRendererProvider<WindCrystal>) (EntityRendererProvider) WindBallRenderer::new);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GoetyAetherModelLayers.VENOMITE_SERVANT, VenomiteServantModel::createBodyLayer);
    }
}
