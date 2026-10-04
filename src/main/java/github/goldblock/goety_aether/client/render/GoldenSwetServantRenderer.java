package github.goldblock.goety_aether.client.render;

import com.aetherteam.aether.client.renderer.entity.SwetRenderer;
import com.aetherteam.aether.entity.monster.Swet;
import github.goldblock.goety_aether.common.entities.ally.mobs.GoldenSwetServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GoldenSwetServantRenderer extends SwetRenderer {
    private static final ResourceLocation GOLDEN_TEXTURE = new ResourceLocation("aether", "textures/entity/mobs/swet/swet_golden.png");

    public GoldenSwetServantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Swet swet) {
        return GOLDEN_TEXTURE;
    }
}
