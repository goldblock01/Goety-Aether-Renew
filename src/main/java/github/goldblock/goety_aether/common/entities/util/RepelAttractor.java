package github.goldblock.goety_aether.common.entities.util;

import com.Polarice3.Goety.utils.MobUtil;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.UUID;

public class RepelAttractor extends Entity implements TraceableEntity {
    private static final double PUSH_SPEED = 0.35D;
    private static final int PULSE_INTERVAL = 20;
    private static final int PULSE_LENGTH = 8;
    private static final int SEAL_SPOKES = 8;

    private static final Vector3f WARD_DEEP = new Vector3f(0.35F, 0.55F, 0.13F);
    private static final Vector3f WARD_LIGHT = new Vector3f(0.66F, 0.88F, 0.35F);
    private static final Vector3f WARD_PALE = new Vector3f(0.87F, 0.98F, 0.72F);

    private static final DustParticleOptions RUNE_DEEP = new DustParticleOptions(WARD_DEEP, 0.7F);
    private static final DustParticleOptions RUNE_LIGHT = new DustParticleOptions(WARD_LIGHT, 0.7F);
    private static final DustParticleOptions RUNE_PALE = new DustParticleOptions(WARD_PALE, 0.7F);
    private static final DustParticleOptions MOTE_LIGHT = new DustParticleOptions(WARD_LIGHT, 1.25F);
    private static final DustParticleOptions MOTE_PALE = new DustParticleOptions(WARD_PALE, 1.25F);

    private int duration = 60;
    private float radius = 8.0F;
    private double strength = 1.0D;
    private int pulseAge = 0;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    public RepelAttractor(EntityType<? extends RepelAttractor> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public RepelAttractor(Level level, double x, double y, double z) {
        this(ModEntityTypes.REPEL_ATTRACTOR.get(), level);
        this.setPos(x, y, z);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setStrength(double strength) {
        this.strength = strength;
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

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount >= this.duration) {
            if (this.level() instanceof ServerLevel serverLevel) {
                this.closingRite(serverLevel);
            }
            this.discard();
            return;
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            this.sourceRite(serverLevel);
            this.sealRite(serverLevel);
            this.outflowRite(serverLevel);
            if (this.tickCount % PULSE_INTERVAL == 0) {
                this.pulseAge = PULSE_LENGTH;
            }
            if (this.pulseAge > 0) {
                this.pulseRite(serverLevel);
                --this.pulseAge;
            }

            LivingEntity owner = this.getOwner();
            for (Entity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(this.radius))) {
                if (entity == owner) {
                    continue;
                }
                if (owner != null && (MobUtil.areAllies(owner, entity) || MobUtil.getOwner(entity) == owner)) {
                    continue;
                }
                double distance = 1.0D - entity.distanceTo(this) / this.radius;
                double scale = distance * PUSH_SPEED * this.strength;
                Vec3 vec3 = entity.position().subtract(this.position());
                vec3 = vec3.normalize().scale(scale);
                MobUtil.push(entity, vec3.x, vec3.y, vec3.z, 0.5D);
            }
        }
    }

    private void mote(ServerLevel serverLevel, ParticleOptions option, double x, double y, double z, double vx, double vy, double vz) {
        serverLevel.sendParticles(option, x, y, z, 0, vx * 10.0D, vy * 10.0D, vz * 10.0D, 1.0D);
    }

    private void sourceRite(ServerLevel serverLevel) {
        double cx = this.getX();
        double cz = this.getZ();
        double base = this.getY() + 0.08D;

        this.mote(serverLevel, MOTE_PALE, cx, base + 0.26D, cz, 0.0D, 0.0D, 0.0D);

        for (int i = 0; i < 2; ++i) {
            double h = 0.85D + i * 0.8D + (this.tickCount % 8) * 0.07D;
            this.mote(serverLevel, i == 0 ? MOTE_LIGHT : RUNE_PALE,
                    cx + (this.random.nextDouble() - 0.5D) * 0.06D,
                    base + h,
                    cz + (this.random.nextDouble() - 0.5D) * 0.06D,
                    0.0D, 0.025D, 0.0D);
        }

        if (this.tickCount % 4 == 0) {
            for (int i = 0; i < 4; ++i) {
                double angle = Math.PI * 0.5D * i + Math.PI * 0.25D;
                this.mote(serverLevel, RUNE_LIGHT,
                        cx + Math.cos(angle) * 0.45D, base + 0.06D, cz + Math.sin(angle) * 0.45D,
                        0.0D, 0.0D, 0.0D);
            }
        }
    }

