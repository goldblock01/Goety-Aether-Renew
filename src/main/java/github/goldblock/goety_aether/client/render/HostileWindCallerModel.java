package github.goldblock.goety_aether.client.render;

import com.Polarice3.Goety.client.render.model.WindCallerModel;
import github.goldblock.goety_aether.common.entities.hostile.illagers.WindCaller;
import net.minecraft.client.model.geom.ModelPart;

public class HostileWindCallerModel extends WindCallerModel<WindCaller> {
    public HostileWindCallerModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(WindCaller entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.animate(entity.idleAnimationState, WindCallerModel.IDLE, ageInTicks);
        this.animate(entity.blastAnimationState, WindCallerModel.BLAST, ageInTicks);
        this.animate(entity.updraftAnimationState, WindCallerModel.UPDRAFT, ageInTicks);
        if (!entity.isAttacking()) {
            this.animateWalk(WindCallerModel.MOVE, limbSwing, limbSwingAmount, 2.5F, 20.0F);
        }
    }
}
