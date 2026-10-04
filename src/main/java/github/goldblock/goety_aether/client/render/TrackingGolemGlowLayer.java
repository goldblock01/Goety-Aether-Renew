package github.goldblock.goety_aether.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.goldblock.goety_aether.common.entities.ally.mobs.TrackingGolem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class TrackingGolemGlowLayer extends EyesLayer<TrackingGolem, TrackingGolemModel> {
    private static final RenderType TRACKING_GOLEM_EYE = RenderType.eyes(new ResourceLocation("aether_genesis", "textures/entity/mobs/tracking_golem/sentry_golem_glow.png"));
    private static final RenderType TRACKING_GOLEM_EYE_GLOW = RenderType.eyes(new ResourceLocation("aether_genesis", "textures/entity/mobs/tracking_golem/sentry_golem_hostile_glow.png"));

    public TrackingGolemGlowLayer(RenderLayerParent<TrackingGolem, TrackingGolemModel> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, TrackingGolem pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.renderType(pEntity));
        this.getParentModel().renderToBuffer(pPoseStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public RenderType renderType() {
        return TRACKING_GOLEM_EYE;
    }

    public RenderType renderType(TrackingGolem pEntity) {
        return pEntity.getSeenEnemy() ? TRACKING_GOLEM_EYE_GLOW : TRACKING_GOLEM_EYE;
    }
}
