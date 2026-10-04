package github.goldblock.goety_aether.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.goldblock.goety_aether.common.entities.ally.mobs.BattleSentryServant;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BattleSentryServantRenderer extends MobRenderer<BattleSentryServant, SlimeModel<BattleSentryServant>> {
    private static final ResourceLocation BATTLE_SENTRY_SERVANT_TEXTURE = new ResourceLocation("aether_genesis", "textures/entity/mobs/battle_sentry/battle_sentry.png");
    private static final ResourceLocation BATTLE_SENTRY_SERVANT_LIT_TEXTURE = new ResourceLocation("aether_genesis", "textures/entity/mobs/battle_sentry/battle_sentry_lit.png");
    private static final RenderType SENTRY_EYE = RenderType.eyes(new ResourceLocation("aether_genesis", "textures/entity/mobs/battle_sentry/eye.png"));

    public BattleSentryServantRenderer(EntityRendererProvider.Context context) {
        super(context, new SlimeModel<>(context.bakeLayer(GoetyAetherModelLayers.BATTLE_SENTRY)), 0.3F);
        this.addLayer(new BattleSentryEyeLayer(this));
    }

    @Override
    protected void scale(BattleSentryServant sentry, PoseStack poseStack, float partialTickTime) {
        float f = 0.879F;
        poseStack.scale(f, f, f);
        float f1 = (float) sentry.getSize() + 1.0F;
        float f2 = 0.0F;
        float f3 = 1.0F / (f2 + 1.0F);
        poseStack.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    @Override
    public void render(BattleSentryServant sentry, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (sentry.isAwake() && !sentry.hurtMarked) {
            super.render(sentry, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
        if (!sentry.isAwake() || sentry.hurtMarked) {
            poseStack.pushPose();
            poseStack.translate(0.0F, -0.75F, 0.0F);
            super.render(sentry, entityYaw, partialTicks, poseStack, buffer, packedLight);
            poseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(BattleSentryServant sentry) {
        return sentry.isAwake() ? BATTLE_SENTRY_SERVANT_LIT_TEXTURE : BATTLE_SENTRY_SERVANT_TEXTURE;
    }

    static class BattleSentryEyeLayer extends EyesLayer<BattleSentryServant, SlimeModel<BattleSentryServant>> {
        BattleSentryEyeLayer(RenderLayerParent<BattleSentryServant, SlimeModel<BattleSentryServant>> parent) {
            super(parent);
        }

        @Override
        public RenderType renderType() {
            return SENTRY_EYE;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, BattleSentryServant sentry, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer consumer = buffer.getBuffer(this.renderType());
            if (sentry.isAwake()) {
                this.getParentModel().renderToBuffer(poseStack, consumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
