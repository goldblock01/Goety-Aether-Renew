package github.goldblock.goety_aether.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.goldblock.goety_aether.common.entities.ally.mobs.TrackingGolem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class TrackingGolemModel extends EntityModel<TrackingGolem> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLegJoint;
    private final ModelPart leftLegJoint;

    public TrackingGolemModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLegJoint = root.getChild("right_leg_joint");
        this.leftLegJoint = root.getChild("left_leg_joint");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -8.0F, -4.5F, 11.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));
        partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 50).addBox(-6.0F, -16.0F, -4.5F, 12.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(0, 17).addBox(-8.0F, -25.0F, -5.5F, 16.0F, 10.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(54, 17).mirror().addBox(8.0F, 8.0F, -3.5F, 5.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(40, 3).mirror().addBox(8.0F, 0.0F, -3.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));
        partDefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(54, 17).addBox(-13.0F, 7.0F, -3.5F, 5.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(40, 3).addBox(-12.0F, -1.0F, -3.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition leftLegJoint = partDefinition.addOrReplaceChild("left_leg_joint", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 0.0F));
        leftLegJoint.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(42, 47).mirror().addBox(0.0F, -11.0F, -3.0F, 5.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition rightLegJoint = partDefinition.addOrReplaceChild("right_leg_joint", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 0.0F));
        rightLegJoint.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(42, 47).addBox(-2.5F, -5.5F, -3.0F, 5.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshDefinition, 128, 64);
    }

    @Override
    public void setupAnim(TrackingGolem pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.head.yRot = pNetHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot = pHeadPitch * Mth.DEG_TO_RAD;
        float f = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        this.rightArm.xRot = f;
        this.leftArm.xRot = -f;
        this.rightLegJoint.xRot = -f;
        this.leftLegJoint.xRot = f;
    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        this.root.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }
}
