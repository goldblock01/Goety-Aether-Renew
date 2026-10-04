package github.goldblock.goety_aether.client.render;

import github.goldblock.goety_aether.common.entities.projectile.RainPoisonDart;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RainPoisonDartRenderer extends ArrowRenderer<RainPoisonDart> {
    private static final ResourceLocation POISON_DART_TEXTURE = new ResourceLocation("aether", "textures/entity/projectile/dart/poison_dart.png");

    public RainPoisonDartRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(RainPoisonDart dart) {
        return POISON_DART_TEXTURE;
    }
}
