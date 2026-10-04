package github.goldblock.goety_aether.client.render;

import com.aetherteam.aether.client.renderer.AetherModelLayers;
import github.goldblock.goety_aether.common.entities.ally.mobs.AechorPlantServant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class AechorPlantServantRenderer extends MobRenderer<AechorPlantServant, AechorPlantServantModel> {
    private static final ResourceLocation AECHOR_PLANT_TEXTURE = new ResourceLocation("aether", "textures/entity/mobs/aechor_plant/aechor_plant.png");

    public AechorPlantServantRenderer(EntityRendererProvider.Context context) {
        super(context, new AechorPlantServantModel(context.bakeLayer(AetherModelLayers.AECHOR_PLANT)), 0.3F);
    }

    @Override
    protected void scale(AechorPlantServant aechorPlant, PoseStack poseStack, float partialTicks) {
        float f2 = 0.625F + aechorPlant.getSize() / 6.0F;
        poseStack.scale(f2, f2, f2);
        poseStack.translate(0.0D, 1.2D, 0.0D);
        this.shadowRadius = f2 - 0.25F;
    }

    @Override
    protected float getBob(AechorPlantServant aechorPlant, float partialTicks) {
        return Mth.lerp(partialTicks, aechorPlant.getSinage(), aechorPlant.getSinage() + aechorPlant.getSinageAdd());
    }

    @Override
    public ResourceLocation getTextureLocation(AechorPlantServant aechorPlant) {
        return AECHOR_PLANT_TEXTURE;
    }
}
