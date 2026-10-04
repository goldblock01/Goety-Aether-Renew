package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import github.goldblock.goety_aether.compat.genesis.GenesisBridge;
import github.goldblock.goety_aether.common.entities.ally.SentryGuardianSummonGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.Level;

public class SentryGuardian extends Summoned {
    private static final int ATTACK_TIME = 10;
    protected static final EntityDataAccessor<Boolean> SEEN_ENEMY = SynchedEntityData.defineId(SentryGuardian.class, EntityDataSerializers.BOOLEAN);

    private int attackTime;

    public SentryGuardian(EntityType<? extends Owned> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return net.minecraft.world.entity.monster.Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 250.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.FOLLOW_RANGE, 8.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 2.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SEEN_ENEMY, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new SentryGuardianSummonGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.attackTime > 0) {
            --this.attackTime;
        }
        if (!this.level().isClientSide()) {
            this.setSeenEnemy(this.getTarget() != null);
        }
        if (this.getDeltaMovement().y < -0.24D) {
            this.setDeltaMovement(this.getDeltaMovement().x, -0.24D, this.getDeltaMovement().z);
            this.hasImpulse = true;
        }
    }

    public int getAttackAnimationTick() {
        return this.attackTime;
    }

    public boolean getSeenEnemy() {
        return this.entityData.get(SEEN_ENEMY);
    }

    public void setSeenEnemy(boolean seen) {
        this.entityData.set(SEEN_ENEMY, seen);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        this.attackTime = ATTACK_TIME;
        this.level().broadcastEntityEvent(this, (byte) 4);
        return super.doHurtTarget(pEntity);
    }

    @Override
    protected float getJumpPower() {
        return 0.0F;
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 4) {
            this.attackTime = ATTACK_TIME;
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        } else {
            super.handleEntityEvent(pId);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return GenesisBridge.sound("entity.sentry_guardian.living");
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return GenesisBridge.sound("entity.sentry_guardian.hit");
    }

    @Override
    protected SoundEvent getDeathSound() {
        return GenesisBridge.sound("entity.sentry_guardian.death");
    }
}
