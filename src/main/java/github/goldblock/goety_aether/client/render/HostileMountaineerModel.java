package github.goldblock.goety_aether.client.render;

import com.Polarice3.Goety.client.render.model.MountaineerModel;
import github.goldblock.goety_aether.common.entities.hostile.illagers.Mountaineer;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.geom.ModelPart;

public class HostileMountaineerModel extends MountaineerModel<Mountaineer> {

    public HostileMountaineerModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(Mountaineer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.arms.visible = false;
        if (entity.isAggressive()) {
            AnimationUtils.swingWeaponDown(this.RightArm, this.LeftArm, entity, this.attackTime, ageInTicks);
        }
    }
}
