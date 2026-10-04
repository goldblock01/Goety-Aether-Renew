package github.goldblock.goety_aether.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.render.ModModelLayer;
import com.Polarice3.Goety.client.render.layer.HierarchicalArmorLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import github.goldblock.goety_aether.common.entities.hostile.illagers.WindCaller;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WindCallerRenderer extends MobRenderer<WindCaller, HostileWindCallerModel> {
    private static final ResourceLocation WIND_CALLER_TEXTURE = Goety.location("textures/entity/servants/illager/wind_caller_original.png");

    public WindCallerRenderer(EntityRendererProvider.Context context) {
        super(context, new HostileWindCallerModel(context.bakeLayer(ModModelLayer.WIND_CALLER)), 0.5F);
        this.addLayer(new HierarchicalArmorLayer(this, context));
    }

    @Override
    public ResourceLocation getTextureLocation(WindCaller entity) {
        return WIND_CALLER_TEXTURE;
    }

    @Override
    protected void scale(WindCaller entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
