package github.goldblock.goety_aether.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import github.goldblock.goety_aether.client.events.ClientEvents;
import github.goldblock.goety_aether.common.entities.ally.mobs.SentryGolem;
import github.goldblock.goety_aether.compat.genesis.client.GenesisCompatClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SentryGolemRenderer extends MobRenderer<SentryGolem, SentryGolemModel> {
    private static final ResourceLocation SENTRY_GOLEM_TEXTURE = new ResourceLocation("aether_genesis", "textures/entity/mobs/sentry_golem/sentry_golem.png");
    private static final ResourceLocation SENTRY_LIT_TEXTURE = new ResourceLocation("aether", "textures/entity/mobs/sentry/sentry_lit.png");
    private static final RenderType SENTRY_EYE = RenderType.eyes(new ResourceLocation("aether", "textures/entity/mobs/sentry/eye.png"));
    private final SentryBombModel bombModel;

    public SentryGolemRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SentryGolemModel(pContext.bakeLayer(GoetyAetherModelLayers.SENTRY_GOLEM)), 0.5F);
        this.addLayer(new SentryGolemGlowLayer(this));
        this.bombModel = new SentryBombModel(pContext.bakeLayer(ClientEvents.SENTRY_BOMB_LAYER));
    }

    @Override
    public void render(SentryGolem pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        if (pEntity.isAlive()) {
            this.renderBomb(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }
    }

    private void renderBomb(SentryGolem golem, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float progress = golem.progress;
        float scale = 1.0F;
        if (golem.getHandState() == SentryGolem.HAND_STATE_LOWERED) {
            if (!((double) progress < 0.5D)) {
                return;
            }
            scale = Math.min(1.0F - (float) (golem.getFireTime() - 30) / 30.0F, 0.9F);
            scale *= 1.1F;
        }

        poseStack.pushPose();
        poseStack.translate(0.0F, 4.2F, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp(partialTicks, golem.yBodyRotO, golem.yBodyRot)));
        poseStack.translate(0.0F, Mth.sin(1.0F - progress) * 2.4F + 1.65F, Mth.sin(1.0F - progress) * -1.4F);
        poseStack.scale(scale, scale, scale);
        this.bombModel.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(SENTRY_LIT_TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.bombModel.renderToBuffer(poseStack, buffer.getBuffer(SENTRY_EYE), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SentryGolem pEntity) {
        return SENTRY_GOLEM_TEXTURE;
    }
}
