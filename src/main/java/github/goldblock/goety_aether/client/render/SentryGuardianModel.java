package github.goldblock.goety_aether.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.goldblock.goety_aether.common.entities.ally.mobs.SentryGuardian;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class SentryGuardianModel extends EntityModel<SentryGuardian> {
    private final ModelPart body;
    private final ModelPart leftArm;
    private final ModelPart rightArm;

    public SentryGuardianModel(ModelPart pRoot) {
        this.body = pRoot.getChild("body");
        this.leftArm = pRoot.getChild("left_arm_joint");
        this.rightArm = pRoot.getChild("right_arm_joint");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition body = partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 42).addBox(-7.0F, -19.0F, -7.0F, 14.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(32, 4).addBox(-9.0F, -39.0F, -9.0F, 18.0F, 20.0F, 18.0F, new CubeDeformation(0.0F)).texOffs(42, 48).addBox(-7.0F, -47.0F, -7.0F, 14.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        body.addOrReplaceChild("left_upper_arm", CubeListBuilder.create().texOffs(0, 0).addBox(14.0F, -15.0F, -45.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0436F, 2.4607F, -18.0F, 0.0F, -1.5708F, -0.7418F));
        body.addOrReplaceChild("right_upper_arm", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-27.0F, -41.4607F, -35.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-11.2057F, 15.2137F, -23.0F, 0.0F, 1.5708F, 0.7418F));
        PartDefinition leftArmJoint = partDefinition.addOrReplaceChild("left_arm_joint", CubeListBuilder.create(), PartPose.offset(18.0F, -1.0F, 0.0F));
        leftArmJoint.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 90).addBox(9.0F, -15.5F, -23.0F, 10.0F, 18.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-18.0F, 25.0F, 0.0F, -0.7854F, 0.0F, 0.0F));
        PartDefinition rightArmJoint = partDefinition.addOrReplaceChild("right_arm_joint", CubeListBuilder.create(), PartPose.offset(-18.0F, -1.0F, 0.0F));
        rightArmJoint.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 90).mirror().addBox(-19.0F, -15.5F, -23.0F, 10.0F, 18.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(18.0F, 25.0F, 0.0F, -0.7854F, 0.0F, 0.0F));
        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    @Override
    public void setupAnim(SentryGuardian pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.leftArm.xRot = -Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        this.rightArm.xRot = -Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        int attackTick = pEntity.getAttackAnimationTick();
        if (attackTick > 0) {
            float f = (float) attackTick / 10.0F;
            float swing = Mth.sin(f * Mth.PI);
            this.leftArm.xRot = -swing * 2.2F;
            this.rightArm.xRot = -swing * 2.2F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        this.leftArm.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        this.rightArm.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        this.body.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }
}
