package github.goldblock.goety_aether.common.entities.ally;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.phys.Vec3;

public class SlimeServantMoveControl<T extends Slime & ServantMover> extends MoveControl {
    private static final double MOVE_SPEED = 1.0D;
    private static final double CLOSE_ENOUGH = 2.5E-7D;
    private static final float TURN_SPEED = 90.0F;
    private final T servant;
    private int jumpDelay;

    public SlimeServantMoveControl(T pServant) {
        super(pServant);
        this.servant = pServant;
    }

    @Override
    public void tick() {
        Vec3 dest = this.servant.getServantMoveTarget();
        if (dest == null) {
            this.operation = Operation.WAIT;
            this.mob.setSpeed(0.0F);
            return;
        }
        double x = dest.x - this.mob.getX();
        double z = dest.z - this.mob.getZ();
        if (x * x + z * z < CLOSE_ENOUGH) {
            this.operation = Operation.WAIT;
            this.mob.setSpeed(0.0F);
            return;
        }
        float degrees = (float) (Mth.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        this.mob.setYRot(this.rotlerp(this.mob.getYRot(), degrees, TURN_SPEED));
        this.mob.yHeadRot = this.mob.getYRot();
        this.mob.yBodyRot = this.mob.getYRot();
        float speed = (float) (MOVE_SPEED * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
        if (this.mob.onGround()) {
            if (this.jumpDelay-- <= 0) {
                this.jumpDelay = this.servant.servantJumpDelay();
                if (this.mob.getTarget() != null) {
                    this.jumpDelay /= 3;
                }
                this.mob.getJumpControl().jump();
                this.servant.playServantJumpSound();
                this.mob.setSpeed(speed);
            } else {
                this.mob.setSpeed(0.0F);
            }
        } else {
            this.mob.setSpeed(speed);
        }
    }
}
