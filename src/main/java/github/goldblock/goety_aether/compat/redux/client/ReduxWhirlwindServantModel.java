package github.goldblock.goety_aether.compat.redux.client;

import github.goldblock.goety_aether.common.entities.ally.mobs.AbstractWhirlwindServant;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class ReduxWhirlwindServantModel extends EntityModel<AbstractWhirlwindServant> {
    private final ModelPart whirlBody;
    private final ModelPart bottomRender;
    private final ModelPart lowerRender;
    private final ModelPart upperRender;
    private final ModelPart topRender;
    private final float[] alpha = new float[]{1.0F, 1.0F, 1.0F, 1.0F};

    public ReduxWhirlwindServantModel(ModelPart root) {
        this.whirlBody = root.getChild("whirl_body");
        ModelPart whirlBottom = this.whirlBody.getChild("whirl_bottom");
        this.bottomRender = whirlBottom.getChild("bottom_render");
        ModelPart whirlLower = whirlBottom.getChild("whirl_lower");
        this.lowerRender = whirlLower.getChild("lower_render");
        ModelPart whirlUpper = whirlLower.getChild("whirl_upper");
        this.upperRender = whirlUpper.getChild("upper_render");
        this.topRender = whirlUpper.getChild("whirl_top").getChild("top_render");
    }

    @Override
    public void setupAnim(AbstractWhirlwindServant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (ageInTicks < 20.0F) {
            for (int i = 0; i < 4; i++) {
                this.alpha[i] = ease(Mth.clamp(ageInTicks - 1.0F - 3 * i, 0.0F, 11.0F) / 11.0F);
            }
        } else if (entity.deathTime > 0 && entity.deathTime < 20) {
            for (int i = 0; i < 4; i++) {
                this.alpha[i] = ease(1.0F - Mth.clamp(entity.deathTime + ageInTicks % 1.0F - 3 * i, 0.0F, 11.0F) / 11.0F);
            }
        }
        if (entity.deathTime >= 20) {
            this.alpha[0] = 1.6777215E7F;
            this.alpha[1] = 1.6777215E7F;
            this.alpha[2] = 1.6777215E7F;
            this.alpha[3] = 1.6777215E7F;
        }

        boolean evil = entity.isEvil();
        float speedModif = evil ? -0.075F : -0.1F;
        float amountModif = evil ? 4.0F : 3.0F;

        this.whirlBody.getAllParts().forEach(ModelPart::resetPose);
        float f = ageInTicks * 3.1415927F * speedModif;

        this.bottomRender.x = Mth.cos(f) * -0.25F * 1.0F * amountModif;
        this.bottomRender.z = Mth.sin(f) * -0.25F * 1.0F * amountModif;

        this.lowerRender.x = this.bottomRender.x + Mth.sin(f) * 0.5F * 0.8F * amountModif;
        this.lowerRender.z = this.bottomRender.z + Mth.cos(f) * 0.8F * amountModif;

        this.upperRender.x = this.lowerRender.x + Mth.cos(f) * 1.0F * 0.6F * amountModif;
        this.upperRender.z = this.lowerRender.z + Mth.sin(f) * 1.0F * 0.6F * amountModif;

        this.topRender.x = this.topRender.x + Mth.sin(f) * 0.5F * 0.4F * amountModif;
        this.topRender.z = this.topRender.z + Mth.cos(f) * 0.4F * amountModif;
    }

    @Override
    public void renderToBuffer(com.mojang.blaze3d.vertex.PoseStack poseStack, com.mojang.blaze3d.vertex.VertexConsumer consumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bottomRender.render(poseStack, consumer, packedLight, packedOverlay, red, green, blue, this.alpha[0]);
        this.lowerRender.render(poseStack, consumer, packedLight, packedOverlay, red, green, blue, this.alpha[1]);
        this.upperRender.render(poseStack, consumer, packedLight, packedOverlay, red, green, blue, this.alpha[2]);
        this.topRender.render(poseStack, consumer, packedLight, packedOverlay, red, green, blue, this.alpha[3]);
        this.alpha[0] = 1.0F;
        this.alpha[1] = 1.0F;
        this.alpha[2] = 1.0F;
        this.alpha[3] = 1.0F;
    }

    private static float ease(float t) {
        return (float) (0.5D - 0.5D * Math.cos(t * Math.PI));
    }
}
