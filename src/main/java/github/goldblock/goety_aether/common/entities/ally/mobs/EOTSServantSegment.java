package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.common.entities.neutral.SummonedFlying;
import com.Polarice3.Goety.common.entities.util.MagicLightningTrap;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.spells.ShockwaveSpell;
import com.Polarice3.Goety.common.magic.spells.storm.MonsoonSpell;
import com.Polarice3.Goety.common.magic.spells.storm.ThunderstormSpell;
import com.Polarice3.Goety.common.magic.spells.wind.UpdraftSpell;
import com.Polarice3.Goety.utils.MobUtil;
import github.goldblock.goety_aether.common.magic.spells.BabyZephyrFocusSpell;
import github.goldblock.goety_aether.compat.deep_aether.DeepAetherCompatManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import github.goldblock.goety_aether.common.magic.spells.WindCrystalSpell;
import github.goldblock.goety_aether.compat.deep_aether.DeepAetherBridge;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

public class EOTSServantSegment extends SummonedFlying {
    private static final EntityDataAccessor<Boolean> DATA_HEAD_ID = SynchedEntityData.defineId(EOTSServantSegment.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_OPEN_MOUTH = SynchedEntityData.defineId(EOTSServantSegment.class, EntityDataSerializers.BOOLEAN);

    @Nullable
    private EOTSServantSegment parent;
    @Nullable
    private UUID parentUUID;
    @Nullable
    private EOTSServantSegment child;
    @Nullable
    private UUID childUUID;
    private int randomYOffset = 0;
    private boolean shouldMove = true;
    private boolean isAttacking = false;
    private final Spell monsoonSpell = new MonsoonSpell();
    private final Spell updraftSpell = new UpdraftSpell();
    private final Spell thunderstormSpell = new ThunderstormSpell();
    private final Spell babyZephyrSpell = new BabyZephyrFocusSpell();
    private final Spell shockwaveSpell = new ShockwaveSpell();
    private int monsoonCooldown;
    private int updraftCooldown;
    private int thunderstormCooldown;
    private int babyZephyrCooldown;
    private int shockwaveCooldown;
    private int headGraceTicks;

    public EOTSServantSegment(EntityType<? extends EOTSServantSegment> type, Level level) {
        super(type, level);
        this.moveControl = new EotsSegmentMoveControl(this);
        this.lookControl = new EotsLookControl(this);
        this.noPhysics = true;
    }

    public EOTSServantSegment(Level level, EOTSServantSegment parent) {
        this(DeepAetherCompatManager.EOTSSERVANT_SEGMENT.get(), level);
        this.setPos(parent.getOnPos().getCenter());
        level.addFreshEntity(this);
        this.setParent(parent);
        parent.setChild(this);
        LivingEntity owner = parent.getTrueOwner();
        if (owner != null) {
            this.setTrueOwner(owner);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return net.minecraft.world.entity.monster.Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 96.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 9.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HEAD_ID, true);
        this.entityData.define(DATA_OPEN_MOUTH, false);
    }

    @Override
    public void followGoal() {
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new EotsAirChargeGoal(this));
        this.goalSelector.addGoal(1, new EotsAttackGoal(this));
        this.goalSelector.addGoal(1, new EotsSpellCastingGoal(this));
        this.goalSelector.addGoal(2, new RandomFloatAroundGoal(this));
    }

