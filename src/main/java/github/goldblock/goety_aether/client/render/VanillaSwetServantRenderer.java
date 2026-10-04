package github.goldblock.goety_aether.client.render;

import com.aetherteam.aether.client.renderer.entity.SwetRenderer;
import com.aetherteam.aether.entity.monster.Swet;
import github.goldblock.goety_aether.common.entities.ally.mobs.VanillaSwetServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class VanillaSwetServantRenderer extends SwetRenderer {
    private static final ResourceLocation VANILLA_TEXTURE = new ResourceLocation("aether_redux", "textures/entity/mobs/swet/swet_vanilla.png");

    public VanillaSwetServantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Swet swet) {
        return VANILLA_TEXTURE;
    }
}
