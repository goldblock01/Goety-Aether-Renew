package github.goldblock.goety_aether.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.goldblock.goety_aether.common.entities.ally.mobs.EOTSServantSegment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class EOTSServantSegmentModel extends HierarchicalModel<EOTSServantSegment> {
    private final ModelPart body;
    private final ModelPart bb_main;
    private final ModelPart bb_segment;
    private final ModelPart upperMouth;
    private final ModelPart lowerMouth;

    public EOTSServantSegmentModel(ModelPart root) {
        this.body = root.getChild("body");
        this.bb_main = root.getChild("bb_main");
        this.bb_segment = root.getChild("bb_segment");
        this.upperMouth = this.bb_main.getChild("Head").getChild("upperMouth");
        this.lowerMouth = this.bb_main.getChild("Head").getChild("lowerMouth");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -8.0F, 0.0F, 14.0F, 12.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-2.0F, 18.0F, -8.0F));

        partdefinition.addOrReplaceChild("bb_segment", CubeListBuilder.create().texOffs(28, 28).addBox(-9.0F, -13.0F, -6.0F, 2.0F, 10.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).addBox(7.0F, -13.0F, -6.0F, 2.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, -3.0F));

        bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(44, 13).addBox(-4.0F, -5.0F, 0.0F, 8.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 1.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition Head11 = bb_main.addOrReplaceChild("Head11", CubeListBuilder.create(), PartPose.offset(2.0F, -6.0F, 8.0F));

        Head11.addOrReplaceChild("Head_r1", CubeListBuilder.create().texOffs(16, 28).addBox(-3.5F, -1.0F, -4.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -6.0F, 1.0F, 1.0462F, 0.5295F, 0.2342F));

        PartDefinition Head10 = bb_main.addOrReplaceChild("Head10", CubeListBuilder.create(), PartPose.offset(-2.0F, -6.0F, 8.0F));

        Head10.addOrReplaceChild("Head_r2", CubeListBuilder.create().texOffs(0, 21).addBox(1.5F, -6.0F, -4.0F, 2.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, -2.0F, 0.2618F, -0.3054F, 0.0F));

        PartDefinition Head2 = bb_main.addOrReplaceChild("Head2", CubeListBuilder.create(), PartPose.offset(2.0F, -6.0F, 8.0F));

        Head2.addOrReplaceChild("Head_r3", CubeListBuilder.create().texOffs(0, 40).addBox(-3.5F, -1.0F, -4.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -3.0F, 3.0F, 0.5662F, 0.5295F, 0.2342F));

        Head2.addOrReplaceChild("Head_r4", CubeListBuilder.create().texOffs(28, 9).addBox(-3.5F, -6.0F, -4.0F, 2.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, -2.0F, 0.2618F, 0.3054F, 0.0F));

        PartDefinition Head = bb_main.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, -10.0F, 8.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -6.0F, 8.0F));

        Head.addOrReplaceChild("Head_r5", CubeListBuilder.create().texOffs(32, 30).addBox(1.5F, -1.0F, -4.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -3.0F, 3.0F, 0.5662F, -0.5295F, -0.2342F));

        Head.addOrReplaceChild("Head_r6", CubeListBuilder.create().texOffs(16, 42).addBox(1.5F, -1.0F, -4.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -6.0F, 1.0F, 1.0462F, -0.5295F, -0.2342F));

        Head.addOrReplaceChild("upperMouth", CubeListBuilder.create().texOffs(44, 0).addBox(-2.5F, -3.0F, -8.0F, 5.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 3.0F, -10.0F));

        Head.addOrReplaceChild("lowerMouth", CubeListBuilder.create().texOffs(70, 3).addBox(-2.5F, 0.0F, -8.0F, 5.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 3.0F, -10.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(EOTSServantSegment entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        if (entity.isControllingSegment()) {
            this.body.visible = false;
            this.bb_segment.visible = false;
            this.bb_main.visible = true;
            if (entity.isMouthOpen()) {
                if (this.upperMouth.xRot > -0.3F) {
                    float mouthRotation = Mth.lerp(ageInTicks * 0.01F, this.upperMouth.xRot, -0.3F);
                    this.upperMouth.xRot = mouthRotation;
                    this.lowerMouth.xRot = -mouthRotation;
                }
            } else if (this.upperMouth.xRot < 0.0F) {
                float mouthRotation = Mth.lerp(ageInTicks * 0.01F, this.upperMouth.xRot, 0.0F);
                this.upperMouth.xRot = mouthRotation;
                this.lowerMouth.xRot = -mouthRotation;
            }
        } else {
            this.body.visible = true;
            this.bb_segment.visible = true;
            this.bb_main.visible = false;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.bb_segment.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.body;
    }
}
