package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.api.entities.ally.IServant;
import com.Polarice3.Goety.common.entities.ai.SummonTargetGoal;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.Goety.mixin.MobAccessor;
import com.Polarice3.Goety.utils.EntityFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.aetherteam.aether.entity.MountableMob;
import com.aetherteam.aether.entity.ai.goal.FallingRandomStrollGoal;
import com.aetherteam.aether.entity.monster.Cockatrice;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class CockatriceServant extends Cockatrice implements IServant, OwnableEntity, MountableMob {
    public static final int MAX_JUMPS = 6;
    private static final int WANDERING_FLAG = 1;
    private static final int STAYING_FLAG = 2;
    private static final int COMMAND_TICKS = 200;
    private static final double FOLLOW_STOP_DISTANCE = 5.0D;
    private static final double WANDER_DISTANCE = 6.0D;
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(CockatriceServant.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Integer> OWNER_CLIENT_ID = SynchedEntityData.defineId(CockatriceServant.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Byte> SERVANT_FLAGS = SynchedEntityData.defineId(CockatriceServant.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Boolean> HOSTILE = SynchedEntityData.defineId(CockatriceServant.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> NATURAL = SynchedEntityData.defineId(CockatriceServant.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> DATA_REMAINING_JUMPS = SynchedEntityData.defineId(CockatriceServant.class, EntityDataSerializers.INT);

    private int wanderTicks;
    private float wanderYaw;
    private BlockPos commandPos;
    private LivingEntity commandPosEntity;
    private int commandTick;
    private long ticketTime;
    private int jumpCooldown;
    private boolean playerJumped;
    private boolean mountJumping;
    public boolean limitedLifespan;
    public int limitedLifeTicks;

    public CockatriceServant(EntityType<? extends Cockatrice> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.checkHostility();
    }

    @Override
    public void registerGoals() {
        super.registerGoals();
        this.targetSelector.removeAllGoals(goal -> goal instanceof NearestAttackableTargetGoal);
        this.goalSelector.removeAllGoals(goal -> goal instanceof FallingRandomStrollGoal);
        this.targetSelector.addGoal(1, new Owned.OwnerHurtByTargetGoal<>(this));
        this.targetSelector.addGoal(2, new Owned.OwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(1, new SummonTargetGoal(this));
        this.goalSelector.addGoal(4, new Summoned.FollowOwnerGoal<>(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(6, new FallingRandomStrollGoal((PathfinderMob) this, 1.0D));
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
        this.entityData.define(OWNER_CLIENT_ID, -1);
        this.entityData.define(SERVANT_FLAGS, (byte) 0);
        this.entityData.define(HOSTILE, false);
        this.entityData.define(NATURAL, false);
        this.entityData.define(DATA_REMAINING_JUMPS, MAX_JUMPS);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.readOwnedData(pCompound);
        this.readServantData(pCompound);
        if (pCompound.contains("RemainingJumps")) {
            this.setRemainingJumps(pCompound.getInt("RemainingJumps"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.saveOwnedData(pCompound);
        this.saveServantData(pCompound);
        pCompound.putInt("RemainingJumps", this.getRemainingJumps());
    }

    @Override
    public void tick() {
        MountableMob.super.tick(this);
        MountableMob.super.riderTick(this);
        super.tick();
        if (this.getJumpCooldown() > 0) {
            this.setJumpCooldown(this.getJumpCooldown() - 1);
            this.setPlayerJumped(false);
        } else if (this.getJumpCooldown() == 0) {
            this.setMountJumping(false);
        }
        if (this.onGround()) {
            this.setRemainingJumps(MAX_JUMPS);
        }
        AttributeInstance gravity = this.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
        LivingEntity rider = this.getControllingPassenger();
        boolean riderCrouching = rider != null && rider.isShiftKeyDown();
        if (gravity != null && this.isVehicle() && !riderCrouching) {
            double fallSpeed = Math.max(gravity.getValue() * -1.25D, -0.5D);
            if (this.getDeltaMovement().y < fallSpeed) {
                this.setDeltaMovement(this.getDeltaMovement().x, fallSpeed, this.getDeltaMovement().z);
                this.fallDistance = 0.0F;
            }
        }
        if (!this.level().isClientSide()) {
            this.updateWander();
        }
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

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.isVehicle() && !this.isBaby() && hand == InteractionHand.MAIN_HAND && !player.isShiftKeyDown()
                && player == this.getTrueOwner()) {
            if (!this.level().isClientSide()) {
                player.startRiding(this);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return super.mobInteract(player, hand);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.getFirstPassenger() instanceof Player player ? player : super.getControllingPassenger();
    }

    @Override
    public void travel(Vec3 travelVector) {
        MountableMob.super.travel(this, travelVector);
    }

    @Override
    public void travelWithInput(Vec3 travelVec) {
        super.travel(travelVec);
    }

    @Override
    protected float getFlyingSpeed() {
        return this.getControllingPassenger() != null ? this.getSteeringSpeed() * 0.25F : super.getFlyingSpeed();
    }

    @Override
    public boolean canJump() {
        return this.getRemainingJumps() > 0 && this.getJumpCooldown() == 0;
    }

    @Override
    public double getMountJumpStrength() {
        return this.onGround() ? 0.95D : 0.9D;
    }

    @Override
    public void onJump(Mob vehicle) {
        MountableMob.super.onJump(vehicle);
        this.setJumpCooldown(10);
        if (!this.onGround()) {
            this.setRemainingJumps(this.getRemainingJumps() - 1);
        }
    }

    @Override
    public boolean getPlayerJumped() {
        return this.playerJumped;
    }

    @Override
    public void setPlayerJumped(boolean jumped) {
        this.playerJumped = jumped;
    }

    @Override
    public boolean isMountJumping() {
        return this.mountJumping;
    }

    @Override
    public void setMountJumping(boolean jumping) {
        this.mountJumping = jumping;
    }

    @Override
    public float getSteeringSpeed() {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.625F;
    }

    @Override
    public double jumpFactor() {
        return this.getBlockJumpFactor();
    }

    public int getRemainingJumps() {
        return this.entityData.get(DATA_REMAINING_JUMPS);
    }

    public void setRemainingJumps(int jumps) {
        this.entityData.set(DATA_REMAINING_JUMPS, Math.max(0, jumps));
    }

    public int getJumpCooldown() {
        return this.jumpCooldown;
    }

    public void setJumpCooldown(int cooldown) {
        this.jumpCooldown = cooldown;
    }
}
