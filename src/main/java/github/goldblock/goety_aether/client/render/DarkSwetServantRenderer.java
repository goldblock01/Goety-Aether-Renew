package github.goldblock.goety_aether.client.render;

import com.aetherteam.aether.client.renderer.entity.SwetRenderer;
import com.aetherteam.aether.entity.monster.Swet;
import github.goldblock.goety_aether.common.entities.ally.mobs.DarkSwetServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DarkSwetServantRenderer extends SwetRenderer {
    private static final ResourceLocation DARK_TEXTURE = new ResourceLocation("aether_genesis", "textures/entity/mobs/swet/swet_dark.png");

    public DarkSwetServantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Swet swet) {
        return DARK_TEXTURE;
    }
}