    private void sealRite(ServerLevel serverLevel) {
        float radius = this.getRadius();
        double cx = this.getX();
        double cz = this.getZ();
        double y = this.getY() + 0.12D;
        double spin = this.tickCount * 0.02D;
        for (int i = 0; i < 2; ++i) {
            int spoke = (this.tickCount / 4 + i * (SEAL_SPOKES / 2)) % SEAL_SPOKES;
            double angle = spin + Math.PI * 2.0D * spoke / SEAL_SPOKES;
            double r = radius * (0.86D + 0.05D * Math.sin(this.tickCount * 0.15D + spoke));
            this.mote(serverLevel, spoke % 2 == 0 ? RUNE_LIGHT : RUNE_DEEP,
                    cx + Math.cos(angle) * r, y, cz + Math.sin(angle) * r,
                    0.0D, 0.0D, 0.0D);
        }
    }

    private void outflowRite(ServerLevel serverLevel) {
        float radius = this.getRadius();
        double cx = this.getX();
        double cz = this.getZ();
        double angle = this.random.nextDouble() * Math.PI * 2.0D;
        double r = radius * (0.12D + this.random.nextDouble() * 0.22D);
        double speed = 0.18D;
        this.mote(serverLevel, this.random.nextInt(3) == 0 ? MOTE_PALE : MOTE_LIGHT,
                cx + Math.cos(angle) * r,
                this.getY() + 0.2D + this.random.nextDouble() * 0.9D,
                cz + Math.sin(angle) * r,
                Math.cos(angle) * speed, 0.01D, Math.sin(angle) * speed);
    }

    private void pulseRite(ServerLevel serverLevel) {
        float radius = this.getRadius();
        double cx = this.getX();
        double cz = this.getZ();
        double t = (PULSE_LENGTH - this.pulseAge) / (double) PULSE_LENGTH;
        double r = radius * (0.08D + t * 0.86D);
        double spin = this.tickCount * 0.03D;
        for (int i = 0; i < 6; ++i) {
            double angle = spin + Math.PI * 2.0D * i / 6.0D;
            this.mote(serverLevel, i % 2 == 0 ? RUNE_PALE : MOTE_LIGHT,
                    cx + Math.cos(angle) * r,
                    this.getY() + 0.15D + t * 0.3D,
                    cz + Math.sin(angle) * r,
                    Math.cos(angle) * 0.06D, 0.015D, Math.sin(angle) * 0.06D);
        }
    }

    private void closingRite(ServerLevel serverLevel) {
        float radius = this.getRadius();
        double cx = this.getX();
        double cz = this.getZ();
        double y = this.getY() + 0.15D;
        serverLevel.sendParticles(MOTE_PALE, cx, y + 0.35D, cz, 5, 0.25D, 0.2D, 0.25D, 0.01D);
        for (int i = 0; i < 10; ++i) {
            double angle = Math.PI * 2.0D * i / 10.0D;
            this.mote(serverLevel, i % 2 == 0 ? RUNE_LIGHT : RUNE_PALE,
                    cx + Math.cos(angle) * radius * 0.8D, y, cz + Math.sin(angle) * radius * 0.8D,
                    Math.cos(angle) * 0.13D, 0.05D, Math.sin(angle) * 0.13D);
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickCount = tag.getInt("Age");
        this.duration = tag.getInt("Duration");
        this.radius = tag.getFloat("Radius");
        this.strength = tag.getDouble("Strength");
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", this.tickCount);
        tag.putInt("Duration", this.duration);
        tag.putFloat("Radius", this.radius);
        tag.putDouble("Strength", this.strength);
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(this.radius * 2.0F, 0.5F);
    }

    @Override
    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }
}
