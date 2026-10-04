package github.goldblock.goety_aether.client.render;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.goldblock.goety_aether.common.entities.ally.mobs.TempestServant;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TempestServantRenderer extends MobRenderer<TempestServant, TempestServantModel> {
    private static final ResourceLocation TEMPEST_TEXTURE = new ResourceLocation("aether_genesis", "textures/entity/mobs/tempest/tempest.png");
    private static final RenderType TEMPEST_EYES = RenderType.eyes(new ResourceLocation("aether_genesis", "textures/entity/mobs/tempest/tempest_emissive.png"));
    private static final ResourceLocation TEMPEST_LAYER_TEXTURE = new ResourceLocation("aether_genesis", "textures/entity/mobs/tempest/tempest_layer.png");
    private static final RenderType LAYER_GLOW = RenderType.eyes(new ResourceLocation("aether_genesis", "textures/entity/mobs/tempest/tempest_layer_glow.png"));

    public TempestServantRenderer(EntityRendererProvider.Context context) {
        super(context, new TempestServantModel(context.bakeLayer(GoetyAetherModelLayers.TEMPEST)), 0.5F);
        this.addLayer(new TempestEyesLayer(this));
        this.addLayer(new TempestTransparencyLayer(this, new TempestServantModel(context.bakeLayer(GoetyAetherModelLayers.TEMPEST_TRANSPARENCY))));
        this.addLayer(new TempestGlowLayer(this));
    }

    @Override
    protected void scale(TempestServant tempest, PoseStack poseStack, float partialTickTime) {
        float f = Mth.lerp(partialTickTime, (float) tempest.getCloudScale(), (float) (tempest.getCloudScale() + tempest.getCloudScaleAdd()));
        float f1 = f / 40.0F;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }
        f1 = 1.0F / ((float) Math.pow(f1, 5.0D) * 2.0F + 1.0F);
        float f2 = (8.0F + f1) / 2.0F;
        float f3 = (8.0F + 1.0F / f1) / 2.0F;
        poseStack.scale(f3, f2, f3);
        poseStack.translate(0.0D, 0.375D, 0.0D);
        poseStack.scale(0.55F, 0.55F, 0.55F);
        poseStack.translate(0.0D, -0.1D, 0.0D);
        float sin = Mth.sin(((float) tempest.tickCount + partialTickTime) / 6.0F);
        poseStack.translate(0.0D, sin / 15.0F, 0.0D);
    }

    @Override
    protected float getBob(TempestServant tempest, float partialTicks) {
        return Mth.lerp(partialTicks, tempest.getTailRot(), tempest.getTailRot() + tempest.getTailRotAdd());
    }

    @Override
    public ResourceLocation getTextureLocation(TempestServant entity) {
        return TEMPEST_TEXTURE;
    }

    static class TempestEyesLayer extends EyesLayer<TempestServant, TempestServantModel> {
        TempestEyesLayer(RenderLayerParent<TempestServant, TempestServantModel> parent) {
            super(parent);
        }

        @Override
        public RenderType renderType() {
            return TEMPEST_EYES;
        }
    }

    static class TempestGlowLayer extends EyesLayer<TempestServant, TempestServantModel> {
        TempestGlowLayer(RenderLayerParent<TempestServant, TempestServantModel> parent) {
            super(parent);
        }

        @Override
        public RenderType renderType() {
            return LAYER_GLOW;
        }
    }

    static class TempestTransparencyLayer extends RenderLayer<TempestServant, TempestServantModel> {
        private final TempestServantModel transparency;

        TempestTransparencyLayer(RenderLayerParent<TempestServant, TempestServantModel> parent, TempestServantModel transparencyModel) {
            super(parent);
            this.transparency = transparencyModel;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, TempestServant tempest, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!tempest.isInvisible()) {
                EntityModel rawModel = (EntityModel) this.getParentModel();
                rawModel.copyPropertiesTo(this.transparency);
                this.transparency.prepareMobModel(null, limbSwing, limbSwingAmount, partialTicks);
                this.transparency.setupAnim(null, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(TEMPEST_LAYER_TEXTURE));
                this.transparency.renderToBuffer(poseStack, consumer, packedLight, LivingEntityRenderer.getOverlayCoords(tempest, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
