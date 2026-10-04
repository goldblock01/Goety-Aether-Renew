package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.common.entities.ai.FloatAroundGoal;
import com.Polarice3.Goety.common.entities.neutral.SummonedFlying;
import com.Polarice3.Goety.utils.MobUtil;
import com.aetherteam.aether.client.AetherSoundEvents;
import com.aetherteam.aether.entity.projectile.ZephyrSnowball;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class ZephyrServant extends SummonedFlying {
    private static final EntityDataAccessor<Integer> DATA_CHARGE_TIME_ID = SynchedEntityData.defineId(ZephyrServant.class, EntityDataSerializers.INT);

    private int cloudScale;
    private int cloudScaleAdd;
    private float tailRot;
    private float tailRotAdd;

    public ZephyrServant(EntityType<? extends ZephyrServant> type, Level level) {
        super(type, level);
        this.moveControl = new ZephyrMoveHelper(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CHARGE_TIME_ID, 0);
    }

    @Override
    public void followGoal() {
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new FloatAroundGoal<>(this, 16.0F, 4, 0.25D));
        this.goalSelector.addGoal(7, new ZephyrLookGoal(this));
        this.registerMeleeGoal();
        this.registerShootGoal();
    }

    protected void registerMeleeGoal() {
    }

    protected void registerShootGoal() {
        this.goalSelector.addGoal(7, new ZephyrShootGoal(this));
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            } else {
                BlockPos ground = this.getBlockPosBelowThatAffectsMyMovement();
                float f = 0.91F;
                if (this.onGround()) {
                    f = this.level().getBlockState(ground).getFriction(this.level(), ground, this) * 0.91F;
                }
                float f1 = 0.16277137F / (f * f * f);
                f = 0.91F;
                if (this.onGround()) {
                    f = this.level().getBlockState(ground).getFriction(this.level(), ground, this) * 0.91F;
                }
                this.moveRelative(this.onGround() ? 0.1F * f1 : 0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(f));
            }
        }
        this.calculateEntityAnimation(false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.cloudScale += this.cloudScaleAdd;
            this.tailRot += this.tailRotAdd;
            if (this.getChargeTime() < 20 && this.getChargeTime() > 0) {
                this.cloudScaleAdd = 1;
            } else {
                this.cloudScaleAdd = 0;
                this.cloudScale = 0;
            }
            this.tailRotAdd = 0.015F;
            if (this.tailRot >= 6.2831855F) {
                this.tailRot -= 6.2831855F;
            }
        }
        if (!this.level().isClientSide() && this.isStaying()) {
            this.getMoveControl().strafe(0.0F, 0.0F);
        }
    }

    public int getChargeTime() {
        return this.entityData.get(DATA_CHARGE_TIME_ID);
    }

    public void setChargeTime(int chargeTime) {
        this.entityData.set(DATA_CHARGE_TIME_ID, chargeTime);
    }

    public int getCloudScale() {
        return this.cloudScale;
    }

    public int getCloudScaleAdd() {
        return this.cloudScaleAdd;
    }

    public float getTailRot() {
        return this.tailRot;
    }

    public float getTailRotAdd() {
        return this.tailRotAdd;
    }

    @Override
    public void push(Entity entity) {
        if (!this.level().isClientSide() && !MobUtil.areAllies(this, entity)) {
            super.push(entity);
        }
    }

    @Override
    protected void doPush(Entity entity) {
        if (!this.level().isClientSide() && !MobUtil.areAllies(this, entity)) {
            super.doPush(entity);
        }
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return !MobUtil.areAllies(this, entity) && super.canCollideWith(entity);
    }

    @Override
    public boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return AetherSoundEvents.ENTITY_ZEPHYR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AetherSoundEvents.ENTITY_ZEPHYR_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 3.0F;
    }

    static class ZephyrMoveHelper extends MoveControl {
        private final ZephyrServant zephyr;
        private int floatDuration;

        ZephyrMoveHelper(ZephyrServant zephyr) {
            super(zephyr);
            this.zephyr = zephyr;
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration += this.zephyr.getRandom().nextInt(5) + 2;
                    Vec3 vec3 = new Vec3(this.wantedX - this.zephyr.getX(), this.wantedY - this.zephyr.getY(), this.wantedZ - this.zephyr.getZ());
                    double d0 = vec3.length();
                    vec3 = vec3.normalize();
                    if (this.canReach(vec3, Mth.ceil(d0))) {
                        this.zephyr.setDeltaMovement(this.zephyr.getDeltaMovement().add(vec3.scale(0.1D)));
                    } else {
                        this.operation = MoveControl.Operation.WAIT;
                    }
                }
            }
        }

        private boolean canReach(Vec3 pos, int distance) {
            AABB aabb = this.zephyr.getBoundingBox();
            for (int i = 1; i < distance; ++i) {
                aabb = aabb.move(pos);
                if (!this.zephyr.level().noCollision(this.zephyr, aabb)) {
                    return false;
                }
            }
            return true;
        }
    }

    static class ZephyrLookGoal extends Goal {
        private final ZephyrServant zephyr;

        ZephyrLookGoal(ZephyrServant zephyr) {
            this.zephyr = zephyr;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            if (this.zephyr.getTarget() == null) {
                Vec3 vec3 = this.zephyr.getDeltaMovement();
                this.zephyr.setYRot(-((float) Mth.atan2(vec3.x, vec3.z)) * 57.295776F);
                this.zephyr.yBodyRot = this.zephyr.getYRot();
            } else {
                LivingEntity target = this.zephyr.getTarget();
                if (target.distanceToSqr(this.zephyr) < 4096.0D) {
                    double x = target.getX() - this.zephyr.getX();
                    double z = target.getZ() - this.zephyr.getZ();
                    this.zephyr.setYRot(-((float) Mth.atan2(x, z)) * 57.295776F);
                    this.zephyr.setYHeadRot(this.zephyr.getYRot());
                }
            }
        }
    }

    static class ZephyrShootGoal extends Goal {
        private final ZephyrServant zephyr;

        ZephyrShootGoal(ZephyrServant zephyr) {
            this.zephyr = zephyr;
        }

        @Override
        public boolean canUse() {
            return this.zephyr.getTarget() != null;
        }

        @Override
        public void start() {
            this.zephyr.setChargeTime(0);
        }

        @Override
        public void stop() {
            this.zephyr.setChargeTime(0);
        }

        @Override
        public void tick() {
            LivingEntity target = this.zephyr.getTarget();
            if (target != null) {
                if (target.distanceToSqr(this.zephyr) < 1600.0D && this.zephyr.hasLineOfSight(target)) {
                    Level level = this.zephyr.level();
                    this.zephyr.setChargeTime(this.zephyr.getChargeTime() + 1);
                    if (this.zephyr.getChargeTime() == 10) {
                        this.zephyr.playSound(AetherSoundEvents.ENTITY_ZEPHYR_AMBIENT.get(), 3.0F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F);
                    } else if (this.zephyr.getChargeTime() == 20) {
                        Vec3 look = this.zephyr.getViewVector(1.0F);
                        double accelX = target.getX() - (this.zephyr.getX() + look.x() * 4.0D);
                        double accelY = target.getY(0.5D) - (0.5D + this.zephyr.getY(0.5D));
                        double accelZ = target.getZ() - (this.zephyr.getZ() + look.z() * 4.0D);
                        this.zephyr.playSound(AetherSoundEvents.ENTITY_ZEPHYR_SHOOT.get(), 3.0F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F);
                        ZephyrSnowball snowball = new ZephyrSnowball(level, this.zephyr, accelX, accelY, accelZ);
                        snowball.setPos(this.zephyr.getX() + look.x() * 4.0D, this.zephyr.getY(0.5D) + 0.5D, this.zephyr.getZ() + look.z() * 4.0D);
                        level.addFreshEntity(snowball);
                        this.zephyr.setChargeTime(-40);
                    }
                } else if (this.zephyr.getChargeTime() > 0) {
                    this.zephyr.setChargeTime(this.zephyr.getChargeTime() - 1);
                }
            }
        }
    }
}
