package github.goldblock.goety_aether.compat.redux.client;

import github.goldblock.goety_aether.common.entities.ally.mobs.BlightbunnyServant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.zepalesque.redux.client.render.ReduxModelLayers;

public class BlightbunnyServantRenderer extends MobRenderer<BlightbunnyServant, BlightbunnyServantModel> {
    private static final ResourceLocation BLIGHTBUNNY_TEXTURE = new ResourceLocation("aether_redux", "textures/entity/mobs/blightbunny/blightbunny.png");
    private static final ResourceLocation BLIGHTBUNNY_GLOW_TEXTURE = new ResourceLocation("aether_redux", "textures/entity/mobs/blightbunny/blightbunny_glow.png");
    private static final ResourceLocation BLIGHTBUNNY_EYES_TEXTURE = new ResourceLocation("aether_redux", "textures/entity/mobs/blightbunny/blightbunny_eyes.png");

    public BlightbunnyServantRenderer(EntityRendererProvider.Context context) {
        super(context, new BlightbunnyServantModel(context.bakeLayer(ReduxModelLayers.BLIGHTBUNNY)), 0.3F);
        this.addLayer(new BlightbunnyServantEmissiveLayer(this, RenderType.entityTranslucentEmissive(BLIGHTBUNNY_GLOW_TEXTURE)));
        this.addLayer(new BlightbunnyServantEmissiveLayer(this, RenderType.eyes(BLIGHTBUNNY_EYES_TEXTURE)));
    }

    @Override
    protected void scale(BlightbunnyServant bunny, PoseStack poseStack, float partialTicks) {
        poseStack.translate(0.0F, 1.2, 0.0F);
    }

    @Override
    protected void setupRotations(BlightbunnyServant bunny, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(bunny, poseStack, ageInTicks, rotationYaw, partialTicks);
        if (!bunny.onGround()) {
            if (bunny.getDeltaMovement().y() > 0.5D) {
                poseStack.mulPose(Axis.XN.rotationDegrees(Mth.rotLerp(partialTicks, 0.0F, 15.0F)));
            } else if (bunny.getDeltaMovement().y() < -0.5D) {
                poseStack.mulPose(Axis.XN.rotationDegrees(Mth.rotLerp(partialTicks, 0.0F, -15.0F)));
            } else {
                poseStack.mulPose(Axis.XN.rotationDegrees((float) (bunny.getDeltaMovement().y() * 30.0D)));
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(BlightbunnyServant entity) {
        return BLIGHTBUNNY_TEXTURE;
    }
}
