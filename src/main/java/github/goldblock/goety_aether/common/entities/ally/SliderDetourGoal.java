package github.goldblock.goety_aether.common.entities.ally;

import com.aetherteam.aether.entity.monster.dungeon.boss.Slider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class SliderDetourGoal extends Goal {
    private static final double IDLE_SPEED_SQR = 1.0E-4D;
    private static final double CLOSE_ENOUGH_SQR = 6.25D;
    private static final int WAYPOINT_DISTANCE = 4;
    private static final int PROBE_DISTANCE = 4;
    private final Slider slider;
    private int trackedTargetId = -1;
    @Nullable
    private Direction blockedDirection;
    @Nullable
    private Direction detourDirection;

    public SliderDetourGoal(Slider pSlider) {
        this.slider = pSlider;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.slider.getTarget();
        int targetId = target == null ? -1 : target.getId();
        if (targetId != this.trackedTargetId) {
            this.trackedTargetId = targetId;
            this.blockedDirection = null;
            this.detourDirection = null;
        }
        if (this.slider.isPassenger() || this.slider.getTargetPoint() != null) {
            return false;
        }
        if (this.slider.getDeltaMovement().horizontalDistanceSqr() > IDLE_SPEED_SQR) {
            return false;
        }
        Vec3 targetPoint = this.slider.findTargetPoint();
        if (targetPoint == null) {
            return false;
        }
        Direction blocked = this.blockedDirection;
        if (blocked == null) {
            blocked = this.getDirectionToward(targetPoint);
            if (blocked == null || blocked.getAxis() == Direction.Axis.Y) {
                return false;
            }
        }
        if (!this.isBlocked(blocked)) {
            this.blockedDirection = null;
            this.detourDirection = null;
            return false;
        }
        double x = targetPoint.x() - this.slider.getX();
        double z = targetPoint.z() - this.slider.getZ();
        return x * x + z * z >= CLOSE_ENOUGH_SQR;
    }

    @Override
    public void start() {
        Vec3 targetPoint = this.slider.findTargetPoint();
        if (targetPoint == null) {
            return;
        }
        if (this.blockedDirection == null) {
            Direction blocked = this.getDirectionToward(targetPoint);
            if (blocked == null || blocked.getAxis() == Direction.Axis.Y) {
                return;
            }
            this.blockedDirection = blocked;
            this.detourDirection = null;
        }
        Direction detour = this.getDetourDirection(this.blockedDirection);
        if (detour == null) {
            this.blockedDirection = null;
            this.detourDirection = null;
            return;
        }
        this.detourDirection = detour;
        Vec3 pos = this.slider.position();
        this.slider.setMoveDirection(detour);
        this.slider.setTargetPoint(new Vec3(pos.x() + detour.getStepX() * WAYPOINT_DISTANCE, pos.y(), pos.z() + detour.getStepZ() * WAYPOINT_DISTANCE));
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Nullable
    private Direction getDirectionToward(Vec3 targetPoint) {
        return Slider.calculateDirection(targetPoint.x() - this.slider.getX(), targetPoint.y() - this.slider.getY(), targetPoint.z() - this.slider.getZ());
    }

    @Nullable
    private Direction getDetourDirection(Direction blocked) {
        Direction clockwise = blocked.getClockWise();
        Direction counterClockwise = blocked.getCounterClockWise();
        int clockwiseDistance = this.getFreeDistance(clockwise);
        int counterClockwiseDistance = this.getFreeDistance(counterClockwise);
        if (clockwiseDistance > 0 && counterClockwiseDistance > 0) {
            if (clockwise == this.detourDirection) {
                return clockwise;
            }
            if (counterClockwise == this.detourDirection) {
                return counterClockwise;
            }
            return clockwiseDistance >= counterClockwiseDistance ? clockwise : counterClockwise;
        }
        if (clockwiseDistance > 0) {
            return clockwise;
        }
        if (counterClockwiseDistance > 0) {
            return counterClockwise;
        }
        Direction backward = blocked.getOpposite();
        return this.getFreeDistance(backward) > 0 ? backward : null;
    }

    private int getFreeDistance(Direction direction) {
        AABB box = this.slider.getBoundingBox();
        int distance = 0;
        for (int i = 0; i < PROBE_DISTANCE; i++) {
            box = Slider.calculateAdjacentBox(box, direction);
            if (this.isBlocked(box)) {
                break;
            }
            distance++;
        }
        return distance;
    }

    private boolean isBlocked(Direction direction) {
        return this.isBlocked(Slider.calculateAdjacentBox(this.slider.getBoundingBox(), direction));
    }

    private boolean isBlocked(AABB box) {
        BlockPos min = new BlockPos(Mth.floor(box.minX), Mth.floor(box.minY), Mth.floor(box.minZ));
        BlockPos max = new BlockPos(Mth.ceil(box.maxX - 1.0D), Mth.ceil(box.maxY - 1.0D), Mth.ceil(box.maxZ - 1.0D));
        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            if (!this.slider.level().getBlockState(pos).getCollisionShape(this.slider.level(), pos).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
