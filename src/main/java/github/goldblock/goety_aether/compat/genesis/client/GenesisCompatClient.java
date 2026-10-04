package github.goldblock.goety_aether.compat.genesis.client;

import github.goldblock.goety_aether.client.render.BattleSentryServantRenderer;
import github.goldblock.goety_aether.client.render.DarkSwetServantRenderer;
import github.goldblock.goety_aether.client.render.GoetyAetherModelLayers;
import github.goldblock.goety_aether.client.render.SentryGolemModel;
import github.goldblock.goety_aether.client.render.SentryGolemRenderer;
import github.goldblock.goety_aether.client.render.SentryGuardianModel;
import github.goldblock.goety_aether.client.render.SentryGuardianRenderer;
import github.goldblock.goety_aether.client.render.ThunderBallRenderer;
import github.goldblock.goety_aether.client.render.TrackingGolemModel;
import github.goldblock.goety_aether.client.render.TrackingGolemRenderer;
import github.goldblock.goety_aether.client.render.TempestServantModel;
import github.goldblock.goety_aether.client.render.TempestServantRenderer;
import github.goldblock.goety_aether.compat.genesis.GenesisCompatManager;
import net.minecraft.client.model.SlimeModel;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class GenesisCompatClient {
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GenesisCompatManager.BATTLE_SENTRY_SERVANT.get(), BattleSentryServantRenderer::new);
        event.registerEntityRenderer(GenesisCompatManager.TRACKING_GOLEM.get(), TrackingGolemRenderer::new);
        event.registerEntityRenderer(GenesisCompatManager.SENTRY_GUARDIAN.get(), SentryGuardianRenderer::new);
        event.registerEntityRenderer(GenesisCompatManager.DARK_SWET_SERVANT.get(), DarkSwetServantRenderer::new);
        event.registerEntityRenderer(GenesisCompatManager.SENTRY_GOLEM.get(), SentryGolemRenderer::new);
        event.registerEntityRenderer(GenesisCompatManager.TEMPEST_SERVANT.get(), TempestServantRenderer::new);
        event.registerEntityRenderer(GenesisCompatManager.TEMPEST_THUNDERBALL.get(), ThunderBallRenderer::new);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GoetyAetherModelLayers.SENTRY_GOLEM, SentryGolemModel::createBodyLayer);
        event.registerLayerDefinition(GoetyAetherModelLayers.SENTRY_GUARDIAN, SentryGuardianModel::createBodyLayer);
        event.registerLayerDefinition(GoetyAetherModelLayers.TRACKING_GOLEM, TrackingGolemModel::createBodyLayer);
        event.registerLayerDefinition(GoetyAetherModelLayers.TEMPEST, TempestServantModel::createBodyLayer);
        event.registerLayerDefinition(GoetyAetherModelLayers.TEMPEST_TRANSPARENCY, TempestServantModel::createBodyLayer);
        event.registerLayerDefinition(GoetyAetherModelLayers.BATTLE_SENTRY, SlimeModel::createOuterBodyLayer);
    }
}
