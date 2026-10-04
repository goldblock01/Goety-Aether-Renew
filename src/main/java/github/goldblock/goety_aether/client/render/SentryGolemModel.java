package github.goldblock.goety_aether.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.goldblock.goety_aether.common.entities.ally.mobs.SentryGolem;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class SentryGolemModel extends EntityModel<SentryGolem> {
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public SentryGolemModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.rightArm = this.body.getChild("right_arm");
        this.leftArm = this.body.getChild("left_arm");
        this.rightLeg = this.body.getChild("right_leg");
        this.leftLeg = this.body.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 50).addBox(-6.0F, 1.25F, -4.5F, 12.0F, 5.0F, 9.0F, CubeDeformation.NONE)
                .texOffs(0, 17).addBox(-8.0F, -8.75F, -5.5F, 16.0F, 10.0F, 11.0F, CubeDeformation.NONE), PartPose.offset(0.0F, 5.75F, 0.0F));
        body.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-5.5F, -6.0F, -4.5F, 11.0F, 8.0F, 9.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -10.75F, -0.5F));
        body.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(54, 17).addBox(-5.0F, 5.0F, -3.5F, 5.0F, 6.0F, 7.0F, CubeDeformation.NONE)
                .texOffs(40, 3).addBox(-4.0F, -3.0F, -3.0F, 4.0F, 8.0F, 6.0F, CubeDeformation.NONE), PartPose.offset(-8.0F, -4.75F, 0.0F));
        body.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(54, 17).mirror().addBox(0.0F, 5.0F, -3.5F, 5.0F, 6.0F, 7.0F, CubeDeformation.NONE).mirror(false)
                .texOffs(40, 3).mirror().addBox(0.0F, -3.0F, -3.0F, 4.0F, 8.0F, 6.0F, CubeDeformation.NONE).mirror(false), PartPose.offset(8.0F, -4.75F, 0.0F));
        body.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(42, 47).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 11.0F, 6.0F, CubeDeformation.NONE), PartPose.offset(-3.5F, 6.25F, 0.0F));
        body.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(42, 47).mirror().addBox(-2.5F, 0.0F, -3.0F, 5.0F, 11.0F, 6.0F, CubeDeformation.NONE).mirror(false), PartPose.offset(3.5F, 6.25F, 0.0F));
        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public void setupAnim(SentryGolem entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot = headPitch * Mth.DEG_TO_RAD;
        float swing = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.rightLeg.xRot = swing;
        this.leftLeg.xRot = -swing;

        this.rightArm.resetPose();
        this.leftArm.resetPose();

        float target = entity.getHandState() == SentryGolem.HAND_STATE_RAISED ? 1.0F : 0.5F;
        if (entity.progress < target) {
            entity.progress += 0.06F;
        }
        if (entity.progress > target) {
            entity.progress -= 0.06F;
        }

        this.rightArm.xRot = -3.0F * entity.progress;
        this.leftArm.xRot = -3.0F * entity.progress;
        this.rightArm.yRot = -0.3F * entity.progress;
        this.leftArm.yRot = 0.3F * entity.progress;
        this.rightArm.zRot = 0.3F * entity.progress;
        this.leftArm.zRot = -0.3F * entity.progress;

        AnimationUtils.bobModelPart(this.rightArm, ageInTicks, 1.0F);
        AnimationUtils.bobModelPart(this.leftArm, ageInTicks, -1.0F);
    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        this.body.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }
}
