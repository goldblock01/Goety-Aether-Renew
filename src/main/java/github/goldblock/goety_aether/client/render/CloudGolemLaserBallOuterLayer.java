package github.goldblock.goety_aether.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.miauczel.legendary_monsters.LegendaryMonsters;
import github.goldblock.goety_aether.common.entities.ally.mobs.CloudGolemServant;


import net.miauczel.legendary_monsters.entity.client.Render.LMRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CloudGolemLaserBallOuterLayer extends RenderLayer<CloudGolemServant, CloudGolemServantModel<CloudGolemServant>> {
    private static final ResourceLocation LOCATION = new ResourceLocation(LegendaryMonsters.MOD_ID,"textures/entity/cloud_golem/laser_ball/laser_ball_outer.png");

    public CloudGolemLaserBallOuterLayer(CloudGolemServantRenderer renderIn) {
        super((RenderLayerParent<CloudGolemServant, CloudGolemServantModel<CloudGolemServant>>) renderIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, CloudGolemServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {


            RenderType eyes = LMRenderTypes.getGlowEyes(LOCATION);
        VertexConsumer VertexConsumer = bufferIn.getBuffer(eyes);

if (entity.getAttackState() == 14 ) {
    this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 0.5f, 0.5f, 0.5f, 0.4f);
}else {

    this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 0, 0, 0, 0);
}
}

}