    @Override
    public void knockback(double strength, double x, double z) {
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.monsoonCooldown > 0) this.monsoonCooldown--;
            if (this.updraftCooldown > 0) this.updraftCooldown--;
            if (this.thunderstormCooldown > 0) this.thunderstormCooldown--;
            if (this.babyZephyrCooldown > 0) this.babyZephyrCooldown--;
            if (this.shockwaveCooldown > 0) this.shockwaveCooldown--;
            if (this.headGraceTicks > 0) this.headGraceTicks--;
        }
        if (!this.isControllingSegment()) {
            EOTSServantSegment parent = this.getParent();
            if (parent != null) {
                float newYRot;
                float newXRot;
                float yRot = this.getYRot();
                float yParentRot = parent.getYRot();
                if (Math.abs(yRot - yParentRot) < 0.1F) {
                    newYRot = yRot;
                } else {
                    newYRot = Mth.lerp(0.15F, yRot, yParentRot);
                }
                float xRot = this.getXRot();
                float xParentRot = parent.getXRot();
                if (Math.abs(xRot - xParentRot) < 0.1F) {
                    newXRot = xRot;
                } else {
                    newXRot = Mth.lerp(0.15F, xRot, xParentRot);
                }
                this.setRot(newYRot, newXRot);
                this.setPos(parent.position()
                        .subtract(parent.getLookAngle().multiply(1.7999999523162842D, 0.0D, 1.7999999523162842D))
                        .subtract(parent.getLookAngle().reverse().multiply(0.0D, 1.7999999523162842D, 0.0D)));
            } else if (!this.level().isClientSide) {
                this.headGraceTicks = 100;
                this.setControllingSegment(true);
            }
            if (this.getTarget() != null && this.getTarget().distanceToSqr(this) < 1.0D) {
                this.doHurtTarget(this.getTarget());
            }
        }
    }

    private boolean hasWindServantNearby() {
        AABB aabb = this.getBoundingBox().inflate(48.0D);
        return !this.level().getEntitiesOfClass(ZephyrServant.class, aabb, servant -> !(servant instanceof TempestServant) && MobUtil.areAllies(this, servant)).isEmpty();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return DeepAetherBridge.sound("entity.eots.ambient");
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return DeepAetherBridge.sound("entity.eots.hurt");
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return DeepAetherBridge.sound("entity.eots.death");
    }

    @Override
    protected void blockedByShield(LivingEntity defender) {
        super.blockedByShield(defender);
        if (defender instanceof Player player) {
            player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 1000);
            player.invulnerableTime = 20;
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    public boolean isControllingSegment() {
        return this.entityData.get(DATA_HEAD_ID);
    }

    public void setControllingSegment(boolean head) {
        this.entityData.set(DATA_HEAD_ID, head);
    }

    public boolean isMouthOpen() {
        return this.entityData.get(DATA_OPEN_MOUTH);
    }

    public void setMouthOpen(boolean open) {
        this.entityData.set(DATA_OPEN_MOUTH, open);
    }

    public void setRandomYOffset(int offset) {
        this.randomYOffset = offset;
    }

    @Nullable
    public EOTSServantSegment getParent() {
        if (this.parent != null && !this.parent.isRemoved()) {
            return this.parent;
        }
        if (this.parentUUID == null) {
            return null;
        }
        if (this.parent == null && this.level() instanceof ServerLevel serverLevel) {
            this.parent = (EOTSServantSegment) serverLevel.getEntity(this.parentUUID);
            return this.parent;
        }
        return null;
    }

    public void setParent(@Nullable EOTSServantSegment parent) {
        this.setControllingSegment(parent == null);
        this.parent = parent;
        this.parentUUID = parent == null ? null : parent.getUUID();
    }

    @Nullable
    public EOTSServantSegment getChild() {
        if (this.child != null && !this.child.isRemoved()) {
            return this.child;
        }
        if (this.childUUID == null) {
            return null;
        }
        if (this.child == null && this.level() instanceof ServerLevel serverLevel) {
            this.child = (EOTSServantSegment) serverLevel.getEntity(this.childUUID);
            return this.child;
        }
        return null;
    }

    public void setChild(@Nullable EOTSServantSegment child) {
        this.child = child;
        this.childUUID = child == null ? null : child.getUUID();
    }

    private int getIdleYPos() {
        LivingEntity owner = this.getTrueOwner();
        if (owner != null) {
            return owner.blockPosition().getY() + 12 + this.randomYOffset;
        }
        return Mth.floor(this.getY()) + this.randomYOffset;
    }

    private float getGlobalSpeedModifier() {
        return 1.25F;
    }

    private float getGlobalAttackModifier() {
        return Mth.lerp(Mth.abs(this.getHealth() / this.getMaxHealth()), 1.65F, 1.25F) * 10.0F;
    }

    public boolean isAroundIdlePos() {
        return this.getY() > (double) (this.getIdleYPos() - 2);
    }

    private void goToIdlePos() {
        float speed = 1.75F;
        if (this.isAroundIdlePos()) {
            speed = 1.0F;
        }
        var random = this.getRandom();
        int x = random.nextInt(9);
        int y;
        if (x < 8) {
            y = random.nextInt(8 - x);
        } else {
            y = 0;
        }
        LivingEntity owner = this.getTrueOwner();
        double baseX = this.getX();
        double baseZ = this.getZ();
        if (owner != null && !this.isStaying() && !this.isCommanded()) {
            baseX = owner.getX();
            baseZ = owner.getZ();
        }
        double d0 = baseX + ((random.nextFloat() * 2.0F - 1.0F) * x);
        double d2 = baseZ + ((random.nextFloat() * 2.0F - 1.0F) * y);
        this.getMoveControl().setWantedPosition(d0, this.getIdleYPos(), d2, speed);
    }

    private void goToIdlePosRelativeToDirection() {
        float speed = 2.5F;
        if (this.isAroundIdlePos()) {
            speed = 1.0F;
        }
        Vec3 lookAngle = this.getLookAngle();
        Vec2 vec2 = new Vec2((float) lookAngle.x, (float) lookAngle.z);
        vec2 = vec2.normalized();
        double d0 = this.getX() + (vec2.x * 5.0F) + this.getRandom().nextInt(2);
        double d2 = this.getZ() + (vec2.y * 5.0F) + this.getRandom().nextInt(2);
        this.getMoveControl().setWantedPosition(d0, this.getIdleYPos(), d2, speed);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.parentUUID != null) {
            tag.putUUID("Parent", this.parentUUID);
        }
        if (this.childUUID != null) {
            tag.putUUID("Child", this.childUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Parent")) {
            this.parentUUID = tag.getUUID("Parent");
            this.setControllingSegment(false);
        }
        if (tag.hasUUID("Child")) {
            this.childUUID = tag.getUUID("Child");
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, travelVector);
                this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, travelVector);
                this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
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
                this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(f));
            }
        }
        this.calculateEntityAnimation(false);
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

    protected static class EotsLookControl extends LookControl {
        private final EOTSServantSegment segment;

        public EotsLookControl(EOTSServantSegment segment) {
            super(segment);
            this.segment = segment;
        }

        @Override
        public void tick() {
            if (this.segment.isAroundIdlePos() && this.segment.isControllingSegment()) {
                super.tick();
            }
        }
    }

    protected static class EotsSegmentMoveControl extends MoveControl {
        private final EOTSServantSegment segment;
        private float baseSpeed = 0.1F;

        public EotsSegmentMoveControl(EOTSServantSegment segment) {
            super(segment);
            this.segment = segment;
        }

        @Override
        public void tick() {
            if (this.segment.isControllingSegment()) {
                double d0 = this.segment.moveControl.getWantedX() - this.segment.getX();
                double d1 = this.segment.moveControl.getWantedY() - this.segment.getY();
                double d2 = this.segment.moveControl.getWantedZ() - this.segment.getZ();
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                if (Math.abs(d3) > 9.999999747378752E-6D) {
                    double d4 = 1.0D - Math.abs(d1 * 0.699999988079071D) / d3;
                    d0 *= d4;
                    d2 *= d4;
                    d3 = Math.sqrt(d0 * d0 + d2 * d2);
                    double d5 = Math.sqrt(d0 * d0 + d2 * d2 + d1 * d1);
                    float f = this.segment.getYRot();
                    float f1 = (float) Mth.atan2(d2, d0);
                    float f2 = Mth.wrapDegrees(this.segment.getYRot() + 90.0F);
                    float f3 = Mth.wrapDegrees(f1 * 57.3F);
                    this.segment.setYRot(Mth.approachDegrees(f2, f3, this.getTurnSpeed()) - 90.0F);
                    this.segment.yBodyRot = this.segment.getYRot();
                    if (Mth.degreesDifferenceAbs(f, this.segment.getYRot()) < 3.0F) {
                        this.baseSpeed = Mth.approach(this.baseSpeed, 1.8F, 0.005F * 1.8F / this.baseSpeed);
                    } else {
                        this.baseSpeed = Mth.approach(this.baseSpeed, 0.2F, 0.025F);
                    }
                    double waveFrequency = 0.05D;
                    double waveAmplitude = 0.8D;
                    double time = this.segment.tickCount;
                    double verticalWave = Math.sin(time * waveFrequency) * waveAmplitude;
                    float f4 = (float) -(Mth.atan2(-d1 + verticalWave, d3) * 180.0D / 3.1415927410125732D);
                    this.segment.setXRot(f4);
                    float f5 = this.segment.getYRot() + 90.0F;
                    double d6 = (this.getSpeed() * Mth.cos(f5 * 0.017453292F)) * Math.abs(d0 / d5);
                    double d7 = (this.getSpeed() * Mth.sin(f5 * 0.017453292F)) * Math.abs(d2 / d5);
                    double d8 = (this.getSpeed() * Mth.sin(f4 * 0.017453292F)) * Math.abs((d1 + verticalWave) / d5);
                    Vec3 vec3 = this.segment.getDeltaMovement();
                    this.segment.setDeltaMovement(vec3.add(new Vec3(d6, d8, d7)).scale(0.2D));
                }
            }
        }

        private float getTurnSpeed() {
            if (!this.segment.isAroundIdlePos() && this.segment.isAttacking) {
                return 10.0F;
            }
            return 5.0F;
        }

        private float getSpeed() {
            return this.baseSpeed * (float) this.segment.moveControl.getSpeedModifier() * this.segment.getGlobalSpeedModifier();
        }
    }

    protected static class RandomFloatAroundGoal extends Goal {
        private final EOTSServantSegment segment;

        public RandomFloatAroundGoal(EOTSServantSegment segment) {
            this.segment = segment;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!this.segment.isControllingSegment() || !this.segment.shouldMove) {
                return false;
            }
            if (this.segment.getTrueOwner() != null && (this.segment.isStaying() || this.segment.isCommanded())) {
                return false;
            }
            MoveControl moveControl = this.segment.getMoveControl();
            if (!moveControl.hasWanted()) {
                return true;
            }
            double d0 = moveControl.getWantedX() - this.segment.getX();
            double d1 = moveControl.getWantedY() - this.segment.getY();
            double d2 = moveControl.getWantedZ() - this.segment.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            return d3 < 5.0D || d3 > 3600.0D;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            this.segment.goToIdlePos();
        }
    }

    protected static class EotsAttackGoal extends Goal {
        private final EOTSServantSegment segment;
        private int nextScanTick = 20;
        private boolean hasAttacked = false;
        private int maxFollowingTimer = 150;
        private Vec3 targetStartPos;

        private enum AttackType {
            FOLLOWING,
            SWEEPING
        }

        private AttackType attackType = AttackType.SWEEPING;

        public EotsAttackGoal(EOTSServantSegment segment) {
            this.segment = segment;
        }

        @Override
        public boolean canUse() {
            if (this.segment.isAttacking || this.segment.headGraceTicks > 0 || !this.segment.isAroundIdlePos() || !this.segment.isControllingSegment() || !this.segment.shouldMove || this.segment.getTarget() == null) {
                return false;
            }
            if (this.nextScanTick > 0) {
                this.nextScanTick--;
                return false;
            }
            if (this.segment.hasWindServantNearby()) {
                this.nextScanTick = 20;
                return false;
            }
            this.hasAttacked = false;
            this.nextScanTick = (int) (this.segment.getRandom().nextInt(20, 70) / this.segment.getGlobalAttackModifier());
            return true;
        }

        @Override
        public void start() {
            this.segment.isAttacking = true;
            if (this.segment.getRandom().nextBoolean()) {
                this.attackType = AttackType.SWEEPING;
            } else {
                this.attackType = AttackType.FOLLOWING;
            }
            this.maxFollowingTimer = 150;
            if (this.segment.getTarget() != null) {
                this.targetStartPos = this.segment.getTarget().position();
            }
            this.segment.setMouthOpen(true);
        }

        @Override
        public void stop() {
            this.segment.isAttacking = false;
            this.segment.setMouthOpen(false);
            this.updateYAxisRandomness();
        }

        private void updateYAxisRandomness() {
            this.segment.randomYOffset = this.segment.getRandom().nextInt(7);
        }

        @Override
        public boolean canContinueToUse() {
            if (this.segment.hurtTime > 0) {
                this.segment.goToIdlePosRelativeToDirection();
                return false;
            }
            if (this.hasAttacked || this.maxFollowingTimer <= 0) {
                return false;
            }
            LivingEntity livingentity = this.segment.getTarget();
            return livingentity != null && this.segment.canAttack(livingentity, TargetingConditions.DEFAULT);
        }

        @Override
        public void tick() {
            if (this.segment.getTarget() != null && this.segment.isControllingSegment()) {
                if (this.targetStartPos == null) {
                    this.hasAttacked = true;
                } else if (this.attackType == AttackType.SWEEPING) {
                    Vec3 pos = this.segment.getTarget().position();
                    this.segment.getMoveControl().setWantedPosition(pos.x, pos.y, pos.z, 1.649999976158142D);
                    if (this.segment.position().y < this.targetStartPos.y || this.segment.position().y < this.segment.getTarget().position().y) {
                        this.hasAttacked = true;
                    }
                } else {
                    Vec3 pos = this.segment.getTarget().position().add(0.0D, 1.0D, 0.0D);
                    this.segment.getMoveControl().setWantedPosition(pos.x, pos.y, pos.z, 1.5D);
                    this.maxFollowingTimer--;
                }
                if (this.segment.getBoundingBox().inflate(0.5D).intersects(this.segment.getTarget().getBoundingBox())) {
                    if (this.segment.shockwaveCooldown <= 0 && this.segment.level() instanceof ServerLevel serverLevel && this.segment.getRandom().nextInt(100) < 50) {
                        this.segment.shockwaveSpell.SpellResult(serverLevel, this.segment, ItemStack.EMPTY, this.segment.shockwaveSpell.defaultStats());
                        this.segment.shockwaveCooldown = 100;
                    } else {
                        this.segment.doHurtTarget(this.segment.getTarget());
                        this.segment.getTarget().setDeltaMovement(this.segment.getLookAngle().multiply(1.5D, 1.5D, 1.5D));
                    }
                    this.hasAttacked = true;
                }
            }
        }
    }

    protected static class EotsAirChargeGoal extends Goal {
        private int attackTimer = 25;
        private final EOTSServantSegment segment;
        private int attackDelay = 9;
        private int numberOfAttacks = 0;

        public EotsAirChargeGoal(EOTSServantSegment segment) {
            this.segment = segment;
        }

        @Override
        public boolean canUse() {
            if (this.segment.isAttacking || !this.segment.isAroundIdlePos() || !this.segment.isControllingSegment()) {
                return false;
            }
            if (this.attackTimer > 0) {
                this.attackTimer--;
                return false;
            }
            this.attackTimer = (int) (this.segment.getRandom().nextInt(25, 75) / this.segment.getGlobalAttackModifier());
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.attackDelay < -2) {
                return false;
            }
            LivingEntity livingentity = this.segment.getTarget();
            return livingentity != null && this.segment.canAttack(livingentity, TargetingConditions.DEFAULT);
        }

        @Override
        public void start() {
            this.segment.isAttacking = true;
            this.attackDelay = 13;
            this.numberOfAttacks = (int) (this.segment.getRandom().nextInt(0, 2) * this.segment.getGlobalSpeedModifier());
            this.segment.shouldMove = false;
            this.segment.setMouthOpen(true);
        }

        @Override
        public void stop() {
            this.segment.isAttacking = false;
            this.segment.shouldMove = true;
            this.segment.setMouthOpen(false);
        }

        @Override
        public void tick() {
            LivingEntity target = this.segment.getTarget();
            if (target != null) {
                this.lookAt(target);
                if (this.attackDelay <= 0) {
                    if (this.segment.level() instanceof ServerLevel serverLevel) {
                        Vec3 shootFrom = this.segment.position().add(0.0D, this.segment.getEyeHeight() * 0.8D, 0.0D);
                        Vec3 aimAt = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D);
                        Vec3 direction = aimAt.subtract(shootFrom).normalize().scale(0.699999988079071D);
                        WindCrystalSpell.shootServantCrystal(serverLevel, this.segment, direction);
                    }
                    this.segment.level().playSound(null, this.segment.getX(), this.segment.getY(), this.segment.getZ(), DeepAetherBridge.sound("entity.eots.shoot"), net.minecraft.sounds.SoundSource.HOSTILE, 2.0F, 1.0F);
                    if (this.numberOfAttacks > 0) {
                        this.numberOfAttacks--;
                        this.attackDelay = 9;
                    } else {
                        this.attackDelay = -3;
                    }
                }
            }
            this.attackDelay--;
        }

        private void lookAt(LivingEntity target) {
            double d0 = target.getX() - this.segment.getX();
            double d1 = target.getEyeY() - this.segment.getEyeY();
            double d2 = target.getZ() - this.segment.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            this.segment.setXRot(Mth.wrapDegrees((float) -(Mth.atan2(d1, d3) * 180.0D / 3.1415927410125732D)));
            this.segment.setYRot(Mth.wrapDegrees((float) (Mth.atan2(d2, d0) * 180.0D / 3.1415927410125732D) - 90.0F));
            this.segment.setYHeadRot(this.segment.getYRot());
            this.segment.xRotO = this.segment.getXRot();
            this.segment.yRotO = this.segment.getYRot();
        }
    }

    int countServants() {
        if (this.level() instanceof ServerLevel serverLevel) {
            return serverLevel.getEntitiesOfClass(BabyZephyrServant.class, this.getBoundingBox().inflate(48.0D), z -> z.getTrueOwner() == this).size();
        }
        return 0;
    }

    protected static class EotsSpellCastingGoal extends Goal {
        private final EOTSServantSegment segment;
        private int nextScanTick = 60;
        @Nullable
        private Spell chosenSpell;

        public EotsSpellCastingGoal(EOTSServantSegment segment) {
            this.segment = segment;
        }

        @Override
        public boolean canUse() {
            if (!this.segment.isAroundIdlePos() || !this.segment.isControllingSegment() || this.segment.getTarget() == null) {
                return false;
            }
            if (this.nextScanTick > 0) {
                this.nextScanTick--;
                return false;
            }
            this.nextScanTick = this.segment.getRandom().nextInt(40, 80);
            this.chosenSpell = this.pickSpell();
            return this.chosenSpell != null;
        }

        @Override
        public void start() {
            Spell spell = this.chosenSpell;
            this.chosenSpell = null;
            if (spell == null) {
                return;
            }
            if (this.segment.level() instanceof ServerLevel serverLevel) {
                SpellStat stat = spell.defaultStats();
                if (spell instanceof ThunderstormSpell) {
                    LivingEntity stormTarget = this.segment.getTarget();
                    BlockPos base = stormTarget != null ? stormTarget.blockPosition() : this.segment.blockPosition();
                    for (int i = 0; i < 20; ++i) {
                        BlockPos strikePos = base.offset(serverLevel.getRandom().nextInt(-16, 17), 0, serverLevel.getRandom().nextInt(-16, 17));
                        BlockPos altPos = base.offset(serverLevel.getRandom().nextInt(-16, 17), 0, serverLevel.getRandom().nextInt(-16, 17));
                        Vec3 strikeVec = Vec3.atBottomCenterOf(strikePos);
                        Vec3 altVec = Vec3.atBottomCenterOf(altPos);
                        MagicLightningTrap trap = new MagicLightningTrap(serverLevel, strikeVec.x, strikeVec.y, strikeVec.z);
                        trap.setOwner(this.segment);
                        trap.setDuration(40);
                        trap.setDamage(trap.getDamage() + stat.getPotency());
                        trap.setRadius((float) (trap.radius() + (stat.getRadius() / 2.0F)));
                        if (!serverLevel.getEntitiesOfClass(MagicLightningTrap.class, new AABB(strikePos)).isEmpty()) {
                            trap.setPos(altVec.x(), altVec.y(), altVec.z());
                        }
                        MobUtil.moveDownToGround(trap);
                        serverLevel.addFreshEntity(trap);
                    }
                } else {
                    spell.SpellResult(serverLevel, this.segment, ItemStack.EMPTY, stat);
                }
            }
            if (spell == this.segment.monsoonSpell) {
                this.segment.monsoonCooldown = 300;
            } else if (spell == this.segment.updraftSpell) {
                this.segment.updraftCooldown = 200;
            } else if (spell == this.segment.thunderstormSpell) {
                this.segment.thunderstormCooldown = 600;
            } else if (spell == this.segment.babyZephyrSpell) {
                this.segment.babyZephyrCooldown = 600;
            } else if (spell == this.segment.shockwaveSpell) {
                this.segment.shockwaveCooldown = 100;
            }
        }

        @Nullable
        private Spell pickSpell() {
            LivingEntity target = this.segment.getTarget();
            if (target == null) {
                return null;
            }
            double dist = this.segment.distanceTo(target);
            java.util.List<Spell> pool = new java.util.ArrayList<>();
            if (dist <= 3.5D && this.segment.shockwaveCooldown <= 0) {
                pool.add(this.segment.shockwaveSpell);
                pool.add(this.segment.shockwaveSpell);
                pool.add(this.segment.shockwaveSpell);
            }
            if (dist <= 16.0D && target.onGround() && this.segment.updraftCooldown <= 0) {
                pool.add(this.segment.updraftSpell);
                pool.add(this.segment.updraftSpell);
                pool.add(this.segment.updraftSpell);
            }
            if (this.segment.monsoonCooldown <= 0) {
                pool.add(this.segment.monsoonSpell);
                pool.add(this.segment.monsoonSpell);
            }
            if (this.segment.thunderstormCooldown <= 0) {
                pool.add(this.segment.thunderstormSpell);
            }
            if (this.segment.babyZephyrCooldown <= 0 && this.segment.countServants() < 4) {
                pool.add(this.segment.babyZephyrSpell);
                pool.add(this.segment.babyZephyrSpell);
            }
            return pool.isEmpty() ? null : pool.get(this.segment.getRandom().nextInt(pool.size()));
        }
    }
}
