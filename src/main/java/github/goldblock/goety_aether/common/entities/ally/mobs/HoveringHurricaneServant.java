package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.Goety.utils.MobUtil;
import github.goldblock.goety_aether.compat.legendary_monsters.LegendaryMonstersBridge;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import java.util.EnumSet;
import java.util.List;

public class HoveringHurricaneServant extends Summoned {
    private static final EntityDataAccessor<Byte> DATA_ATTACK_STATE = SynchedEntityData.defineId(HoveringHurricaneServant.class, EntityDataSerializers.BYTE);
    private static final byte STATE_NONE = 0;
    private static final byte STATE_SLAM = 1;
    private static final byte STATE_SHOOT = 2;

    private int attackTicks;
    private int shootCooldown;
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState slamAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();

    public HoveringHurricaneServant(EntityType<? extends Owned> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ATTACK_STATE, STATE_NONE);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.ARMOR, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    public double getFollowSpeed() {
        return 3.0D;
    }

    @Override
    public double getCommandSpeed() {
        return 3.0D;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SlamGoal(this));
        this.goalSelector.addGoal(1, new ShootGoal(this));
        this.goalSelector.addGoal(2, new HurricaneMoveGoal(this, 3.0D));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            byte state = this.getAttackState();
            this.idleAnimationState.animateWhen(state == STATE_NONE, this.tickCount);
            this.level().addParticle(ParticleTypes.CLOUD, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.025D, 0.0D);
        } else {
            if (this.getAttackState() != STATE_NONE) {
                ++this.attackTicks;
                if (this.getAttackState() == STATE_SLAM && this.attackTicks == 7) {
                    this.playSound(LegendaryMonstersBridge.sound("posessed_paladin_swing"), 1.0F, 2.0F);
                } else if (this.getAttackState() == STATE_SLAM && this.attackTicks == 15) {
                    this.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE, 2.0F, 1.0F);
                    this.areaAttack(4.5F, 3.0F, 180.0F, 13.0F);
                } else if (this.getAttackState() == STATE_SHOOT && this.attackTicks == 13) {
                    this.playSound(SoundEvents.WITHER_SHOOT, 1.0F, 1.0F);
                    this.shootTornado();
                }
            } else {
                this.attackTicks = 0;
            }
            if (this.shootCooldown > 0) {
                --this.shootCooldown;
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_ATTACK_STATE.equals(accessor) && this.level().isClientSide) {
            this.stopAllAnimationStates();
            byte state = this.getAttackState();
            if (state == STATE_NONE) {
                this.idleAnimationState.startIfStopped(this.tickCount);
            } else if (state == STATE_SLAM) {
                this.slamAnimationState.startIfStopped(this.tickCount);
            } else if (state == STATE_SHOOT) {
                this.shootAnimationState.startIfStopped(this.tickCount);
            }
        }
        super.onSyncedDataUpdated(accessor);
    }

    private void stopAllAnimationStates() {
        this.idleAnimationState.stop();
        this.slamAnimationState.stop();
        this.shootAnimationState.stop();
    }

    private void shootTornado() {
        float damage = 10.0F;
        LivingEntity target = this.getTarget();
        if (target != null) {
            damage += (float) (target.getMaxHealth() * 0.05D);
        }
        LegendaryMonstersBridge.spawnTornado(this, this.yBodyRot, damage);
    }

    public int getAttackTicks() {
        return this.attackTicks;
    }

    public byte getAttackState() {
        return this.entityData.get(DATA_ATTACK_STATE);
    }

    public void setAttackState(byte state) {
        this.entityData.set(DATA_ATTACK_STATE, state);
    }

    public AnimationState getIdleAnimationState() {
        return this.idleAnimationState;
    }

    public AnimationState getSlamAnimationState() {
        return this.slamAnimationState;
    }

    public AnimationState getShootAnimationState() {
        return this.shootAnimationState;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return LegendaryMonstersBridge.sound("cloud_golem_ambient");
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return LegendaryMonstersBridge.sound("cloud_golem_hurt");
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LegendaryMonstersBridge.sound("cloud_golem_death");
    }

    private void areaAttack(float range, float height, float arc, float damage) {
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(range, height, range));
        float attackingAngle = this.yBodyRot % 360.0F;
        if (attackingAngle < 0.0F) {
            attackingAngle += 360.0F;
        }
        for (LivingEntity target : entities) {
            if (target == this || !target.isAlive() || MobUtil.areAllies(this, target) || this.isAlliedTo(target)) {
                continue;
            }
            float hitAngle = (float) ((Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX()) * (180.0D / Math.PI) - 90.0D) % 360.0D);
            if (hitAngle < 0.0F) {
                hitAngle += 360.0F;
            }
            float relativeAngle = hitAngle - attackingAngle;
            float hitDistance = (float) Math.sqrt((target.getZ() - this.getZ()) * (target.getZ() - this.getZ())
                    + (target.getX() - this.getX()) * (target.getX() - this.getX()));
            if (hitDistance <= range && ((relativeAngle <= arc / 2.0F && relativeAngle >= -arc / 2.0F)
                    || relativeAngle >= 360.0F - arc / 2.0F || relativeAngle <= -360.0F + arc / 2.0F)) {
                target.hurt(this.damageSources().mobAttack(this), damage);
            }
        }
    }

    static class HurricaneMoveGoal extends Goal {
        private final HoveringHurricaneServant hurricane;
        private final double speedModifier;
        private int delayCounter;

        HurricaneMoveGoal(HoveringHurricaneServant hurricane, double speedModifier) {
            this.hurricane = hurricane;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.hurricane.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = this.hurricane.getTarget();
            return target != null && target.isAlive() && !this.hurricane.getNavigation().isDone();
        }

        @Override
        public void stop() {
            this.hurricane.getNavigation().stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.hurricane.getTarget();
            if (target == null) {
                return;
            }
            this.hurricane.getLookControl().setLookAt(target, 30.0F, 30.0F);
            double distSq = this.hurricane.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());
            if (--this.delayCounter <= 0) {
                this.delayCounter = 4 + this.hurricane.getRandom().nextInt(7);
                PathNavigation navigation = this.hurricane.getNavigation();
                if (distSq > Math.pow(this.hurricane.getAttributeValue(Attributes.FOLLOW_RANGE), 2.0D)) {
                    if (!this.hurricane.isPathFinding() && !navigation.moveTo(target, 1.0D)) {
                        this.delayCounter += 5;
                    }
                } else {
                    navigation.moveTo(target, this.speedModifier);
                }
            }
        }
    }

    static class SlamGoal extends Goal {
        private final HoveringHurricaneServant hurricane;

        SlamGoal(HoveringHurricaneServant hurricane) {
            this.hurricane = hurricane;
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.hurricane.getTarget();
            return target != null && target.isAlive()
                    && this.hurricane.distanceTo(target) < 3.5F
                    && this.hurricane.getAttackState() == HoveringHurricaneServant.STATE_NONE
                    && this.hurricane.getRandom().nextFloat() * 40.0F < 16.0F;
        }

        @Override
        public void start() {
            this.hurricane.setAttackState(HoveringHurricaneServant.STATE_SLAM);
        }

        @Override
        public void stop() {
            this.hurricane.setAttackState(HoveringHurricaneServant.STATE_NONE);
        }

        @Override
        public boolean canContinueToUse() {
            return this.hurricane.getAttackTicks() < 40;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.hurricane.getTarget();
            if (target != null) {
                this.hurricane.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
        }
    }

    static class ShootGoal extends Goal {
        private final HoveringHurricaneServant hurricane;

        ShootGoal(HoveringHurricaneServant hurricane) {
            this.hurricane = hurricane;
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.hurricane.getTarget();
            return target != null && target.isAlive()
                    && this.hurricane.distanceTo(target) < 15.0F
                    && this.hurricane.getAttackState() == HoveringHurricaneServant.STATE_NONE
                    && this.hurricane.shootCooldown <= 0
                    && this.hurricane.getRandom().nextFloat() * 40.0F < 16.0F;
        }

        @Override
        public void start() {
            this.hurricane.setAttackState(HoveringHurricaneServant.STATE_SHOOT);
        }

        @Override
        public void stop() {
            this.hurricane.setAttackState(HoveringHurricaneServant.STATE_NONE);
            this.hurricane.shootCooldown = 40;
        }

        @Override
        public boolean canContinueToUse() {
            return this.hurricane.getAttackTicks() < 45;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.hurricane.getTarget();
            if (target != null) {
                this.hurricane.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
        }
    }
}
