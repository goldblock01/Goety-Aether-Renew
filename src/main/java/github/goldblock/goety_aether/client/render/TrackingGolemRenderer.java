package github.goldblock.goety_aether.client.render;

import github.goldblock.goety_aether.common.entities.ally.mobs.TrackingGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TrackingGolemRenderer extends MobRenderer<TrackingGolem, TrackingGolemModel> {
    private static final ResourceLocation TRACKING_GOLEM_TEXTURE = new ResourceLocation("aether_genesis", "textures/entity/mobs/tracking_golem/sentry_golem.png");
    private static final ResourceLocation TRACKING_GOLEM_TEXTURE_GLOW = new ResourceLocation("aether_genesis", "textures/entity/mobs/tracking_golem/sentry_golem_hostile.png");

    public TrackingGolemRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new TrackingGolemModel(pContext.bakeLayer(GoetyAetherModelLayers.TRACKING_GOLEM)), 0.5F);
        this.addLayer(new TrackingGolemGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(TrackingGolem pEntity) {
        return pEntity.getSeenEnemy() ? TRACKING_GOLEM_TEXTURE_GLOW : TRACKING_GOLEM_TEXTURE;
    }
}
