package github.goldblock.goety_aether.compat.redux.client;

import github.goldblock.goety_aether.common.entities.ally.mobs.BlightbunnyServant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class BlightbunnyServantModel extends EntityModel<BlightbunnyServant> {
    public final ModelPart base;
    public final ModelPart head;
    public final ModelPart rightEar;
    public final ModelPart leftEar;
    public final ModelPart rightWhiskers;
    public final ModelPart leftWhiskers;
    public final ModelPart body;
    public final ModelPart puff;
    public final ModelPart tail;
    public final ModelPart rightFrontLeg;
    public final ModelPart leftFrontLeg;
    public final ModelPart rightBackLeg;
    public final ModelPart leftBackLeg;
    public float puffiness;

    public BlightbunnyServantModel(ModelPart root) {
        this.base = root.getChild("base");
        this.head = this.base.getChild("head");
        this.rightEar = this.head.getChild("right_ear");
        this.leftEar = this.head.getChild("left_ear");
        this.rightWhiskers = this.head.getChild("right_whiskers");
        this.leftWhiskers = this.head.getChild("left_whiskers");
        this.body = this.base.getChild("body");
        this.puff = this.base.getChild("puff");
        this.tail = this.body.getChild("tail");
        this.rightFrontLeg = this.body.getChild("right_front_leg");
        this.leftFrontLeg = this.body.getChild("left_front_leg");
        this.rightBackLeg = this.body.getChild("right_back_leg");
        this.leftBackLeg = this.body.getChild("left_back_leg");
    }

    @Override
    public void prepareMobModel(BlightbunnyServant bunny, float limbSwing, float limbSwingAmount, float partialTicks) {
        super.prepareMobModel(bunny, limbSwing, limbSwingAmount, partialTicks);
        this.puffiness = Mth.lerp(partialTicks, bunny.getPuffiness(), bunny.getPuffiness() - bunny.getPuffSubtract()) / 20.0F;
    }

    @Override
    public void setupAnim(BlightbunnyServant bunny, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * 0.017453292F;
        this.head.yRot = netHeadYaw * 0.017453292F;
        this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount - this.body.xRot;
        this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount - this.body.xRot;
        this.rightBackLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.2F * limbSwingAmount - this.body.xRot;
        this.leftBackLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.2F * limbSwingAmount - this.body.xRot;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.head.render(poseStack, consumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.body.render(poseStack, consumer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.pushPose();
        float a = 1.0F + this.puffiness * 0.5F;
        this.puff.xScale = a;
        this.puff.yScale = a;
        this.puff.zScale = a;
        this.puff.render(poseStack, consumer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }
}
