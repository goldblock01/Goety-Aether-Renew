package github.goldblock.goety_aether.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import github.goldblock.goety_aether.client.events.ClientEvents;
import github.goldblock.goety_aether.common.entities.projectile.SentryBomb;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SentryBombRenderer extends EntityRenderer<SentryBomb> {
    private static final ResourceLocation SENTRY_BOMB_TEXTURE = new ResourceLocation("aether", "textures/entity/mobs/sentry/sentry_lit.png");
    private static final RenderType SENTRY_BOMB_EYE = RenderType.eyes(new ResourceLocation("aether", "textures/entity/mobs/sentry/eye.png"));
    private final SentryBombModel model;

    public SentryBombRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new SentryBombModel(pContext.bakeLayer(ClientEvents.SENTRY_BOMB_LAYER));
        this.shadowRadius = 0.3F;
    }

    @Override
    public void render(SentryBomb pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot())));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        VertexConsumer consumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(SENTRY_BOMB_TEXTURE));
        this.model.renderToBuffer(pPoseStack, consumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        VertexConsumer eye = pBuffer.getBuffer(SENTRY_BOMB_EYE);
        this.model.renderToBuffer(pPoseStack, eye, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SentryBomb pEntity) {
        return SENTRY_BOMB_TEXTURE;
    }
}
