package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.api.entities.ally.IServant;
import com.Polarice3.Goety.common.entities.ai.SummonTargetGoal;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.Goety.mixin.MobAccessor;
import com.Polarice3.Goety.client.particles.CircleExplodeParticleOption;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.client.particles.SphereExplodeParticleOption;
import com.Polarice3.Goety.common.entities.util.CameraShake;
import com.Polarice3.Goety.utils.ColorUtil;
import com.Polarice3.Goety.utils.EntityFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.aetherteam.aether.entity.monster.dungeon.Sentry;
import github.goldblock.goety_aether.common.entities.ally.ServantMover;
import github.goldblock.goety_aether.common.entities.ally.SlimeServantMoveControl;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class SentryServant extends Sentry implements IServant, OwnableEntity, ServantMover {
    private static final int WANDERING_FLAG = 1;
    private static final int STAYING_FLAG = 2;
    private static final float WAKE_TICKS = 24.0F;
    private static final double WAKE_RANGE = 8.0D;
    private static final int COMMAND_TICKS = 200;
    private static final double FOLLOW_STOP_DISTANCE = 3.0D;
    private static final double WANDER_DISTANCE = 6.0D;
    protected static final EntityDataAccessor<Boolean> DATA_AWAKE_ID = SynchedEntityData.defineId(SentryServant.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(SentryServant.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Integer> OWNER_CLIENT_ID = SynchedEntityData.defineId(SentryServant.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Byte> SERVANT_FLAGS = SynchedEntityData.defineId(SentryServant.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Boolean> HOSTILE = SynchedEntityData.defineId(SentryServant.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> NATURAL = SynchedEntityData.defineId(SentryServant.class, EntityDataSerializers.BOOLEAN);

    private float spotTicks;
    private int wanderTicks;
    private float wanderYaw;
    private BlockPos commandPos;
    private LivingEntity commandPosEntity;
    private int commandTick;
    private long ticketTime;
    public boolean limitedLifespan;
    public int limitedLifeTicks;

    public SentryServant(EntityType<? extends Sentry> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new SlimeServantMoveControl<>(this);
        this.checkHostility();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.removeAllGoals(goal -> goal instanceof NearestAttackableTargetGoal);
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

    public void setServantAwake(boolean awake) {
        this.entityData.set(DATA_AWAKE_ID, awake);
    }

    @Override
    public boolean isAwake() {
        return this.entityData.get(DATA_AWAKE_ID);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!this.level().isClientSide() && pSource.getEntity() instanceof LivingEntity living && living != this && !MobUtil.areAllies(this, living)) {
            this.setServantAwake(true);
            this.setTarget(living);
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected void explodeAt(LivingEntity pEntity) {
        if (MobUtil.areAllies(this, pEntity)) {
            return;
        }
        if (this.distanceToSqr(pEntity) < 1.5D && this.isAwake() && this.hasLineOfSight(pEntity) && pEntity.hurt(this.damageSources().mobAttack(this), 1.0F) && this.tickCount > 20 && this.isAlive()) {
            pEntity.knockback(0.3D, 0.4D, 0.3D);
            this.doEnchantDamageEffects(this, pEntity);
            this.explodeSelf();
        }
    }

    private void explodeSelf() {
        float explosionPower = 5.0F;
        if (this.level() instanceof ServerLevel serverLevel) {
            ColorUtil colorUtil = new ColorUtil(0x7EC8F2);
            serverLevel.sendParticles(new CircleExplodeParticleOption(colorUtil.red, colorUtil.green, colorUtil.blue, explosionPower, 1), this.getX(), this.getY(), this.getZ(), 0, 0.0D, 0.0D, 0.0D, 0);
            serverLevel.sendParticles(new SphereExplodeParticleOption(colorUtil.red, colorUtil.green, colorUtil.blue, explosionPower, 1), this.getX(), this.getY(), this.getZ(), 0, 0.0D, 0.0D, 0.0D, 0);
            for (int i = 0; i < 32; ++i) {
                ColorUtil colorUtil1 = new ColorUtil(0xac9b8f);
                serverLevel.sendParticles(ModParticleTypes.BIG_CULT_SPELL.get(), this.getRandomX(1.0F), this.getRandomY(), this.getRandomZ(1.0F), 0, colorUtil1.red, colorUtil1.green, colorUtil1.blue, 1.0F);
            }
        }
        this.allySafeExplosion(1.0F);
        this.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 0.2F * (this.random.nextFloat() - this.random.nextFloat()) + 1.0F);
        CameraShake.cameraShake(this.level(), this.position(), explosionPower * 2, 0.1F, 0, 20);
        this.discard();
    }

    @Override
    public void lifeSpanDamage() {
        if (!this.level().isClientSide() && this.isAlive()) {
            this.explodeSelf();
        }
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

    private void allySafeExplosion(float pRadius) {
        Explosion explosion = new Explosion(this.level(), this, this.getX(), this.getY(), this.getZ(), pRadius, false, Explosion.BlockInteraction.KEEP);
        float f = pRadius * 2.0F;
        Vec3 center = new Vec3(this.getX(), this.getY(), this.getZ());
        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(f), target -> target.isAlive() && !MobUtil.areAllies(this, target))) {
            double d0 = Math.sqrt(entity.distanceToSqr(this.getX(), this.getY(), this.getZ())) / f;
            if (d0 <= 1.0D) {
                double d1 = entity.getX() - this.getX();
                double d2 = entity.getEyeY() - this.getY();
                double d3 = entity.getZ() - this.getZ();
                double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
                if (d4 != 0.0D) {
                    d1 /= d4;
                    d2 /= d4;
                    d3 /= d4;
                    double d5 = (1.0D - d0) * Explosion.getSeenPercent(center, entity);
                    entity.hurt(this.damageSources().explosion(explosion), (float) ((d5 * d5 + d5) / 2.0D * 7.0D * f + 1.0D));
                    double d6 = d5;
                    if (entity instanceof LivingEntity living) {
                        d6 = ProtectionEnchantment.getExplosionKnockbackAfterDampener(living, d5);
                    }
                    entity.setDeltaMovement(entity.getDeltaMovement().add(d1 * d6, d2 * d6, d3 * d6));
                }
            }
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
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
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
}
