package github.goldblock.goety_aether.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.goldblock.goety_aether.common.entities.ally.mobs.SentryGuardian;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SentryGuardianGlowLayer extends EyesLayer<SentryGuardian, SentryGuardianModel> {
    private static final RenderType GUARDIAN_EYE = RenderType.eyes(new ResourceLocation("aether_genesis", "textures/entity/mobs/sentry_guardian/sentry_guardian_glow.png"));
    private static final RenderType GUARDIAN_EYE_LIT = RenderType.eyes(new ResourceLocation("aether_genesis", "textures/entity/mobs/sentry_guardian/sentry_guardian_critical_glow.png"));

    public SentryGuardianGlowLayer(RenderLayerParent<SentryGuardian, SentryGuardianModel> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, SentryGuardian pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        VertexConsumer vertexConsumer = pBuffer.getBuffer(this.renderType(pLivingEntity));
        this.getParentModel().renderToBuffer(pPoseStack, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public RenderType renderType(SentryGuardian pEntity) {
        return pEntity.getSeenEnemy() ? GUARDIAN_EYE_LIT : this.renderType();
    }

    @Override
    public RenderType renderType() {
        return GUARDIAN_EYE;
    }
}
