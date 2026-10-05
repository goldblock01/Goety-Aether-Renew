package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.aetherteam.aether.client.AetherSoundEvents;
import com.aetherteam.aether.entity.EntityUtil;
import com.aetherteam.aether.entity.ai.goal.ContinuousMeleeAttackGoal;
import com.aetherteam.aether.entity.ai.goal.FallingRandomStrollGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public class BlightbunnyServant extends Summoned {
    private static final EntityDataAccessor<Integer> DATA_PUFFINESS_ID = SynchedEntityData.defineId(BlightbunnyServant.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_FAST_FALLING_ID = SynchedEntityData.defineId(BlightbunnyServant.class, EntityDataSerializers.BOOLEAN);

    private static final int MAXIMUM_PUFFS = 11;

    private int puffSubtract;

    public BlightbunnyServant(EntityType<? extends Summoned> type, Level level) {
        super(type, level);
        this.xpReward = 0;
        this.moveControl = new BlightbunnyServantMoveControl(this);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ContinuousMeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new FallingRandomStrollGoal(this, 1.0D, 80));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PUFFINESS_ID, 0);
        this.entityData.define(DATA_FAST_FALLING_ID, false);
    }

    @Override
    public void tick() {
        super.tick();
        this.resetFallDistance();
        if (!this.isFastFalling()) {
            this.handleFallSpeed();
        } else if (this.onGround()) {
            this.setFastFalling(false);
        }

        this.setPuffiness(this.getPuffiness() - this.puffSubtract);
        if (this.getPuffiness() > 0) {
            this.puffSubtract = 1;
        } else {
            this.puffSubtract = 0;
            this.setPuffiness(0);
        }
    }

    protected void midairJump() {
        if (this.getTarget() == null) {
            Vec3 motion = this.getDeltaMovement();
            if (motion.y() < 0.0D) {
                this.puff();
                this.level().broadcastEntityEvent(this, (byte) 70);
            }

            this.setDeltaMovement(new Vec3(motion.x(), 0.25D, motion.z()));
        }
    }

    public void puff() {
        if (this.level() instanceof ServerLevel) {
            this.setPuffiness(MAXIMUM_PUFFS);
        }
    }

    private void handleFallSpeed() {
        AttributeInstance gravity = this.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
        if (gravity != null) {
            double fallSpeed = Math.max(gravity.getValue() * -1.25D, -0.1D);
            if (this.getDeltaMovement().y() < fallSpeed) {
                this.setDeltaMovement(this.getDeltaMovement().x(), fallSpeed, this.getDeltaMovement().z());
            }
        }
    }

    private void spawnExplosionParticle() {
        for (int i = 0; i < 5; i++) {
            EntityUtil.spawnMovementExplosionParticles(this);
        }
    }

    public int getPuffiness() {
        return this.entityData.get(DATA_PUFFINESS_ID);
    }

    public void setPuffiness(int puffiness) {
        this.entityData.set(DATA_PUFFINESS_ID, puffiness);
    }

    public boolean isFastFalling() {
        return this.entityData.get(DATA_FAST_FALLING_ID);
    }

    public void setFastFalling(boolean fastFalling) {
        this.entityData.set(DATA_FAST_FALLING_ID, fastFalling);
    }

    public int getPuffSubtract() {
        return this.puffSubtract;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 70) {
            this.spawnExplosionParticle();
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return AetherSoundEvents.ENTITY_AERBUNNY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AetherSoundEvents.ENTITY_AERBUNNY_DEATH.get();
    }

    protected static class BlightbunnyServantMoveControl extends MoveControl {
        private final BlightbunnyServant bunny;

        public BlightbunnyServantMoveControl(BlightbunnyServant bunny) {
            super(bunny);
            this.bunny = bunny;
        }

        @Override
        public void tick() {
            super.tick();
            if (this.bunny.zza != 0.0F) {
                if (this.bunny.onGround()) {
                    this.bunny.getJumpControl().jump();
                } else {
                    int x = Mth.floor(this.bunny.getX());
                    int y = Mth.floor(this.bunny.getBoundingBox().minY);
                    int z = Mth.floor(this.bunny.getZ());
                    if (this.bunny.getTarget() == null && checkForSurfaces(this.bunny.level(), x, y, z) && !this.bunny.horizontalCollision) {
                        this.bunny.midairJump();
                    }
                }
            }
        }

        private boolean checkForSurfaces(Level level, int x, int y, int z) {
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);
            if (level.getBlockState(pos.setY(y - 1)).isAir()) {
                return false;
            }
            return level.getBlockState(pos.setY(y + 2)).isAir() && level.getBlockState(pos.setY(y + 1)).isAir();
        }
    }
}
