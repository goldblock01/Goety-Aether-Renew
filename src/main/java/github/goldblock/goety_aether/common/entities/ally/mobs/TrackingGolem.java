package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.aetherteam.aether.entity.ai.goal.ContinuousMeleeAttackGoal;
import github.goldblock.goety_aether.compat.genesis.GenesisBridge;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class TrackingGolem extends Summoned {
    protected static final EntityDataAccessor<Boolean> SEEN_ENEMY = SynchedEntityData.defineId(TrackingGolem.class, EntityDataSerializers.BOOLEAN);

    public TrackingGolem(EntityType<? extends Owned> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return net.minecraft.world.entity.monster.Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.FOLLOW_RANGE, 8.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SEEN_ENEMY, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ContinuousMeleeAttackGoal(this, 1.0D, false));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            this.updateSeenEnemy();
        }
    }

    protected void updateSeenEnemy() {
        LivingEntity target = this.getTarget();
        boolean seen = target != null;
        if (seen) {
            if (!this.getSeenEnemy()) {
                this.level().playSound(null, this.blockPosition(), GenesisBridge.sound("entity.tracking_golem.seen_enemy"), SoundSource.AMBIENT, 5.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }
            target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 10, 3));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3));
        }
        this.setSeenEnemy(seen);
    }

    public boolean getSeenEnemy() {
        return this.entityData.get(SEEN_ENEMY);
    }

    public void setSeenEnemy(boolean seen) {
        this.entityData.set(SEEN_ENEMY, seen);
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
}
