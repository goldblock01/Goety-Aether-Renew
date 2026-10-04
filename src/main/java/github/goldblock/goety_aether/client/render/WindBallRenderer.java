package github.goldblock.goety_aether.client.render;

import com.aetherteam.aether.client.renderer.entity.IceCrystalRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import github.goldblock.goety_aether.common.entities.projectile.WindCrystal;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class WindBallRenderer extends IceCrystalRenderer<WindCrystal> {
    private static final ResourceLocation WIND_BALL_TEXTURE = new ResourceLocation("deep_aether", "textures/entity/projectile/wind_ball.png");

    public WindBallRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(WindCrystal crystal) {
        return WIND_BALL_TEXTURE;
    }

    @Override
    public void render(WindCrystal crystal, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (crystal.isFriendly()) {
            poseStack.scale(0.4F, 0.4F, 0.4F);
        }
        super.render(crystal, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
