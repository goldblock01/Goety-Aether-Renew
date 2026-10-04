package github.goldblock.goety_aether.client.render;

import github.goldblock.goety_aether.common.entities.ally.mobs.CreeperServant;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CreeperServantRenderer extends MobRenderer<CreeperServant, CreeperModel<CreeperServant>> {
    private static final ResourceLocation CREEPER_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper.png");

    public CreeperServantRenderer(EntityRendererProvider.Context context) {
        super(context, new CreeperModel<>(context.bakeLayer(ModelLayers.CREEPER)), 0.5F);
    }

    @Override
    protected float getWhiteOverlayProgress(CreeperServant creeper, float partialTicks) {
        float swelling = creeper.getSwelling(partialTicks);
        return (int) (swelling * 10.0F) % 2 == 0 ? 0.0F : Math.min(swelling, 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(CreeperServant creeper) {
        return CREEPER_TEXTURE;
    }
}
