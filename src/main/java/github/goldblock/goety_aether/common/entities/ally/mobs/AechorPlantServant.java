package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.aetherteam.aether.client.AetherSoundEvents;
import com.aetherteam.aether.entity.projectile.PoisonNeedle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class AechorPlantServant extends Summoned implements RangedAttackMob {
    private static final EntityDataAccessor<Integer> DATA_SIZE_ID = SynchedEntityData.defineId(AechorPlantServant.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_POISON_REMAINING_ID = SynchedEntityData.defineId(AechorPlantServant.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_TARGETING_ENTITY_ID = SynchedEntityData.defineId(AechorPlantServant.class, EntityDataSerializers.BOOLEAN);

    private float sinage;
    private float sinageAdd;

    public AechorPlantServant(EntityType<? extends Summoned> type, Level level) {
        super(type, level);
        this.xpReward = 5;
        this.setPoisonRemaining(2);
        if (level.isClientSide()) {
            this.sinage = this.getRandom().nextFloat() * 6.0F;
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Summoned.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new RangedAttackGoal(this, 0.0D, 60, 10.0F));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SIZE_ID, 0);
        this.entityData.define(DATA_POISON_REMAINING_ID, 0);
        this.entityData.define(DATA_TARGETING_ENTITY_ID, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        if (DATA_SIZE_ID.equals(dataAccessor)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(dataAccessor);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide()) {
            this.sinage += this.sinageAdd;
            if (this.hurtTime > 0) {
                this.sinageAdd = 0.45F;
            } else if (this.getTargetingEntity()) {
                this.sinageAdd = 0.3F;
            } else {
                this.sinageAdd = 0.15F;
            }
            if (this.sinage >= 6.2831855F) {
                this.sinage -= 6.2831855F;
            }
        } else if (this.getTarget() != null) {
            this.setTargetingEntity(true);
        } else if (this.getTargetingEntity()) {
            this.setTargetingEntity(false);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.hurtTime == 0) {
            for (int i = 0; i < 8; i++) {
                double d1 = this.getX() + (this.getRandom().nextDouble() - this.getRandom().nextDouble()) * 0.5D;
                double d2 = this.getY() + 0.25D + (this.getRandom().nextDouble() - this.getRandom().nextDouble()) * 0.5D;
                double d3 = this.getZ() + (this.getRandom().nextDouble() - this.getRandom().nextDouble()) * 0.5D;
                double d4 = (this.getRandom().nextDouble() - this.getRandom().nextDouble()) * 0.5D;
                double d5 = (this.getRandom().nextDouble() - this.getRandom().nextDouble()) * 0.5D;
                this.level().addParticle(ParticleTypes.CRIT, d1, d2, d3, d4, 0.25D, d5);
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        PoisonNeedle needle = new PoisonNeedle(this.level(), this);
        double x = target.getX() - this.getX();
        double z = target.getZ() - this.getZ();
        double sqrt = Math.sqrt(x * x + z * z + 0.1D);
        double y = 0.1D + sqrt * 0.5D + (this.getY() - target.getY()) * 0.25D;
        double distance = 1.5D / sqrt;
        x *= distance;
        z *= distance;
        needle.shoot(x, y + 0.5D, z, 0.285F + (float) y * 0.08F, 1.0F);
        this.playSound(AetherSoundEvents.ENTITY_AECHOR_PLANT_SHOOT.get(), 2.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(needle);
    }

    public int getSize() {
        return this.entityData.get(DATA_SIZE_ID);
    }

    public void setSize(int size) {
        this.entityData.set(DATA_SIZE_ID, size);
    }

    public int getPoisonRemaining() {
        return this.entityData.get(DATA_POISON_REMAINING_ID);
    }

    public void setPoisonRemaining(int poisonRemaining) {
        this.entityData.set(DATA_POISON_REMAINING_ID, poisonRemaining);
    }

    public boolean getTargetingEntity() {
        return this.entityData.get(DATA_TARGETING_ENTITY_ID);
    }

    public void setTargetingEntity(boolean targetingEntity) {
        this.entityData.set(DATA_TARGETING_ENTITY_ID, targetingEntity);
    }

    public float getSinage() {
        return this.sinage;
    }

    public float getSinageAdd() {
        return this.sinageAdd;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return AetherSoundEvents.ENTITY_AECHOR_PLANT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AetherSoundEvents.ENTITY_AECHOR_PLANT_DEATH.get();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        float width = 0.75F + this.getSize() * 0.125F;
        float height = 0.5F + this.getSize() * 0.075F;
        return EntityDimensions.scalable(width, height);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return size.height / 1.15F;
    }

    @Override
    public double getMyRidingOffset() {
        return this.getVehicle() != null && this.getVehicle().isShiftKeyDown() ? 0.1D : 0.275D;
    }

    @Override
    protected boolean isImmobile() {
        return true;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return effect.getEffect() != com.aetherteam.aether.effect.AetherEffects.INEBRIATION.get() && super.canBeAffected(effect);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Size", this.getSize());
        tag.putInt("Poison Remaining", this.getPoisonRemaining());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Size")) {
            this.setSize(tag.getInt("Size"));
        }
        if (tag.contains("Poison Remaining")) {
            this.setPoisonRemaining(tag.getInt("Poison Remaining"));
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag) {
        this.setSize(level.getRandom().nextInt(4) + 1);
        this.moveTo(Vec3.atBottomCenterOf(this.blockPosition()));
        this.refreshDimensions();
        return super.finalizeSpawn(level, difficulty, reason, spawnData, tag);
    }
}
