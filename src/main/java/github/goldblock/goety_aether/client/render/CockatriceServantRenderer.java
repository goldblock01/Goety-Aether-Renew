package github.goldblock.goety_aether.client.render;

import com.aetherteam.aether.client.renderer.entity.CockatriceRenderer;
import github.goldblock.goety_aether.common.entities.ally.mobs.CockatriceServant;
import github.goldblock.goety_aether.compat.mod.AetherReduxCompat;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.zepalesque.redux.client.render.ReduxModelLayers;
import net.zepalesque.redux.client.render.entity.layer.entity.CockatriceReduxLayer;
import net.zepalesque.redux.client.render.entity.model.entity.CockatriceReduxModel;

public class CockatriceServantRenderer extends CockatriceRenderer {

    public CockatriceServantRenderer(EntityRendererProvider.Context context) {
        super(context);
        if (AetherReduxCompat.isReduxLoaded()) {
            this.addReduxLayers(context);
        }
    }

    private void addReduxLayers(EntityRendererProvider.Context context) {
        this.addLayer(new CockatriceReduxLayer(this,
                new CockatriceReduxModel(context.getModelSet().bakeLayer(ReduxModelLayers.COCKATRICE_OLD)),
                new CockatriceReduxModel(context.getModelSet().bakeLayer(ReduxModelLayers.COCKATRICE_REFRESHED))));
    }
}
