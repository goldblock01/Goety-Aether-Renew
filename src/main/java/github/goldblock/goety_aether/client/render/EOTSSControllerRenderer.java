package github.goldblock.goety_aether.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import github.goldblock.goety_aether.common.entities.ally.mobs.EOTSSController;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EOTSSControllerRenderer extends MobRenderer<EOTSSController, EOTSSControllerModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("deep_aether", "textures/entity/eots/eots_controller.png");

    public EOTSSControllerRenderer(EntityRendererProvider.Context context) {
        super(context, new EOTSSControllerModel(context.bakeLayer(GoetyAetherModelLayers.EOTS_CONTROLLER)), 0.0F);
    }

    @Override
    public void render(EOTSSController entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.scale(1.3F, 1.3F, 1.3F);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected void scale(EOTSSController entity, PoseStack poseStack, float partialTickTime) {
        super.scale(entity, poseStack, partialTickTime);
        float age = Math.min(entity.localEmergingTime(), entity.getAge());
        float factor = age / entity.localEmergingTime();
        poseStack.scale(factor, factor, factor);
    }

    @Override
    protected float getFlipDegrees(EOTSSController entity) {
        return 0.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(EOTSSController entity) {
        return TEXTURE;
    }
}
