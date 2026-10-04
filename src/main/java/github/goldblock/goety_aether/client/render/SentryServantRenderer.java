package github.goldblock.goety_aether.client.render;

import com.aetherteam.aether.client.renderer.entity.SentryRenderer;
import com.aetherteam.aether.entity.monster.dungeon.Sentry;
import github.goldblock.goety_aether.GoetyAether;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SentryServantRenderer extends SentryRenderer {
    private static final ResourceLocation SENTRY_SERVANT_TEXTURE = new ResourceLocation(GoetyAether.MOD_ID, "textures/entity/sentry_servant.png");
    private static final ResourceLocation SENTRY_SERVANT_LIT_TEXTURE = new ResourceLocation(GoetyAether.MOD_ID, "textures/entity/sentry_servant_lit.png");

    public SentryServantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Sentry sentry) {
        return sentry.isAwake() ? SENTRY_SERVANT_LIT_TEXTURE : SENTRY_SERVANT_TEXTURE;
    }
}
