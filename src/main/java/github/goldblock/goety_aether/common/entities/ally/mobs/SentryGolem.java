package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import github.goldblock.goety_aether.compat.genesis.GenesisBridge;
import github.goldblock.goety_aether.common.entities.projectile.SentryBomb;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SentryGolem extends Summoned implements RangedAttackMob {
    private static final EntityDataAccessor<Byte> DATA_HAND_STATE = SynchedEntityData.defineId(SentryGolem.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> DATA_FIRE_TIME = SynchedEntityData.defineId(SentryGolem.class, EntityDataSerializers.INT);
    public static final byte HAND_STATE_RAISED = 1;
    public static final byte HAND_STATE_LOWERED = 2;
    public float progress = 0.0F;

    public SentryGolem(EntityType<? extends Owned> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.5D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.FOLLOW_RANGE, 50.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HAND_STATE, HAND_STATE_LOWERED);
        this.entityData.define(DATA_FIRE_TIME, 60);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ThrowBombGoal(this));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distance) {
        SentryBomb bomb = new SentryBomb(ModEntityTypes.SENTRY_BOMB.get(), this, this.level());
        double dx = target.getX() - this.getX();
        double dy = target.getEyeY() - this.getY() - 1.0D;
        double dz = target.getZ() - this.getZ();
        double flat = Math.sqrt(dx * dx + dz * dz);
        bomb.shoot(dx, dy + flat * 0.2D, dz, 0.5F, 8.0F);
        bomb.setYRot(this.yBodyRot);
        this.playSound(GenesisBridge.sound("entity.tracking_golem.hit"), 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(bomb);
    }

    public byte getHandState() {
        return this.entityData.get(DATA_HAND_STATE);
    }

    public void setHandState(byte state) {
        this.entityData.set(DATA_HAND_STATE, state);
    }

    public int getFireTime() {
        return this.entityData.get(DATA_FIRE_TIME);
    }

    public void setFireTime(int time) {
        this.entityData.set(DATA_FIRE_TIME, time);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return GenesisBridge.sound("entity.tracking_golem.say");
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return GenesisBridge.sound("entity.tracking_golem.hit");
    }

    @Override
    protected SoundEvent getDeathSound() {
        return GenesisBridge.sound("entity.tracking_golem.death");
    }

    public static class ThrowBombGoal extends Goal {
        private static final int ATTACK_INTERVAL = 60;
        private static final int WINDUP_TIME = 30;
        private static final float ATTACK_RADIUS = 10.0F;
        private final SentryGolem mob;
        @Nullable
        private LivingEntity target;
        private int attackTime = -1;
        private int seeTime;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;

        public ThrowBombGoal(SentryGolem mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.mob.getTarget();
            if (target != null && target.isAlive()) {
                this.target = target;
                return true;
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse() || (this.target != null && this.target.isAlive() && !this.mob.getNavigation().isDone());
        }

        @Override
        public void stop() {
            this.target = null;
            this.seeTime = 0;
            this.attackTime = -1;
            this.strafingTime = -1;
            this.mob.setHandState(HAND_STATE_LOWERED);
            this.mob.setFireTime(ATTACK_INTERVAL);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            double distance = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
            boolean canSee = this.mob.getSensing().hasLineOfSight(this.target);
            boolean seenBefore = this.seeTime > 0;
            if (canSee != seenBefore) {
                this.seeTime = 0;
            }
            if (canSee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(distance > (double) (ATTACK_RADIUS * ATTACK_RADIUS)) && this.seeTime >= 20) {
                this.mob.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.mob.getNavigation().moveTo(this.target, 1.0D);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double) this.mob.getRandom().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }
                if ((double) this.mob.getRandom().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (distance > (double) (ATTACK_RADIUS * ATTACK_RADIUS * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (distance < (double) (ATTACK_RADIUS * ATTACK_RADIUS * 0.25F)) {
                    this.strafingBackwards = true;
                }
                this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.mob.lookAt(this.target, 30.0F, 30.0F);
            } else {
                this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            }

            this.attackTime = Math.max(this.attackTime - 1, 0);
            this.mob.setFireTime(this.attackTime);
            if (this.attackTime <= WINDUP_TIME) {
                this.mob.setHandState(HAND_STATE_RAISED);
            }

            if (this.attackTime <= 0 && this.seeTime >= -60) {
                this.mob.performRangedAttack(this.target, 1.0F);
                this.attackTime = ATTACK_INTERVAL;
                this.mob.setHandState(HAND_STATE_LOWERED);
            }
        }
    }
}
