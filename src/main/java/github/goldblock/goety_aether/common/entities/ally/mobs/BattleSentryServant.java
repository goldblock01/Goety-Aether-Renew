package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.api.entities.ally.IServant;
import com.Polarice3.Goety.common.entities.ai.SummonTargetGoal;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.Goety.mixin.MobAccessor;
import com.Polarice3.Goety.utils.EntityFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.client.AetherSoundEvents;
import github.goldblock.goety_aether.common.entities.ally.ServantMover;
import github.goldblock.goety_aether.common.entities.ally.SlimeServantMoveControl;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class BattleSentryServant extends Slime implements IServant, OwnableEntity, ServantMover {
    private static final int WANDERING_FLAG = 1;
    private static final int STAYING_FLAG = 2;
    private static final float WAKE_TICKS = 24.0F;
    private static final double WAKE_RANGE = 8.0D;
    private static final int COMMAND_TICKS = 200;
    private static final double FOLLOW_STOP_DISTANCE = 3.0D;
    private static final double WANDER_DISTANCE = 6.0D;
    private static final double HIT_REACH = 0.15D;
    private static final int ATTACK_INTERVAL = 20;
    protected static final EntityDataAccessor<Boolean> DATA_AWAKE_ID = SynchedEntityData.defineId(BattleSentryServant.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(BattleSentryServant.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Integer> OWNER_CLIENT_ID = SynchedEntityData.defineId(BattleSentryServant.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Byte> SERVANT_FLAGS = SynchedEntityData.defineId(BattleSentryServant.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Boolean> HOSTILE = SynchedEntityData.defineId(BattleSentryServant.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> NATURAL = SynchedEntityData.defineId(BattleSentryServant.class, EntityDataSerializers.BOOLEAN);

    private float spotTicks;
    private int wanderTicks;
    private float wanderYaw;
    private BlockPos commandPos;
    private LivingEntity commandPosEntity;
    private int commandTick;
    private long ticketTime;
    public boolean limitedLifespan;
    public int limitedLifeTicks;
    private int attackTick;

    public BattleSentryServant(EntityType<? extends Slime> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new SlimeServantMoveControl<>(this);
        this.checkHostility();
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.removeAllGoals(goal -> goal instanceof NearestAttackableTargetGoal);
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (entity) -> Math.abs(entity.getY() - this.getY()) <= 4.0D));
        this.targetSelector.addGoal(1, new Owned.OwnerHurtByTargetGoal<>(this));
        this.targetSelector.addGoal(2, new Owned.OwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(1, new SummonTargetGoal(this));
        this.goalSelector.addGoal(4, new Summoned.FollowOwnerGoal<>(this, 1.0D, 10.0F, 2.0F));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_AWAKE_ID, false);
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
        this.entityData.define(OWNER_CLIENT_ID, -1);
        this.entityData.define(SERVANT_FLAGS, (byte) 0);
        this.entityData.define(HOSTILE, false);
        this.entityData.define(NATURAL, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.readOwnedData(pCompound);
        this.readServantData(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.saveOwnedData(pCompound);
        this.saveServantData(pCompound);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            this.updateAwake();
            this.updateWander();
            this.servantAttack();
        }
        super.tick();
        this.ownedTick();
        this.servantTick();
    }

    protected void updateWander() {
        if (this.isWandering() && this.getTarget() == null) {
            if (--this.wanderTicks <= 0) {
                this.wanderTicks = 60 + this.random.nextInt(100);
                this.wanderYaw = this.random.nextFloat() * 360.0F;
            }
        } else {
            this.wanderTicks = 0;
        }
    }

    protected void updateAwake() {
        if (this.getTrueOwner() != null && !this.isStaying()) {
            this.spotTicks = 0.0F;
            this.setServantAwake(true);
            return;
        }
        if (this.isThreatNearby()) {
            if (!this.isAwake()) {
                if (this.spotTicks >= WAKE_TICKS) {
                    this.setServantAwake(true);
                }
                this.spotTicks++;
            }
        } else {
            this.spotTicks = 0.0F;
            this.setServantAwake(false);
        }
    }

    protected boolean isThreatNearby() {
        if (this.getTarget() != null) {
            return true;
        }
        return !this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(WAKE_RANGE), this::isThreat).isEmpty();
    }

    protected boolean isThreat(LivingEntity pLivingEntity) {
        if (pLivingEntity == this || MobUtil.areAllies(this, pLivingEntity)) {
            return false;
        }
        return pLivingEntity instanceof Mob mob && mob.getTarget() != null;
    }

    protected void servantAttack() {
        LivingEntity target = this.getTarget();
        if (target == null || !this.isAwake()) {
            this.attackTick = ATTACK_INTERVAL;
            return;
        }
        if (this.attackTick < ATTACK_INTERVAL) {
            this.attackTick++;
            return;
        }
        int invulnerableTime = target.invulnerableTime;
        this.dealDamage(target);
        if (target.invulnerableTime != invulnerableTime) {
            this.attackTick = 0;
        }
    }

    public void setServantAwake(boolean awake) {
        this.entityData.set(DATA_AWAKE_ID, awake);
    }

    public boolean isAwake() {
        return this.entityData.get(DATA_AWAKE_ID);
    }

    @Override
    protected void dealDamage(LivingEntity pTarget) {
        if (!this.isAlive() || !this.getBoundingBox().inflate(HIT_REACH).intersects(pTarget.getBoundingBox()) || !this.hasLineOfSight(pTarget)) {
            return;
        }
        if (pTarget.hurt(this.damageSources().mobAttack(this), this.getAttackDamage())) {
            this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.doEnchantDamageEffects(this, pTarget);
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    private boolean getServantFlag(int mask) {
        return (this.entityData.get(SERVANT_FLAGS) & mask) != 0;
    }

    private void setServantFlag(int mask, boolean value) {
        byte flags = this.entityData.get(SERVANT_FLAGS);
        this.entityData.set(SERVANT_FLAGS, (byte) (value ? flags | mask : flags & ~mask));
    }

    @Override
    public boolean isWandering() {
        return this.getServantFlag(WANDERING_FLAG);
    }

    @Override
    public void setWandering(boolean wandering) {
        this.setServantFlag(WANDERING_FLAG, wandering);
    }

    @Override
    public boolean isStaying() {
        return this.getServantFlag(STAYING_FLAG) && !this.isCommanded() && !this.isVehicle();
    }

    @Override
    public void setStaying(boolean staying) {
        this.setServantFlag(STAYING_FLAG, staying);
    }

    @Override
    public boolean canUpdateMove() {
        return true;
    }

    @Override
    public boolean canGuardArea() {
        return false;
    }

    @Override
    public boolean isCommanded() {
        return this.commandPos != null;
    }

    @Override
    public void setCommandPos(BlockPos blockPos, boolean removeEntity) {
        if (removeEntity) {
            this.setCommandPosEntity(null);
        }
        this.commandPos = blockPos;
        this.commandTick = COMMAND_TICKS;
    }

    @Override
    public BlockPos getCommandPos() {
        return this.commandPos;
    }

    @Override
    public int getCommandTick() {
        return this.commandTick;
    }

    @Override
    public void setCommandTick(int commandTick) {
        this.commandTick = commandTick;
    }

    @Override
    public void setCommandPosEntity(@Nullable LivingEntity living) {
        this.commandPosEntity = living;
        if (living != null) {
            this.setCommandPos(living.blockPosition(), false);
        }
    }

    @Nullable
    @Override
    public LivingEntity getCommandPosEntity() {
        return this.commandPosEntity;
    }

    @Override
    public long getTicketTime() {
        return this.ticketTime;
    }

    @Override
    public void setTicketTime(long ticketTime) {
        this.ticketTime = ticketTime;
    }

    @Override
    public void setHasLifespan(boolean lifespan) {
        this.limitedLifespan = lifespan;
    }

    @Override
    public boolean hasLifespan() {
        return this.limitedLifespan;
    }

    @Override
    public void setLifespan(int lifespan) {
        this.limitedLifeTicks = lifespan;
    }

    @Override
    public int getLifespan() {
        return this.limitedLifeTicks;
    }

    @Override
    public void tryKill(Player player) {
        this.kill();
    }

    @Nullable
    @Override
    public LivingEntity getTrueOwner() {
        if (!this.level().isClientSide()) {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : EntityFinder.getLivingEntityByUuiD(uuid);
        } else {
            int id = this.getOwnerClientId();
            return id <= -1 || !(this.level().getEntity(id) instanceof LivingEntity living) || living == this ? null : living;
        }
    }

    @Nullable
    @Override
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse(null);
    }

    @Override
    public void setOwnerId(@Nullable UUID pUUID) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(pUUID));
    }

    @Override
    public int getOwnerClientId() {
        return this.entityData.get(OWNER_CLIENT_ID);
    }

    @Override
    public void setOwnerClientId(int id) {
        this.entityData.set(OWNER_CLIENT_ID, id);
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.getOwnerId();
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return this.getTrueOwner();
    }

    @Override
    public void setHostile(boolean hostile) {
        this.entityData.set(HOSTILE, hostile);
    }

    @Override
    public boolean isHostile() {
        return this.entityData.get(HOSTILE);
    }

    @Override
    public void setNatural(boolean natural) {
        this.entityData.set(NATURAL, natural);
    }

    @Override
    public boolean isNatural() {
        return this.entityData.get(NATURAL);
    }

    @Override
    public void checkHostility() {
        if (!this.level().isClientSide()) {
            if (this.getTrueOwner() instanceof Enemy) {
                this.setHostile(true);
            }
            if (this.getTrueOwner() instanceof IOwned owned && owned.isHostile()) {
                this.setHostile(true);
            }
        }
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (this.getTrueOwner() != null) {
            LivingEntity trueOwner = this.getTrueOwner();
            return trueOwner.isAlliedTo(pEntity)
                    || pEntity.isAlliedTo(trueOwner)
                    || pEntity == trueOwner
                    || (pEntity instanceof IOwned owned && MobUtil.ownerStack(this, owned))
                    || (pEntity instanceof OwnableEntity ownable && ownable.getOwner() == trueOwner)
                    || (pEntity instanceof LivingEntity livingEntity && this.isAllyWith(livingEntity));
        }
        return super.isAlliedTo(pEntity) || (pEntity instanceof LivingEntity livingEntity && this.isAllyWith(livingEntity));
    }

    @Nullable
    @Override
    public Team getTeam() {
        if (this.getTrueOwner() != null) {
            LivingEntity livingEntity = this.getTrueOwner();
            if (livingEntity != this && !this.areOwnedByEachOther(livingEntity) && livingEntity.getTeam() != null) {
                return livingEntity.getTeam();
            }
        }
        return super.getTeam();
    }

    public boolean areOwnedByEachOther(LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof IOwned owned) {
            return owned.getTrueOwner() == this && this.getTrueOwner() == pLivingEntity;
        }
        return false;
    }

    @Override
    public void push(Entity pEntity) {
        if (!this.level().isClientSide() && !MobUtil.areAllies(this, pEntity)) {
            super.push(pEntity);
        }
    }

    @Override
    protected void doPush(Entity pEntity) {
        if (!this.level().isClientSide() && !MobUtil.areAllies(this, pEntity)) {
            super.doPush(pEntity);
        }
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return !MobUtil.areAllies(this, pEntity) && super.canCollideWith(pEntity);
    }

    @Override
    public void playerTouch(Player pPlayer) {
        if (!MobUtil.areAllies(this, pPlayer)) {
            super.playerTouch(pPlayer);
        }
    }

    @Override
    public int servantJumpDelay() {
        return this.getJumpDelay();
    }

    @Override
    public void playServantJumpSound() {
        if (this.doPlayJumpSound()) {
            this.playSound(this.getJumpSound(), this.getSoundVolume(), this.getVoicePitch());
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        ((MobAccessor) this).setSpawnType(pReason);
        this.checkHostility();
        if (pReason != MobSpawnType.MOB_SUMMONED && this.getTrueOwner() == null) {
            this.setNatural(true);
        }
        this.setWandering(this.getTrueOwner() == null);
        this.setStaying(false);
        this.setSize(1, true);
        this.setLeftHanded(false);
        return pSpawnData;
    }

    @Nullable
    @Override
    public Vec3 getServantMoveTarget() {
        if (this.isPassenger()) {
            return null;
        }
        LivingEntity target = this.getTarget();
        if (target != null && target.isAlive()) {
            return target.position();
        }
        if (this.isCommanded()) {
            BlockPos commandPos = this.getCommandPos();
            return commandPos == null ? null : Vec3.atBottomCenterOf(commandPos);
        }
        if (this.isStaying()) {
            return null;
        }
        LivingEntity owner = this.getTrueOwner();
        if (owner != null && !owner.isSpectator() && this.isFollowing()) {
            return this.distanceToSqr(owner) > Mth.square(FOLLOW_STOP_DISTANCE) ? owner.position() : null;
        }
        if (owner != null && this.isWandering()) {
            return Vec3.directionFromRotation(0.0F, this.wanderYaw).scale(WANDER_DISTANCE).add(this.position());
        }
        return null;
    }

    @Override
    public void setSize(int size, boolean resetHealth) {
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        this.setRemoved(reason);
        if (reason == Entity.RemovalReason.KILLED) {
            this.gameEvent(net.minecraft.world.level.gameevent.GameEvent.ENTITY_DIE);
        }
        this.invalidateCaps();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(1.758F);
    }

    @Override
    protected float getAttackDamage() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    @Override
    protected boolean isDealsDamage() {
        return this.isEffectiveAi();
    }

    @Override
    protected net.minecraft.sounds.SoundEvent getHurtSound(DamageSource damageSource) {
        return AetherSoundEvents.ENTITY_SENTRY_HURT.get();
    }

    @Override
    protected net.minecraft.sounds.SoundEvent getDeathSound() {
        return AetherSoundEvents.ENTITY_SENTRY_DEATH.get();
    }

    @Override
    protected net.minecraft.sounds.SoundEvent getSquishSound() {
        return AetherSoundEvents.ENTITY_SENTRY_JUMP.get();
    }

    @Override
    protected net.minecraft.sounds.SoundEvent getJumpSound() {
        return AetherSoundEvents.ENTITY_SENTRY_JUMP.get();
    }

    @Override
    protected net.minecraft.core.particles.ParticleOptions getParticleType() {
        return new net.minecraft.core.particles.BlockParticleOption(net.minecraft.core.particles.ParticleTypes.BLOCK, AetherBlocks.SENTRY_STONE.get().defaultBlockState());
    }

    @Override
    protected net.minecraft.resources.ResourceLocation getDefaultLootTable() {
        return this.getType().getDefaultLootTable();
    }

    @Override
    protected void jumpFromGround() {
        if (this.isAwake()) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x, 0.1D, vec3.z);
            this.hasImpulse = true;
        }
    }
}
