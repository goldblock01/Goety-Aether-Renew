package github.goldblock.goety_aether.client.render;

import com.Polarice3.Goety.client.render.BlackWolfRenderer;
import com.Polarice3.Goety.common.entities.ally.BlackWolf;
import github.goldblock.goety_aether.GoetyAether;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SkyWolfRenderer extends BlackWolfRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(GoetyAether.MOD_ID, "textures/entity/sky_wolf.png");
    private static final ResourceLocation HOSTILE = new ResourceLocation(GoetyAether.MOD_ID, "textures/entity/sky_wolf_hostile.png");

    public SkyWolfRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(BlackWolf entity) {
        return entity.isHostile() ? HOSTILE : TEXTURE;
    }
}
