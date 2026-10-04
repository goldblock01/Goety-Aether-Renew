package github.goldblock.goety_aether.client.render;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.goldblock.goety_aether.common.entities.ally.mobs.AechorPlantServant;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class AechorPlantServantModel extends EntityModel<AechorPlantServant> {
    private final ModelPart stem;
    private final ModelPart head;
    private final ImmutableList<ModelPart> stamenStems;
    private final ImmutableList<ModelPart> leaves;
    private final ImmutableList<ModelPart> petals;

    public AechorPlantServantModel(ModelPart root) {
        this.stem = root.getChild("stem");
        this.head = root.getChild("head");
        this.stamenStems = ImmutableList.of(
                this.stem.getChild("stamen_stem_1"),
                this.stem.getChild("stamen_stem_2"),
                this.stem.getChild("stamen_stem_3"));
        ImmutableList.Builder<ModelPart> leaves = ImmutableList.builder();
        for (int i = 1; i <= 10; i++) {
            leaves.add(this.stem.getChild("leaf_" + i));
        }
        this.leaves = leaves.build();
        ImmutableList.Builder<ModelPart> petals = ImmutableList.builder();
        for (int i = 1; i <= 5; i++) {
            petals.add(this.stem.getChild("upper_petal_" + i));
            petals.add(this.stem.getChild("lower_petal_" + i));
        }
        this.petals = petals.build();
    }

    @Override
    public void setupAnim(AechorPlantServant aechorPlant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float sinage1 = Mth.sin(ageInTicks);
        float sinage2;
        if (aechorPlant.hurtTime > 0) {
            sinage1 = sinage1 * 0.45F - 0.125F;
            sinage2 = 1.75F + Mth.sin(ageInTicks + 2.0F) * 1.5F;
        } else if (aechorPlant.getTargetingEntity()) {
            sinage1 *= 0.25F;
            sinage2 = 1.75F + Mth.sin(ageInTicks + 2.0F) * 1.5F;
        } else {
            sinage1 *= 0.125F;
            sinage2 = 1.75F;
        }

        this.head.xRot = headPitch / 57.29578F;
        this.stem.xRot = this.head.xRot;
        this.stem.y = sinage2 * 0.5F;

        int i = 0;
        for (ModelPart part : this.stamenStems) {
            part.z = 0.2F + i / 15.0F;
            this.head.xRot += 0.1F;
            part.yRot += 2.0943952F * i;
            part.z += sinage1 * 0.4F;
            part.y = sinage2 + sinage1 * 2.0F;
            i++;
        }

        i = 0;
        for (ModelPart part : this.leaves) {
            part.z = (i % 2 == 0 ? 0.1F : 0.2F) + sinage1 * 0.75F;
            this.head.xRot += 0.31415927F;
            part.yRot += 0.62831855F * i;
            part.y = sinage2;
            i++;
        }

        i = 0;
        for (ModelPart part : this.petals) {
            part.z = (i % 2 == 0 ? -0.25F : -0.4125F) + sinage1;
            part.xRot = this.head.xRot + 0.62831855F * i;
            part.y = sinage2;
            i++;
        }

        this.head.y = sinage2 + sinage1 * 2.0F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.stem.render(poseStack, consumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.head.render(poseStack, consumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
