package github.goldblock.goety_aether.client.render;

import github.goldblock.goety_aether.common.entities.ally.mobs.HoveringHurricaneServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HoveringHurricaneServantRenderer extends MobRenderer<HoveringHurricaneServant, HoveringHurricaneServantModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("legendary_monsters", "textures/entity/hovering_hurricane.png");

    public HoveringHurricaneServantRenderer(EntityRendererProvider.Context context) {
        super(context, new HoveringHurricaneServantModel(context.bakeLayer(GoetyAetherModelLayers.HURRICANE_SERVANT)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(HoveringHurricaneServant entity) {
        return TEXTURE;
    }
}
