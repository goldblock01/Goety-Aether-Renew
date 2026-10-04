package github.goldblock.goety_aether.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.goldblock.goety_aether.common.entities.ally.mobs.BabyZephyrServant;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class BabyZephyrServantModel extends EntityModel<BabyZephyrServant> {
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart tail2;

    public BabyZephyrServantModel(ModelPart root) {
        this.body = root.getChild("body");
        this.tail = this.body.getChild("tail");
        this.tail2 = this.tail.getChild("tail2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -4.5F, -8.0F, 12.0F, 9.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.5F, 0.0F, 0.0F, 3.1416F, 0.0F));

        body.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(30, 42).addBox(-8.0F, -10.0F, -5.0F, 2.0F, 7.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(2, 40).addBox(6.0F, -10.0F, -5.0F, 2.0F, 7.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(16, 38).addBox(-3.0F, -9.0F, -10.0F, 6.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.5F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -11.0F));

        tail.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(40, 0).addBox(-3.0F, -9.0F, -17.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.5F, 11.0F));

        tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(59, 6).addBox(-4.0F, -8.0F, -2.0F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 6.5F, -10.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(BabyZephyrServant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.tail.yRot = Mth.sin(ageInTicks / 2.5F) / 4.0F;
        this.tail2.yRot = Mth.sin(ageInTicks / 2.5F) / 4.0F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.scale(0.3F, 0.3F, 0.3F);
        poseStack.translate(0.0F, 1.667F, 0.0F);
        this.body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
