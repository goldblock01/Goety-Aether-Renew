package github.goldblock.goety_aether.client.render;

import com.aetherteam.aether.client.renderer.AetherModelLayers;
import com.aetherteam.aether.client.renderer.entity.layers.ValkyrieWingsLayer;
import com.aetherteam.aether.client.renderer.entity.model.ValkyrieModel;
import com.aetherteam.aether.client.renderer.entity.model.ValkyrieWingsModel;
import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.entities.ally.mobs.ZombieValkyrieQueenServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ZombieValkyrieQueenServantRenderer extends MobRenderer<ZombieValkyrieQueenServant, ValkyrieModel<ZombieValkyrieQueenServant>> {
    private static final ResourceLocation ZOMBIE_VALKYRIE_QUEEN_TEXTURE = new ResourceLocation(GoetyAether.MOD_ID, "textures/entity/zombie_valkyrie_queen_servant.png");

    public ZombieValkyrieQueenServantRenderer(EntityRendererProvider.Context context) {
        super(context, new ValkyrieModel<>(context.bakeLayer(AetherModelLayers.VALKYRIE_QUEEN)), 0.3F);
        this.addLayer(new ValkyrieWingsLayer<>(this, ZOMBIE_VALKYRIE_QUEEN_TEXTURE, new ValkyrieWingsModel<>(context.bakeLayer(AetherModelLayers.VALKYRIE_QUEEN_WINGS))));
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieValkyrieQueenServant valkyrie) {
        return ZOMBIE_VALKYRIE_QUEEN_TEXTURE;
    }
}
