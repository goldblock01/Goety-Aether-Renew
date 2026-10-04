package github.goldblock.goety_aether.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import github.goldblock.goety_aether.common.entities.ally.mobs.EOTSServantSegment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class EOTSServantSegmentRenderer extends MobRenderer<EOTSServantSegment, EOTSServantSegmentModel> {
    private static final ResourceLocation EOTS_SEGMENT_LOCATION = new ResourceLocation("deep_aether", "textures/entity/eots/eots_segment.png");
    private static final ResourceLocation EOTS_SEGMENT_CONTROLLING_LOCATION = new ResourceLocation("deep_aether", "textures/entity/eots/eots_segment_controlling.png");

    public EOTSServantSegmentRenderer(EntityRendererProvider.Context context) {
        super(context, new EOTSServantSegmentModel(context.bakeLayer(GoetyAetherModelLayers.EOTS_SEGMENT)), 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(EOTSServantSegment segment) {
        return segment.isControllingSegment() ? EOTS_SEGMENT_CONTROLLING_LOCATION : EOTS_SEGMENT_LOCATION;
    }

    @Override
    protected float getFlipDegrees(EOTSServantSegment segment) {
        return 0.0F;
    }

    @Override
    public void render(EOTSServantSegment segment, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (segment.isDeadOrDying()) {
            float scale = segment.getScale() - segment.deathTime / 20.0F;
            poseStack.scale(scale, scale, scale);
        }
        poseStack.scale(1.2F, 1.2F, 1.2F);
        super.render(segment, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected float getBob(EOTSServantSegment segment, float partialTick) {
        return partialTick;
    }

    @Override
    protected void setupRotations(EOTSServantSegment segment, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(segment, poseStack, ageInTicks, rotationYaw, partialTicks);
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(ageInTicks, segment.xRotO, segment.getXRot())));
    }
}
