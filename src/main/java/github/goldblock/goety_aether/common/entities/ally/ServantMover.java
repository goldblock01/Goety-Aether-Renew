package github.goldblock.goety_aether.common.entities.ally;

import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public interface ServantMover {
    @Nullable
    Vec3 getServantMoveTarget();

    int servantJumpDelay();

    void playServantJumpSound();
}
