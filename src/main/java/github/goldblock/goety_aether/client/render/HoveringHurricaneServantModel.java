package github.goldblock.goety_aether.client.render;

import github.goldblock.goety_aether.common.entities.ally.mobs.HoveringHurricaneServant;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class HoveringHurricaneServantModel extends HierarchicalModel<HoveringHurricaneServant> {
    private final ModelPart root;
    private final ModelPart head;

    public HoveringHurricaneServantModel(ModelPart model) {
        this.root = model.getChild("root");
        this.head = this.root.getChild("body").getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition root = partDefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));
        PartDefinition leftarm = body.addOrReplaceChild("leftarm", CubeListBuilder.create()
                .texOffs(64, 0).addBox(-2.5F, -3.0F, -2.5F, 5.0F, 14.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(72, 38).addBox(-2.5F, -3.0F, -2.5F, 5.0F, 14.0F, 5.0F, new CubeDeformation(0.3F)), PartPose.offset(12.0F, -16.0F, 0.0F));
        leftarm.addOrReplaceChild("lower", CubeListBuilder.create()
                .texOffs(32, 0).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 18).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 13.0F, 0.0F));
        PartDefinition rightarm = body.addOrReplaceChild("rightarm", CubeListBuilder.create()
                .texOffs(64, 19).addBox(-2.5F, -3.0F, -2.5F, 5.0F, 14.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 68).addBox(-2.5F, -3.0F, -2.5F, 5.0F, 14.0F, 5.0F, new CubeDeformation(0.3F)), PartPose.offset(-12.0F, -16.0F, 0.0F));
        rightarm.addOrReplaceChild("lower2", CubeListBuilder.create()
                .texOffs(32, 36).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 50).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 13.0F, 0.0F));
        body.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 17.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 25).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 17.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, -19.0F, 0.0F));
        body.addOrReplaceChild("rod", CubeListBuilder.create()
                .texOffs(32, 54).addBox(-2.5F, -11.5F, -2.5F, 5.0F, 23.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(52, 54).addBox(-2.5F, -11.5F, -2.5F, 5.0F, 23.0F, 5.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, -5.5F, 0.0F));
        root.addOrReplaceChild("bone", CubeListBuilder.create()
                .texOffs(20, 68).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(72, 57).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(3.5F, 6.0F, -7.5F));
        root.addOrReplaceChild("bone2", CubeListBuilder.create()
                .texOffs(72, 72).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(32, 82).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(6.5F, -8.0F, 7.5F));
        root.addOrReplaceChild("bone3", CubeListBuilder.create()
                .texOffs(44, 82).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(56, 82).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(-5.5F, -4.0F, -2.5F));
        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    @Override
    public void setupAnim(HoveringHurricaneServant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);
        this.animateWalk(HoveringHurricaneServantAnimations.walk, limbSwing, limbSwingAmount, 1.5F, 4.0F);
        this.animate(entity.getIdleAnimationState(), HoveringHurricaneServantAnimations.idle, ageInTicks, 1.0F);
        this.animate(entity.getSlamAnimationState(), HoveringHurricaneServantAnimations.slam, ageInTicks, 1.0F);
        this.animate(entity.getShootAnimationState(), HoveringHurricaneServantAnimations.tornadoShoot, ageInTicks, 1.0F);
    }

    private void applyHeadRotation(float netHeadYaw, float headPitch) {
        netHeadYaw = Mth.clamp(netHeadYaw, -30.0F, 30.0F);
        headPitch = Mth.clamp(headPitch, -25.0F, 25.0F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
