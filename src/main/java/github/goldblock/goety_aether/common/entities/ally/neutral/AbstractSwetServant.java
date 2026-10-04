package github.goldblock.goety_aether.common.entities.ally.neutral;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.api.entities.ally.IServant;
import com.Polarice3.Goety.common.entities.ai.SummonTargetGoal;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.Goety.mixin.MobAccessor;
import com.Polarice3.Goety.utils.EntityFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.aetherteam.aether.client.AetherSoundEvents;
import com.aetherteam.aether.entity.monster.Swet;
import github.goldblock.goety_aether.GoetyAether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.zepalesque.redux.capability.ReduxCapabilities;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractSwetServant extends Swet implements IServant, OwnableEntity {
    private static final int WANDERING_FLAG = 1;
    private static final int STAYING_FLAG = 2;
    private static final int COMMAND_TICKS = 200;
    private static final double FOLLOW_STOP_DISTANCE = 3.0D;
    private static final double WANDER_DISTANCE = 6.0D;
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(AbstractSwetServant.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Integer> OWNER_CLIENT_ID = SynchedEntityData.defineId(AbstractSwetServant.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Byte> SERVANT_FLAGS = SynchedEntityData.defineId(AbstractSwetServant.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Boolean> HOSTILE = SynchedEntityData.defineId(AbstractSwetServant.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> NATURAL = SynchedEntityData.defineId(AbstractSwetServant.class, EntityDataSerializers.BOOLEAN);

    private int wanderTicks;
    private float wanderYaw;
    private BlockPos commandPos;
    private LivingEntity commandPosEntity;
    private int commandTick;
    private long ticketTime;
    public boolean limitedLifespan;
    public int limitedLifeTicks;
    private int swallowCooldown;
    private int targetDebugTick;

    public AbstractSwetServant(EntityType<? extends Swet> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.checkHostility();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.removeAllGoals(goal -> goal instanceof Swet.SwetRandomDirectionGoal
                || goal instanceof Swet.ConsumeGoal
                || goal instanceof Swet.HuntGoal);
        this.goalSelector.addGoal(0, new ServantConsumeGoal(this));
        this.goalSelector.addGoal(1, new ServantHuntGoal(this));
        this.goalSelector.addGoal(2, new ServantDirectionGoal(this));
        this.targetSelector.removeAllGoals(goal -> goal instanceof NearestAttackableTargetGoal);
        this.targetSelector.addGoal(1, new Owned.OwnerHurtByTargetGoal<>(this));
        this.targetSelector.addGoal(2, new Owned.OwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(1, new DebugSummonTargetGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
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
            if (this.swallowCooldown > 0) {
                --this.swallowCooldown;
            }
            this.updateWander();
            this.ensureTargetGoals();
        }
        super.tick();
        this.ownedTick();
        this.servantTick();
        if (!this.level().isClientSide() && ++this.targetDebugTick >= 40) {
            this.targetDebugTick = 0;
            LivingEntity currentTarget = this.getTarget();
            if (currentTarget == null) {
                StringBuilder sb = new StringBuilder("[SwetDebug] id=").append(this.getId())
                        .append(" owner=").append(this.getTrueOwner() == null ? "none" : this.getTrueOwner().getName().getString())
                        .append(" hostile=").append(this.isHostile())
                        .append(" staying=").append(this.isStaying())
                        .append(" noAi=").append(this.isNoAi())
                        .append(" targetGoals=[");
                for (WrappedGoal wrapped : this.targetSelector.getAvailableGoals()) {
                    sb.append(wrapped.getGoal().getClass().getSimpleName()).append('@').append(wrapped.getPriority())
                            .append(wrapped.isRunning() ? "R " : " ");
                }
                sb.append("] candidates:");
                List<LivingEntity> candidates = this.level().getEntitiesOfClass(LivingEntity.class,
                        this.getBoundingBox().inflate(16.0D), e -> e != this && e.isAlive());
                for (LivingEntity candidate : candidates) {
                    sb.append(' ').append(candidate.getType().toShortString())
                            .append('@').append((int) this.distanceTo(candidate))
                            .append(":targetable=").append(MobUtil.isOwnedTargetable(this, candidate));
                }
                GoetyAether.LOGGER.info(sb.toString());
            } else {
                GoetyAether.LOGGER.info("[SwetDebug] id={} hasTarget={}", this.getId(), currentTarget.getType().toShortString());
            }
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (!this.level().isClientSide() && target != this.getTarget()) {
            GoetyAether.LOGGER.info("[SwetDebug] id={} setTarget -> {}", this.getId(),
                    target == null ? "null" : target.getType().toShortString());
        }
        super.setTarget(target);
    }

    @Override
    public boolean isFriendlyTowardEntity(LivingEntity pEntity) {
        return super.isFriendlyTowardEntity(pEntity) || MobUtil.areAllies(this, pEntity);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == ReduxCapabilities.SWET_MASS) {
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    protected void ensureTargetGoals() {
        boolean hasSummonTarget = false;
        for (WrappedGoal wrapped : this.targetSelector.getAvailableGoals()) {
            if (wrapped.getGoal() instanceof SummonTargetGoal) {
                hasSummonTarget = true;
                break;
            }
        }
        if (!hasSummonTarget) {
            this.targetSelector.addGoal(1, new Owned.OwnerHurtByTargetGoal<>(this));
            this.targetSelector.addGoal(2, new Owned.OwnerHurtTargetGoal<>(this));
            this.targetSelector.addGoal(1, new DebugSummonTargetGoal(this));
            GoetyAether.LOGGER.info("[SwetDebug] id={} restored target goals (wiped by external hook)", this.getId());
        }
    }

    public boolean canSwallow(@Nullable LivingEntity prey) {
        if (this.swallowCooldown > 0 || prey == null || !prey.isAlive() || this.isFriendlyTowardEntity(prey)) {
            return false;
        }
        if (prey == this.getTrueOwner()) {
            return false;
        }
        if (this.getOwnerId() != null && prey instanceof OwnableEntity ownable) {
            UUID ownerUuid = ownable.getOwnerUUID();
            if (ownerUuid != null && ownerUuid.equals(this.getOwnerId())) {
                return false;
            }
        }
        return true;
    }

    public void setSwallowCooldown(int ticks) {
        this.swallowCooldown = ticks;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.hasPrey() && this.getTrueOwner() == player) {
            if (!this.level().isClientSide()) {
                this.consumePassenger(player);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return InteractionResult.PASS;
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

    @Nullable
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

    class DebugSummonTargetGoal extends SummonTargetGoal {
        private boolean lastResult = true;

        DebugSummonTargetGoal(AbstractSwetServant swet) {
            super(swet);
        }

        @Override
        public boolean canUse() {
            boolean result = super.canUse();
            if (result && this.target != null && AbstractSwetServant.this.isFriendlyTowardEntity(this.target)) {
                return false;
            }
            if (result != this.lastResult) {
                this.lastResult = result;
                GoetyAether.LOGGER.info("[SwetDebug] id={} SummonTargetGoal.canUse -> {} (target={})",
                        AbstractSwetServant.this.getId(), result,
                        this.target == null ? "null" : this.target.getType().toShortString());
            }
            return result;
        }
    }

    class ServantDirectionGoal extends Goal {
        private final AbstractSwetServant swet;
        private float chosenDegrees;
        private int nextRandomizeTime;

        ServantDirectionGoal(AbstractSwetServant swet) {
            this.swet = swet;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.swet.getTarget() == null
                    && !this.swet.hasPrey()
                    && (this.swet.onGround() || this.swet.isInFluidType() || this.swet.hasEffect(MobEffects.MOVEMENT_SPEED))
                    && this.swet.getMoveControl() instanceof Swet.SwetMoveControl;
        }

        @Override
        public void tick() {
            Swet.SwetMoveControl moveControl = (Swet.SwetMoveControl) this.swet.getMoveControl();
            Vec3 dest = this.swet.getServantMoveTarget();
            if (dest != null) {
                double dx = dest.x - this.swet.getX();
                double dz = dest.z - this.swet.getZ();
                if (dx * dx + dz * dz > 1.0D) {
                    float degrees = (float) (Mth.atan2(dz, dx) * (180.0D / Math.PI)) - 90.0F;
                    moveControl.setDirection(degrees, false);
                    moveControl.setCanJump(true);
                    return;
                }
            }
            float rot = this.chosenDegrees;
            Vec3 offset = new Vec3(-Math.sin(rot * ((float) Math.PI / 180F)) * 2.0D, 0.0D, Math.cos(rot * ((float) Math.PI / 180F)) * 2.0D);
            BlockPos offsetPos = BlockPos.containing(this.swet.position().add(offset));
            if (this.swet.level().getHeight(Heightmap.Types.WORLD_SURFACE, offsetPos.getX(), offsetPos.getZ()) < offsetPos.getY() - this.swet.getMaxFallDistance()) {
                this.nextRandomizeTime = this.adjustedTickDelay(40 + this.swet.getRandom().nextInt(60));
                this.chosenDegrees += 180.0F;
                moveControl.setCanJump(false);
            } else {
                if (--this.nextRandomizeTime <= 0) {
                    this.nextRandomizeTime = this.adjustedTickDelay(40 + this.swet.getRandom().nextInt(60));
                    this.chosenDegrees = this.swet.getRandom().nextInt(360);
                }
                moveControl.setCanJump(true);
            }
            moveControl.setDirection(this.chosenDegrees, false);
        }
    }

    class ServantHuntGoal extends Goal {
        private final AbstractSwetServant swet;

        ServantHuntGoal(AbstractSwetServant swet) {
            this.swet = swet;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.swet.getTarget();
            if (target == null || !target.isAlive() || this.swet.hasPrey() || this.swet.isStaying()) {
                return false;
            }
            return this.swet.canSwallow(target) && this.swet.getMoveControl() instanceof Swet.SwetMoveControl;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.swet.getTarget();
            if (target == null || !(this.swet.getMoveControl() instanceof Swet.SwetMoveControl moveControl)) {
                return;
            }
            this.swet.lookAt(target, 10.0F, 10.0F);
            moveControl.setDirection(this.swet.getYRot(), true);
            moveControl.setWantedMovement(1.0D);
            if (this.swet.getBoundingBox().intersects(target.getBoundingBox()) && this.swet.canSwallow(target)) {
                this.swet.consumePassenger(target);
            }
        }
    }

    class ServantConsumeGoal extends Goal {
        private final AbstractSwetServant swet;
        private int jumps;
        private float chosenDegrees;
        private int hurtTicks;
        private boolean wasOnGround;

        ServantConsumeGoal(AbstractSwetServant swet) {
            this.swet = swet;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.swet.getFirstPassenger() instanceof LivingEntity living && this.swet.canSwallow(living);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (!(this.swet.getFirstPassenger() instanceof LivingEntity prey)) {
                return;
            }
            if (--this.hurtTicks <= 0) {
                this.hurtTicks = 10;
                prey.hurt(this.swet.damageSources().mobAttack(this.swet), (float) this.swet.getAttributeValue(Attributes.ATTACK_DAMAGE));
            }
            boolean onGround = this.swet.onGround();
            if (onGround && !this.wasOnGround) {
                if (this.jumps >= 3) {
                    this.jumps = 0;
                    prey.stopRiding();
                    this.swet.spawnDissolveParticles();
                    this.swet.setSwallowCooldown(40);
                } else {
                    this.swet.playSound(AetherSoundEvents.ENTITY_SWET_JUMP.get(), 1.0F, ((this.swet.getRandom().nextFloat() - this.swet.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                    this.chosenDegrees = this.swet.getRandom().nextInt(360);
                    double jumpStrength = this.jumps == 0 ? 0.65D : (this.jumps == 1 ? 0.75D : 1.55D);
                    this.jumps++;
                    this.swet.setDeltaMovement(this.swet.getDeltaMovement().add(0.0D, jumpStrength, 0.0D));
                }
            }
            this.wasOnGround = onGround;
            if (!onGround) {
                float forward = 0.1F * this.jumps;
                float f1 = Mth.sin(this.chosenDegrees * ((float) Math.PI / 180F));
                float f2 = Mth.cos(this.chosenDegrees * ((float) Math.PI / 180F));
                this.swet.setDeltaMovement(-forward * f1, this.swet.getDeltaMovement().y(), forward * f2);
                if (this.swet.getMoveControl() instanceof Swet.SwetMoveControl moveControl) {
                    moveControl.setDirection(this.chosenDegrees, false);
                }
            }
        }
    }
}
