package github.goldblock.goety_aether.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.render.ModModelLayer;
import com.Polarice3.Goety.client.render.layer.HierarchicalArmorLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import github.goldblock.goety_aether.common.entities.hostile.illagers.Mountaineer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class MountaineerRenderer extends MobRenderer<Mountaineer, HostileMountaineerModel> {
    private static final ResourceLocation MOUNTAINEER_TEXTURE = Goety.location("textures/entity/servants/illager/mountaineer_original.png");

    public MountaineerRenderer(EntityRendererProvider.Context context) {
        super(context, new HostileMountaineerModel(context.bakeLayer(ModModelLayer.MOUNTAINEER)), 0.5F);
        this.addLayer(new HierarchicalArmorLayer(this, context));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(Mountaineer entity) {
        return MOUNTAINEER_TEXTURE;
    }

    @Override
    protected void scale(Mountaineer entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
