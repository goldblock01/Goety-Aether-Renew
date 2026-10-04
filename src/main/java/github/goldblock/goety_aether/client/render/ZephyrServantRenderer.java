package github.goldblock.goety_aether.client.render;

import com.aetherteam.aether.client.renderer.AetherModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.goldblock.goety_aether.common.entities.ally.mobs.ZephyrServant;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ZephyrServantRenderer extends MobRenderer<ZephyrServant, ZephyrServantModel> {
    private static final ResourceLocation ZEPHYR_TEXTURE = new ResourceLocation("aether", "textures/entity/mobs/zephyr/zephyr.png");

    public ZephyrServantRenderer(EntityRendererProvider.Context context) {
        super(context, new ZephyrServantModel(context.bakeLayer(AetherModelLayers.ZEPHYR)), 0.5F);
        this.addLayer(new ZephyrServantTransparencyLayer(this, new ZephyrServantModel(context.bakeLayer(AetherModelLayers.ZEPHYR_TRANSPARENCY))));
    }

    @Override
    protected void scale(ZephyrServant zephyr, PoseStack poseStack, float partialTicks) {
        float f = Math.min(Mth.lerp(partialTicks, zephyr.getCloudScale(), zephyr.getCloudScale() + zephyr.getCloudScaleAdd()), 38.0F);
        float f1 = f / 38.0F;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }
        f1 = 1.0F / ((float) Math.pow(f1, 5.0D) * 2.0F + 1.0F);
        float f2 = (8.0F + f1) / 2.0F;
        float f3 = (8.0F + 1.0F / f1) / 2.0F;
        poseStack.scale(f3, f2, f3);
        poseStack.translate(0.0D, 0.5D, 0.0D);
    }

    @Override
    protected float getBob(ZephyrServant zephyr, float partialTicks) {
        return Mth.lerp(partialTicks, zephyr.getTailRot(), zephyr.getTailRot() + zephyr.getTailRotAdd());
    }

    @Override
    public ResourceLocation getTextureLocation(ZephyrServant entity) {
        return ZEPHYR_TEXTURE;
    }

    private static class ZephyrServantTransparencyLayer extends RenderLayer<ZephyrServant, ZephyrServantModel> {
        private static final ResourceLocation ZEPHYR_TRANSPARENCY_TEXTURE = new ResourceLocation("aether", "textures/entity/mobs/zephyr/zephyr_layer.png");
        private final ZephyrServantModel transparency;

        public ZephyrServantTransparencyLayer(RenderLayerParent<ZephyrServant, ZephyrServantModel> entityRenderer, ZephyrServantModel transparencyModel) {
            super(entityRenderer);
            this.transparency = transparencyModel;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ZephyrServant zephyr, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (this.getParentModel() instanceof ZephyrServantModel && !zephyr.isInvisible()) {
                this.getParentModel().copyPropertiesTo(this.transparency);
                this.transparency.prepareMobModel(zephyr, limbSwing, limbSwingAmount, partialTicks);
                this.transparency.setupAnim(zephyr, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(ZEPHYR_TRANSPARENCY_TEXTURE));
                this.transparency.renderToBuffer(poseStack, consumer, packedLight, net.minecraft.client.renderer.entity.LivingEntityRenderer.getOverlayCoords(zephyr, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
