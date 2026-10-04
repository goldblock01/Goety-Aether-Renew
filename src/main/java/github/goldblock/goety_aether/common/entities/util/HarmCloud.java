package github.goldblock.goety_aether.common.entities.util;

import com.Polarice3.Goety.utils.MobUtil;
import github.goldblock.goety_aether.compat.lost_aether.LostAetherCompatManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HarmCloud extends Entity implements TraceableEntity {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(HarmCloud.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(HarmCloud.class, EntityDataSerializers.BOOLEAN);
    private final Map<Entity, Integer> victims = new java.util.IdentityHashMap<>();
    private int duration = 600;
    private int waitTime = 10;
    private int reapplicationDelay = 20;
    private int durationOnUse = 0;
    private float damage = 3.0F;
    private float radiusOnUse = -0.5F;
    private float radiusPerTick = 0.0F;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    public HarmCloud(EntityType<? extends HarmCloud> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public HarmCloud(Level level, double x, double y, double z) {
        this(LostAetherCompatManager.HARM_CLOUD.get(), level);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_RADIUS, 3.0F);
        this.getEntityData().define(DATA_WAITING, false);
    }

    public void setRadius(float radius) {
        if (!this.level().isClientSide()) {
            this.getEntityData().set(DATA_RADIUS, Mth.clamp(radius, 0.0F, 32.0F));
        }
    }

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    private void setWaiting(boolean waiting) {
        this.getEntityData().set(DATA_WAITING, waiting);
    }

    public boolean isWaiting() {
        return this.getEntityData().get(DATA_WAITING);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void setReapplicationDelay(int delay) {
        this.reapplicationDelay = delay;
    }

    public void setDurationOnUse(int durationOnUse) {
        this.durationOnUse = durationOnUse;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setRadiusOnUse(float radiusOnUse) {
        this.radiusOnUse = radiusOnUse;
    }

    public void setRadiusPerTick(float radiusPerTick) {
        this.radiusPerTick = radiusPerTick;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUUID = owner == null ? null : owner.getUUID();
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity livingEntity) {
                this.owner = livingEntity;
            }
        }
        return this.owner;
    }

    private boolean areAllies(LivingEntity target) {
        return MobUtil.areAllies(target, this.getOwner());
    }

    @Override
    public void tick() {
        super.tick();
        boolean waiting = this.isWaiting();
        float radius = this.getRadius();
        if (this.level().isClientSide()) {
            if (waiting && this.random.nextBoolean()) {
                return;
            }
            int count;
            float spread;
            if (waiting) {
                count = 2;
                spread = 0.2F;
            } else {
                count = Mth.ceil((float) Math.PI * radius * radius);
                spread = radius;
            }
            for (int i = 0; i < count; ++i) {
                float angle = this.random.nextFloat() * ((float) Math.PI * 2.0F);
                float dist = Mth.sqrt(this.random.nextFloat()) * spread;
                double px = this.getX() + (double) (Mth.cos(angle) * dist);
                double pz = this.getZ() + (double) (Mth.sin(angle) * dist);
                double vx;
                double vy;
                double vz;
                if (waiting) {
                    vx = 0.0D;
                    vy = 0.0D;
                    vz = 0.0D;
                } else {
                    vx = (0.5D - this.random.nextDouble()) * 0.15D;
                    vy = 0.01D;
                    vz = (0.5D - this.random.nextDouble()) * 0.15D;
                }
                this.level().addAlwaysVisibleParticle(ParticleTypes.CLOUD, px, this.getY(), pz, vx, vy, vz);
            }
        } else {
            if (this.tickCount >= this.waitTime + this.duration) {
                this.discard();
                return;
            }
            boolean waitingNow = this.tickCount < this.waitTime;
            if (waiting != waitingNow) {
                this.setWaiting(waitingNow);
            }
            if (waitingNow) {
                return;
            }
            if (this.radiusPerTick != 0.0F) {
                radius += this.radiusPerTick;
                if (radius < 0.5F) {
                    this.discard();
                    return;
                }
                this.setRadius(radius);
            }
            this.victims.entrySet().removeIf(entry -> this.tickCount >= entry.getValue());
            List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
            if (!list.isEmpty()) {
                for (LivingEntity living : list) {
                    if (!this.victims.containsKey(living) && !this.areAllies(living)) {
                        double dx = living.getX() - this.getX();
                        double dz = living.getZ() - this.getZ();
                        if (dx * dx + dz * dz <= (double) (radius * radius)) {
                            this.victims.put(living, this.tickCount + this.reapplicationDelay);
                            DamageSource source = this.damageSources().indirectMagic(this, this.getOwner());
                            living.hurt(source, this.getDamage());
                            if (this.radiusOnUse != 0.0F) {
                                radius += this.radiusOnUse;
                                if (radius < 0.5F) {
                                    this.discard();
                                    return;
                                }
                                this.setRadius(radius);
                            }
                            if (this.durationOnUse != 0) {
                                this.duration += this.durationOnUse;
                                if (this.duration <= 0) {
                                    this.discard();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.5F);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_RADIUS.equals(accessor)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickCount = tag.getInt("Age");
        this.duration = tag.getInt("Duration");
        this.waitTime = tag.getInt("WaitTime");
        this.reapplicationDelay = tag.getInt("ReapplicationDelay");
        this.durationOnUse = tag.getInt("DurationOnUse");
        this.damage = tag.getFloat("Damage");
        this.radiusOnUse = tag.getFloat("RadiusOnUse");
        this.radiusPerTick = tag.getFloat("RadiusPerTick");
        this.setRadius(tag.getFloat("Radius"));
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", this.tickCount);
        tag.putInt("Duration", this.duration);
        tag.putInt("WaitTime", this.waitTime);
        tag.putInt("ReapplicationDelay", this.reapplicationDelay);
        tag.putInt("DurationOnUse", this.durationOnUse);
        tag.putFloat("Damage", this.damage);
        tag.putFloat("RadiusOnUse", this.radiusOnUse);
        tag.putFloat("RadiusPerTick", this.radiusPerTick);
        tag.putFloat("Radius", this.getRadius());
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
    }
}
