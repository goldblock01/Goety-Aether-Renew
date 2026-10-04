package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.Goety.common.entities.projectiles.HellBlast;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.MathHelper;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.Goety.utils.MobUtil;
import com.aetherteam.aether.AetherTags;
import com.aetherteam.aether.client.AetherSoundEvents;
import github.goldblock.goety_aether.common.entities.projectile.FireCrystal;
import github.goldblock.goety_aether.common.entities.projectile.IceCrystal;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class SunSpiritServant extends Summoned implements RangedAttackMob {
    private static final EntityDataAccessor<Boolean> DATA_IS_FROZEN = SynchedEntityData.defineId(SunSpiritServant.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_FROZEN_DURATION = SynchedEntityData.defineId(SunSpiritServant.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> AUTO_MODE = SynchedEntityData.defineId(SunSpiritServant.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_CASTING_SPELL = SynchedEntityData.defineId(SunSpiritServant.class, EntityDataSerializers.BOOLEAN);
    private static final int FROZEN_DURATION = 175;
    private static final int SHOOT_CRYSTAL_INTERVAL = 5;
    private static final int HIT_THRESHOLD = 5;

    private int crystalCount;
    private int summonCooldown;
    private int spellCooldown;
    private int infernoCoolDown;
    private int hitCount;

    public SunSpiritServant(EntityType<? extends SunSpiritServant> type, Level level) {
        super(type, level);
        this.xpReward = 0;
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CastingSpellGoal());
        this.goalSelector.addGoal(2, new SummonFireMinionSpellGoal());
        this.goalSelector.addGoal(3, new FireHellBlastGoal());
        this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.25, 40, 20.0F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FLYING_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_FROZEN, false);
        this.entityData.define(DATA_FROZEN_DURATION, 0);
        this.entityData.define(AUTO_MODE, false);
        this.entityData.define(IS_CASTING_SPELL, false);
    }

    public boolean isFrozen() {
        return this.entityData.get(DATA_IS_FROZEN);
    }

    public void setFrozen(boolean frozen) {
        this.entityData.set(DATA_IS_FROZEN, frozen);
    }

    public int getFrozenDuration() {
        return this.entityData.get(DATA_FROZEN_DURATION);
    }

    public void setFrozenDuration(int duration) {
        this.entityData.set(DATA_FROZEN_DURATION, duration);
    }

    public void setAutonomous(boolean autonomous) {
        this.entityData.set(AUTO_MODE, autonomous);
        if (autonomous && !this.isWandering()) {
            this.setWandering(true);
            this.setStaying(false);
        }
    }

    public boolean isAutonomous() {
        return this.entityData.get(AUTO_MODE);
    }

    public void setSpellCasting(boolean casting) {
        this.entityData.set(IS_CASTING_SPELL, casting);
    }

    public boolean isSpellCasting() {
        return this.entityData.get(IS_CASTING_SPELL);
    }

    public int getSpellCooldown() {
        return this.spellCooldown;
    }

    public void setSpellCooldown(int cooldown) {
        this.spellCooldown = cooldown;
    }

    public int getInfernoCoolDown() {
        return this.infernoCoolDown;
    }

    public void setInfernoCoolDown(int coolDown) {
        this.infernoCoolDown = coolDown;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("AutoMode", this.isAutonomous());
        compound.putBoolean("IsFrozen", this.isFrozen());
        compound.putInt("FrozenDuration", this.getFrozenDuration());
        compound.putInt("HitCount", this.hitCount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("AutoMode")) {
            this.setAutonomous(compound.getBoolean("AutoMode"));
        }
        if (compound.contains("IsFrozen")) {
            this.setFrozen(compound.getBoolean("IsFrozen"));
        }
        if (compound.contains("FrozenDuration")) {
            this.setFrozenDuration(compound.getInt("FrozenDuration"));
        }
        if (compound.contains("HitCount")) {
            this.hitCount = compound.getInt("HitCount");
        }
        if (compound.contains("InfernoCoolDown")) {
            this.infernoCoolDown = compound.getInt("InfernoCoolDown");
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level().isClientSide && this.getTrueOwner() == player) {
            ItemStack itemstack = player.getItemInHand(hand);
            if (itemstack.isEmpty() && player.isCrouching()) {
                this.setAutonomous(!this.isAutonomous());
                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.getFrozenDuration() > 0) {
                this.setFrozenDuration(this.getFrozenDuration() - 1);
            } else {
                this.setFrozen(false);
            }
            if (this.summonCooldown > 0) {
                --this.summonCooldown;
            }
            if (this.spellCooldown > 0) {
                --this.spellCooldown;
            }
            if (this.infernoCoolDown > 0) {
                --this.infernoCoolDown;
            }
        }
        if (this.level().isClientSide && this.random.nextInt(4) == 0) {
            this.level()
                    .addParticle(
                            ParticleTypes.FLAME,
                            this.getX() + (this.random.nextDouble() - 0.5) * (double) this.getBbWidth(),
                            this.getY() + this.random.nextDouble() * (double) this.getBbHeight(),
                            this.getZ() + (this.random.nextDouble() - 0.5) * (double) this.getBbWidth(),
                            0.0,
                            0.0,
                            0.0
                    );
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive() && !this.isFrozen()) {
            this.burnEntities();
        }
    }

    private void burnEntities() {
        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(0.0, -2.0, 0.0).expandTowards(-0.75, 0.0, -0.75).expandTowards(0.75, 0.0, 0.75))) {
            if (entity instanceof LivingEntity living && living != this.getTrueOwner() && !this.isAlliedTo(living)) {
                entity.hurt(this.damageSources().mobAttack(this), 3.0F);
                entity.setRemainingFireTicks(40);
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (this.isDeadOrDying()) {
            return true;
        }
        if (this.isFrozen()) {
            return false;
        }
        if (source.is(ModDamageSource.DISMISSED)) {
            return false;
        }
        if (source.is(DamageTypeTags.IS_FALL)) {
            return true;
        }
        if (source.is(DamageTypeTags.IS_FIRE)) {
            return true;
        }
        if (source.getDirectEntity() == null && source.getEntity() == null) {
            return false;
        }
        return !source.is(AetherTags.DamageTypes.IS_COLD);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean isFireOrCold = source.is(DamageTypeTags.IS_FIRE) || source.is(AetherTags.DamageTypes.IS_COLD);
        boolean hasNoSource = source.getDirectEntity() == null && source.getEntity() == null;
        if (!this.isFrozen()) {
            if (!this.level().isClientSide) {
                if (source.getDirectEntity() instanceof com.aetherteam.aether.entity.projectile.crystal.IceCrystal) {
                    this.setFrozen(true);
                    this.setFrozenDuration(FROZEN_DURATION);
                    this.hitCount = 0;
                    if (source.getEntity() instanceof LivingEntity livingEntity) {
                        this.spawnFireMinion(livingEntity);
                    }
                } else if (!hasNoSource) {
                    ++this.hitCount;
                    if (this.hitCount >= HIT_THRESHOLD) {
                        this.setFrozen(true);
                        this.setFrozenDuration(FROZEN_DURATION);
                        this.hitCount = 0;
                        if (source.getEntity() instanceof LivingEntity livingEntity) {
                            this.spawnFireMinion(livingEntity);
                        }
                    }
                }

                if (!isFireOrCold) {
                    this.playSound(this.getHurtSound(source), this.getSoundVolume(), this.getVoicePitch());
                }
            }
        }

        if (!this.isFrozen() && hasNoSource) {
            amount *= 0.5F;
        }

        return super.hurt(source, amount);
    }

    private void spawnFireMinion(LivingEntity target) {
        if (this.level() instanceof ServerLevel serverLevel) {
            FireMinionServant servant = new FireMinionServant(ModEntityTypes.FIRE_MINION_SERVANT.get(), serverLevel);
            BlockPos blockPos = BlockFinder.SummonRadius(this.blockPosition(), servant, serverLevel);
            servant.setTrueOwner(this);
            servant.moveTo(blockPos, 0.0F, 0.0F);
            MobUtil.moveDownToGround(servant);
            servant.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            servant.setTarget(target);
            if (serverLevel.addFreshEntity(servant)) {
                this.playSound(AetherSoundEvents.ENTITY_SUN_SPIRIT_SHOOT_FIRE.get(), 1.0F, 1.0F);
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (!this.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            Vec3 aim = new Vec3(target.getX() - this.getX(), target.getY(0.3333333333333333) - (this.getY() + 1.0D), target.getZ() - this.getZ()).normalize();
            if (--this.crystalCount <= 0) {
                IceCrystal crystal = new IceCrystal(serverLevel, this, 0.0F, false, aim);
                serverLevel.addFreshEntity(crystal);
                this.crystalCount = SHOOT_CRYSTAL_INTERVAL;
            } else {
                FireCrystal crystal = new FireCrystal(serverLevel, this, 0.0F, aim);
                serverLevel.addFreshEntity(crystal);
            }
            this.playSound(AetherSoundEvents.ENTITY_SUN_SPIRIT_SHOOT_FIRE.get(), 1.0F, 1.0F);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AetherSoundEvents.ENTITY_SUN_SPIRIT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AetherSoundEvents.ENTITY_SUN_SPIRIT_DEATH.get();
    }

    @Override
    public void die(DamageSource pCause) {
        if (this.getTrueOwner() != null) {
            ItemStack itemStack = new ItemStack(github.goldblock.goety_aether.common.init.ModItems.SWOLLEN_SUN.get());
            github.goldblock.goety_aether.common.items.revive.ReviveServantItem.setOwnerName(this.getTrueOwner(), itemStack);
            github.goldblock.goety_aether.common.items.revive.ReviveServantItem.setSummon(this, itemStack);
            net.minecraft.world.entity.item.ItemEntity itemEntity = this.spawnAtLocation(itemStack);
            if (itemEntity != null) {
                itemEntity.setExtendedLifetime();
            }
        }
        super.die(pCause);
    }

    class CastingSpellGoal extends Goal {
        CastingSpellGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            return SunSpiritServant.this.isSpellCasting();
        }

        public void start() {
            SunSpiritServant.this.navigation.stop();
        }

        public void tick() {
            if (SunSpiritServant.this.getTarget() != null) {
                SunSpiritServant.this.getLookControl().setLookAt(SunSpiritServant.this.getTarget(), 30.0F, 30.0F);
            }
        }
    }

    static class FloatGoal extends Goal {
        private final SunSpiritServant entity;

        public FloatGoal(SunSpiritServant entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return this.entity.isInWater() || this.entity.isInLava();
        }

        @Override
        public void tick() {
            if (this.entity.getRandom().nextFloat() < 0.8F) {
                this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(0.0, 0.05, 0.0));
            }
        }
    }

    static class RangedAttackGoal extends Goal {
        private final SunSpiritServant entity;
        @Nullable
        private LivingEntity target;
        private int attackTime = -1;
        private final double speedModifier;
        private int seeTime;
        private final int attackInterval;
        private final float attackRadius;
        private final float attackRadiusSqr;

        public RangedAttackGoal(SunSpiritServant entity, double speed, int attackInterval, float attackRadius) {
            this.entity = entity;
            this.speedModifier = speed;
            this.attackInterval = attackInterval;
            this.attackRadius = attackRadius;
            this.attackRadiusSqr = attackRadius * attackRadius;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.entity.isSpellCasting()) {
                return false;
            }
            LivingEntity livingentity = this.entity.getTarget();
            if (livingentity != null && livingentity.isAlive()) {
                this.target = livingentity;
                return true;
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return !this.entity.isSpellCasting() && (this.canUse() || this.target != null && this.target.isAlive() && !this.entity.getNavigation().isDone());
        }

        @Override
        public void stop() {
            this.target = null;
            this.seeTime = 0;
            this.attackTime = -1;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.target != null && !this.entity.isSpellCasting()) {
                double d0 = this.entity.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
                boolean flag = this.entity.getSensing().hasLineOfSight(this.target);
                if (flag) {
                    ++this.seeTime;
                } else {
                    this.seeTime = 0;
                }

                if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 5) {
                    this.entity.getNavigation().stop();
                } else {
                    this.entity.getNavigation().moveTo(this.target, this.speedModifier);
                }

                this.entity.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
                if (--this.attackTime == 0) {
                    if (!flag) {
                        return;
                    }
                    float f = (float) Math.sqrt(d0) / this.attackRadius;
                    float f1 = Math.min(f, 1.0F);
                    this.entity.performRangedAttack(this.target, f1);
                    this.attackTime = this.attackInterval;
                } else if (this.attackTime < 0) {
                    this.attackTime = this.attackInterval;
                }
            }
        }
    }

    class SummonFireMinionSpellGoal extends Goal {
        protected int spellTime;

        SummonFireMinionSpellGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = SunSpiritServant.this.getTarget();
            if (target == null || !target.isAlive()) {
                return false;
            }
            if (SunSpiritServant.this.isSpellCasting()) {
                return false;
            }
            if (SunSpiritServant.this.isFrozen()) {
                return false;
            }
            return SunSpiritServant.this.summonCooldown <= 0 && SunSpiritServant.this.spellCooldown <= 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.spellTime > 0;
        }

        @Override
        public void start() {
            this.spellTime = 20;
            SunSpiritServant.this.summonCooldown = 200;
            SunSpiritServant.this.spellCooldown = 40;
            SunSpiritServant.this.setSpellCasting(true);
            SunSpiritServant.this.navigation.stop();
            SunSpiritServant.this.playSound(AetherSoundEvents.ENTITY_SUN_SPIRIT_SHOOT_FIRE.get(), 1.0F, 1.0F);
        }

        @Override
        public void stop() {
            this.spellTime = 0;
            SunSpiritServant.this.setSpellCasting(false);
        }

        @Override
        public void tick() {
            --this.spellTime;
            if (SunSpiritServant.this.getTarget() != null) {
                SunSpiritServant.this.getLookControl().setLookAt(SunSpiritServant.this.getTarget(), 30.0F, 30.0F);
            }
            if (this.spellTime == 0) {
                if (SunSpiritServant.this.level() instanceof ServerLevel serverLevel) {
                    FireMinionServant servant = new FireMinionServant(ModEntityTypes.FIRE_MINION_SERVANT.get(), serverLevel);
                    BlockPos blockPos = BlockFinder.SummonRadius(SunSpiritServant.this.blockPosition(), servant, serverLevel);
                    servant.setTrueOwner(SunSpiritServant.this);
                    servant.moveTo(blockPos, 0.0F, 0.0F);
                    MobUtil.moveDownToGround(servant);
                    servant.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(SunSpiritServant.this.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                    if (SunSpiritServant.this.getTarget() != null) {
                        servant.setTarget(SunSpiritServant.this.getTarget());
                    }
                    serverLevel.addFreshEntity(servant);
                    SunSpiritServant.this.setSpellCasting(false);
                }
            }
        }
    }

    class FireHellBlastGoal extends Goal {
        FireHellBlastGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (SunSpiritServant.this.isFrozen()) {
                return false;
            }
            LivingEntity target = SunSpiritServant.this.getTarget();
            if (target == null || !target.isAlive()) {
                return false;
            }
            if (SunSpiritServant.this.infernoCoolDown > 0) {
                return false;
            }
            LivingEntity owner = SunSpiritServant.this.getTrueOwner();
            if (owner == null) {
                return false;
            }
            return CuriosFinder.hasUnholySet(owner);
        }

        @Override
        public void start() {
            SunSpiritServant.this.infernoCoolDown = MathHelper.secondsToTicks(45);
            LivingEntity target = SunSpiritServant.this.getTarget();
            if (target != null) {
                double d1 = target.getX() - SunSpiritServant.this.getX();
                double d2 = target.getY(0.5D) - SunSpiritServant.this.getY(0.5D);
                double d3 = target.getZ() - SunSpiritServant.this.getZ();
                HellBlast hellBlast = new HellBlast(SunSpiritServant.this, d1, d2, d3, SunSpiritServant.this.level());
                hellBlast.setPos(hellBlast.getX(), SunSpiritServant.this.getY(0.5), hellBlast.getZ());
                SunSpiritServant.this.level().addFreshEntity(hellBlast);
                SunSpiritServant.this.playSound(AetherSoundEvents.ENTITY_SUN_SPIRIT_SHOOT_FIRE.get(), 1.0F, 1.0F);
            }
        }
    }

    @Override
    public void tryKill(Player player) {
        if (this.killChance <= 0) {
            this.warnKill(player);
        } else {
            super.tryKill(player);
        }
    }
}
