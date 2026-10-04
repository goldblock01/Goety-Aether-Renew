package github.goldblock.goety_aether.client.render;

import github.goldblock.goety_aether.common.entities.ally.mobs.SentryGuardian;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SentryGuardianRenderer extends MobRenderer<SentryGuardian, SentryGuardianModel> {
    private static final ResourceLocation SENTRY_GUARDIAN_TEXTURE = new ResourceLocation("aether_genesis", "textures/entity/mobs/sentry_guardian/sentry_guardian.png");
    private static final ResourceLocation SENTRY_GUARDIAN_TEXTURE_CRITICAL = new ResourceLocation("aether_genesis", "textures/entity/mobs/sentry_guardian/sentry_guardian_critical.png");

    public SentryGuardianRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SentryGuardianModel(pContext.bakeLayer(GoetyAetherModelLayers.SENTRY_GUARDIAN)), 0.5F);
        this.addLayer(new SentryGuardianGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SentryGuardian pEntity) {
        return pEntity.getSeenEnemy() ? SENTRY_GUARDIAN_TEXTURE_CRITICAL : SENTRY_GUARDIAN_TEXTURE;
    }
}
