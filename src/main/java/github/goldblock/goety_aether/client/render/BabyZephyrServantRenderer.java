package github.goldblock.goety_aether.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import github.goldblock.goety_aether.common.entities.ally.mobs.BabyZephyrServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BabyZephyrServantRenderer extends MobRenderer<BabyZephyrServant, BabyZephyrServantModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("deep_aether", "textures/entity/baby_zephyr.png");

    public BabyZephyrServantRenderer(EntityRendererProvider.Context context) {
        super(context, new BabyZephyrServantModel(context.bakeLayer(GoetyAetherModelLayers.BABY_ZEPHYR)), 0.5F);
    }

    @Override
    protected void scale(BabyZephyrServant zephyr, PoseStack poseStack, float partialTicks) {
        float f = Math.min(Mth.lerp(partialTicks, (float) zephyr.getCloudScale(), (float) (zephyr.getCloudScale() + zephyr.getCloudScaleAdd())), 38.0F);
        float f1 = f / 38.0F;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }
        f1 = 1.0F / ((float) Math.pow(f1, 5.0D) * 2.0F + 1.0F);
        float f2 = (8.0F + f1) / 2.0F;
        float f3 = (8.0F + 1.0F / f1) / 2.0F;
        poseStack.scale(f3, f2, f3);
        poseStack.translate(0.0D, 0.55D, 0.0D);
    }

    @Override
    public ResourceLocation getTextureLocation(BabyZephyrServant entity) {
        return TEXTURE;
    }
}
