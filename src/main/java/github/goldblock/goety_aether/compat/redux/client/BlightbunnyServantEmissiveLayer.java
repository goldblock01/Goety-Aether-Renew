package github.goldblock.goety_aether.compat.redux.client;

import github.goldblock.goety_aether.common.entities.ally.mobs.BlightbunnyServant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class BlightbunnyServantEmissiveLayer extends EyesLayer<BlightbunnyServant, BlightbunnyServantModel> {
    private final RenderType renderType;

    public BlightbunnyServantEmissiveLayer(RenderLayerParent<BlightbunnyServant, BlightbunnyServantModel> parent, RenderType renderType) {
        super(parent);
        this.renderType = renderType;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, BlightbunnyServant bunny, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        VertexConsumer vertexconsumer = buffer.getBuffer(this.renderType());
        this.getParentModel().renderToBuffer(poseStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public RenderType renderType() {
        return this.renderType;
    }
}
