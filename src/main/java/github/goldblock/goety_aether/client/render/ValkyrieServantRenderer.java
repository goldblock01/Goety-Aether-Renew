package github.goldblock.goety_aether.client.render;

import com.aetherteam.aether.client.renderer.entity.ValkyrieRenderer;
import com.aetherteam.aether.entity.monster.dungeon.Valkyrie;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ValkyrieServantRenderer extends ValkyrieRenderer {
    private static final ResourceLocation VALKYRIE_SERVANT_TEXTURE = new ResourceLocation("aether", "textures/entity/mobs/valkyrie/valkyrie.png");

    public ValkyrieServantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Valkyrie valkyrie) {
        return VALKYRIE_SERVANT_TEXTURE;
    }
}
