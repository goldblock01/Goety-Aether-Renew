package github.goldblock.goety_aether.compat.redux.client;

import github.goldblock.goety_aether.client.render.CockatriceServantRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.zepalesque.redux.client.render.ReduxModelLayers;
import net.zepalesque.redux.client.render.entity.layer.entity.CockatriceReduxLayer;
import net.zepalesque.redux.client.render.entity.model.entity.CockatriceReduxModel;

public class ReduxCockatriceServantRenderer extends CockatriceServantRenderer {

    public ReduxCockatriceServantRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new CockatriceReduxLayer(this,
                new CockatriceReduxModel(context.getModelSet().bakeLayer(ReduxModelLayers.COCKATRICE_OLD)),
                new CockatriceReduxModel(context.getModelSet().bakeLayer(ReduxModelLayers.COCKATRICE_REFRESHED))));
    }
}
