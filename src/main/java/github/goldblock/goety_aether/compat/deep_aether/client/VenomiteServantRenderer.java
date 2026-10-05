package github.goldblock.goety_aether.compat.deep_aether.client;

import github.goldblock.goety_aether.client.render.GoetyAetherModelLayers;
import github.goldblock.goety_aether.common.entities.ally.mobs.VenomiteServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class VenomiteServantRenderer extends MobRenderer<VenomiteServant, VenomiteServantModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("deep_aether", "textures/entity/venomite/venomite.png");
    private static final ResourceLocation ANGRY_TEXTURE = new ResourceLocation("deep_aether", "textures/entity/venomite/venomite_angry.png");

    public VenomiteServantRenderer(EntityRendererProvider.Context context) {
        super(context, new VenomiteServantModel(context.bakeLayer(GoetyAetherModelLayers.VENOMITE_SERVANT)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(VenomiteServant entity) {
        if (entity.isAggressive()) {
            return ANGRY_TEXTURE;
        }
        return TEXTURE;
    }
}
