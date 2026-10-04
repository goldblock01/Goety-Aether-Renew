package github.goldblock.goety_aether.client.render;

import com.aetherteam.aether.client.renderer.entity.model.ZephyrModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.goldblock.goety_aether.common.entities.ally.mobs.ZephyrServant;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public class ZephyrServantModel extends EntityModel<ZephyrServant> {
    private final ZephyrModel delegate;

    public ZephyrServantModel(ModelPart root) {
        this.delegate = new ZephyrModel(root);
    }

    public static LayerDefinition createBodyLayer() {
        return ZephyrModel.createBodyLayer();
    }

    @Override
    public void setupAnim(ZephyrServant zephyr, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.delegate.setupAnim(null, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.delegate.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
