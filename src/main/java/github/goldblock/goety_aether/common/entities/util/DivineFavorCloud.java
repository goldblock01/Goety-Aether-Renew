package github.goldblock.goety_aether.common.entities.util;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.client.particles.CircleExplodeParticleOption;
import com.Polarice3.Goety.client.particles.SphereExplodeParticleOption;
import com.aetherteam.aether.client.particle.AetherParticleTypes;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class DivineFavorCloud extends Entity implements TraceableEntity {
    private static final int HEAL_INTERVAL = 20;
    private static final int BASE_HEAL_AMOUNT = 1;
    private static final int OPENING_LENGTH = 24;
    private static final int BEAM_LENGTH = 12;
    private static final int PULSE_LENGTH = 8;
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(DivineFavorCloud.class, EntityDataSerializers.FLOAT);

    private static final Vector3f GOLD = new Vector3f(1.0F, 0.85F, 0.2F);
    private static final Vector3f GOLD_BRIGHT = new Vector3f(1.0F, 0.96F, 0.6F);
    private static final Vector3f HOLY_WHITE = new Vector3f(0.95F, 0.98F, 1.0F);

    private static final DustParticleOptions RUNE_GOLD = new DustParticleOptions(GOLD, 0.8F);
    private static final DustParticleOptions RUNE_BRIGHT = new DustParticleOptions(GOLD_BRIGHT, 0.8F);
    private static final DustParticleOptions RUNE_WHITE = new DustParticleOptions(HOLY_WHITE, 0.8F);
    private static final DustParticleOptions MOTE_GOLD = new DustParticleOptions(GOLD, 1.3F);
    private static final DustParticleOptions MOTE_BRIGHT = new DustParticleOptions(GOLD_BRIGHT, 1.3F);
    private static final DustParticleOptions MOTE_WHITE = new DustParticleOptions(HOLY_WHITE, 1.3F);

    private int duration = 160;
    private int potency = 0;
    private int pulseAge = 0;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    public DivineFavorCloud(EntityType<? extends DivineFavorCloud> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public DivineFavorCloud(Level level, double x, double y, double z) {
        this(ModEntityTypes.DIVINE_FAVOR_CLOUD.get(), level);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_RADIUS, 5.0F);
    }

    public void setRadius(float radius) {
        this.getEntityData().set(DATA_RADIUS, radius);
        this.refreshDimensions();
    }

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPotency(int potency) {
        this.potency = potency;
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
        if (this.level() instanceof ServerLevel serverLevel) {
            this.serverTick(serverLevel);
        } else {
            this.clientTick();
        }
    }

    private void serverTick(ServerLevel serverLevel) {
        if (this.tickCount >= this.duration) {
            this.closingRite(serverLevel);
            this.discard();
            return;
        }
        if (this.tickCount <= OPENING_LENGTH) {
            this.openingRite(serverLevel);
        }
        if (this.tickCount == 1) {
            this.shockwaveRite(serverLevel);
        }
        if (this.tickCount % HEAL_INTERVAL == 0) {
            this.healPulse(serverLevel);
        }
        if (this.pulseAge > 0) {
            this.pulseSpiral(serverLevel);
            --this.pulseAge;
        }
        this.fieldAmbient(serverLevel);
    }

    private double fieldY() {
        return this.getY() + 0.12D;
    }

    private void mote(ServerLevel serverLevel, ParticleOptions option, double x, double y, double z, double vx, double vy, double vz) {
        serverLevel.sendParticles(option, x, y, z, 0, vx * 10.0D, vy * 10.0D, vz * 10.0D, 1.0D);
    }

    private void openingRite(ServerLevel serverLevel) {
        float radius = this.getRadius();
        double cx = this.getX();
        double cz = this.getZ();
        double y = this.fieldY();

        if (this.tickCount == 1) {
            serverLevel.sendParticles(MOTE_WHITE, cx, y + 1.2D, cz, 10, 0.35D, 0.55D, 0.35D, 0.04D);
            serverLevel.sendParticles(ParticleTypes.END_ROD, cx, y + 1.4D, cz, 4, 0.22D, 0.35D, 0.22D, 0.02D);
        }

        double t = (this.tickCount - 1) / (double) OPENING_LENGTH;
        double eased = 1.0D - (1.0D - t) * (1.0D - t);
        double r = radius * (0.12D + 0.88D * eased);
        double spin = t * Math.PI * 6.0D;
        for (int i = 0; i < 3; ++i) {
            double angle = spin + Math.PI * 2.0D * i / 3.0D;
            this.mote(serverLevel, i == 1 ? RUNE_WHITE : RUNE_BRIGHT,
                    cx + Math.cos(angle) * r, y + 0.04D, cz + Math.sin(angle) * r, 0.0D, 0.0D, 0.0D);
        }

        if (this.tickCount <= BEAM_LENGTH) {
            int index = (this.tickCount - 1) * 2;
            for (int k = 0; k < 2; ++k) {
                double h = 9.0D * (1.0D - (index + k) / (double) (BEAM_LENGTH * 2));
                this.mote(serverLevel, k == 0 ? RUNE_WHITE : RUNE_BRIGHT,
                        cx + (this.random.nextDouble() - 0.5D) * 0.18D,
                        y + 0.9D + h,
                        cz + (this.random.nextDouble() - 0.5D) * 0.18D,
                        0.0D, -0.3D, 0.0D);
            }
        }
    }

    private void shockwaveRite(ServerLevel serverLevel) {
        float radius = this.getRadius();
        double x = this.getX();
        double y = this.fieldY() + 0.02D;
        double z = this.getZ();
        serverLevel.sendParticles(new CircleExplodeParticleOption(1.0F, 0.94F, 0.6F, radius, 1),
                x, y, z, 0, 0.0D, 0.0D, 0.0D, 0.0D);
        serverLevel.sendParticles(new SphereExplodeParticleOption(1.0F, 0.9F, 0.5F, radius, 1),
                x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    private void fieldAmbient(ServerLevel serverLevel) {
        float radius = this.getRadius();
        double cx = this.getX();
        double cz = this.getZ();
        double y = this.fieldY();

        double spin = this.tickCount * 0.03D;
        for (int i = 0; i < 3; ++i) {
            double angle = spin + Math.PI * 2.0D * i / 3.0D;
            this.mote(serverLevel, i == 1 ? RUNE_BRIGHT : RUNE_GOLD,
                    cx + Math.cos(angle) * radius, y, cz + Math.sin(angle) * radius,
                    -Math.sin(angle) * 0.02D, 0.015D, Math.cos(angle) * 0.02D);
        }

        double p = (this.tickCount % 70) / 70.0D;
        double pullRadius = radius * (1.0D - p * 0.8D);
        double pullAngle = p * Math.PI * 4.0D + this.tickCount * 0.008D;
        double pullY = y + 0.05D + p * 1.8D;
        for (int i = 0; i < 2; ++i) {
            double angle = pullAngle + Math.PI * i;
            this.mote(serverLevel, MOTE_BRIGHT,
                    cx + Math.cos(angle) * pullRadius, pullY, cz + Math.sin(angle) * pullRadius,
                    -Math.sin(angle) * 0.03D, 0.03D, Math.cos(angle) * 0.03D);
        }

        double riseAngle = this.random.nextDouble() * Math.PI * 2.0D;
        double riseRadius = radius * (0.25D + this.random.nextDouble() * 0.7D);
        this.mote(serverLevel, this.random.nextInt(4) == 0 ? MOTE_WHITE : MOTE_GOLD,
                cx + Math.cos(riseAngle) * riseRadius, y + 0.05D, cz + Math.sin(riseAngle) * riseRadius,
                0.0D, 0.05D, 0.0D);
    }

    private void pulseSpiral(ServerLevel serverLevel) {
        float radius = this.getRadius();
        double cx = this.getX();
        double cz = this.getZ();
        double y = this.fieldY();
        double t = (PULSE_LENGTH - this.pulseAge) / (double) PULSE_LENGTH;
        double r = radius * (1.0D - t * 0.75D);
        double spin = t * Math.PI * 2.0D;
        for (int i = 0; i < 2; ++i) {
            double angle = spin + Math.PI * i;
            this.mote(serverLevel, RUNE_BRIGHT,
                    cx + Math.cos(angle) * r, y + 0.03D + t * 0.35D, cz + Math.sin(angle) * r, 0.0D, 0.0D, 0.0D);
        }
    }

    private boolean isAlly(LivingEntity entity) {
        LivingEntity owner = this.getOwner();
        if (owner == null) {
            return false;
        }
        if (entity == owner) {
            return true;
        }
        if (entity instanceof IOwned owned && owner.getUUID().equals(owned.getOwnerId())) {
            return true;
        }
        return false;
    }

    private void healPulse(ServerLevel serverLevel) {
        float radius = this.getRadius();
        double cx = this.getX();
        double cz = this.getZ();
        double y = this.fieldY();
        boolean breathe = this.tickCount / HEAL_INTERVAL % 3 == 0;

        this.pulseAge = PULSE_LENGTH;

        if (breathe) {
            serverLevel.sendParticles(MOTE_WHITE, cx, y + 1.0D, cz, 6, 0.3D, 0.45D, 0.3D, 0.03D);
            serverLevel.sendParticles(ParticleTypes.END_ROD, cx, y + 1.3D, cz, 2, 0.18D, 0.3D, 0.18D, 0.015D);
            serverLevel.playSound(null, cx, this.getY(), cz, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.5F, 1.5F);
        }

        AABB aabb = this.getBoundingBox().inflate(0.5D, 4.0D, 0.5D);
        List<LivingEntity> allies = this.level().getEntitiesOfClass(LivingEntity.class, aabb, this::isAlly);

        int healAmount = BASE_HEAL_AMOUNT + (int) (this.potency * 0.5F);
        if (healAmount < 1) {
            healAmount = 1;
        }

        for (LivingEntity ally : allies) {
            double dx = ally.getX() - this.getX();
            double dz = ally.getZ() - this.getZ();
            if (dx * dx + dz * dz <= (double) (radius * radius)) {
                boolean wounded = ally.getHealth() < ally.getMaxHealth();
                ally.heal(healAmount);
                if (wounded) {
                    this.healedUnitRite(serverLevel, ally);
                }
            }
        }
    }

    private void healedUnitRite(ServerLevel serverLevel, LivingEntity ally) {
        double height = ally.getBbHeight();
        double r = 0.4D;
        double angle = this.random.nextDouble() * Math.PI * 2.0D;
        double ox = Math.cos(angle) * r;
        double oz = Math.sin(angle) * r;
        serverLevel.sendParticles(MOTE_WHITE, ally.getX() + ox, ally.getY() + height * 0.35D, ally.getZ() + oz,
                1, 0.0D, 0.0D, 0.0D, 0.03D);
        serverLevel.sendParticles(MOTE_BRIGHT, ally.getX() - ox, ally.getY() + height * 0.75D, ally.getZ() - oz,
                1, 0.0D, 0.0D, 0.0D, 0.04D);
        if (this.random.nextInt(3) == 0) {
            serverLevel.sendParticles(ParticleTypes.END_ROD, ally.getX() + ox, ally.getY() + height * 0.9D, ally.getZ() + oz,
                    1, 0.0D, 0.0D, 0.0D, 0.02D);
        }
    }

    private void closingRite(ServerLevel serverLevel) {
        float radius = this.getRadius();
        double cx = this.getX();
        double cz = this.getZ();
        double y = this.fieldY();
        serverLevel.sendParticles(MOTE_BRIGHT, cx, y + 0.7D, cz, 10, radius * 0.4D, 0.35D, radius * 0.4D, 0.02D);
        for (int i = 0; i < 18; ++i) {
            double angle = Math.PI * 2.0D * i / 18.0D;
            this.mote(serverLevel, i % 2 == 0 ? RUNE_WHITE : RUNE_BRIGHT,
                    cx + Math.cos(angle) * radius, y, cz + Math.sin(angle) * radius, 0.0D, 0.07D, 0.0D);
        }
        serverLevel.playSound(null, cx, this.getY(), cz, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5F, 1.7F);
    }

    private void clientTick() {
        if (this.tickCount % 10 != 0) {
            return;
        }
        float radius = this.getRadius();
        double angle = this.random.nextDouble() * Math.PI * 2.0D;
        double r = Math.sqrt(this.random.nextDouble()) * radius;
        this.level().addParticle(AetherParticleTypes.GOLDEN_OAK_LEAVES.get(),
                this.getX() + Math.cos(angle) * r,
                this.getY() + 2.4D + this.random.nextDouble(),
                this.getZ() + Math.sin(angle) * r,
                0.0D, 0.0D, 0.0D);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickCount = tag.getInt("Age");
        this.duration = tag.getInt("Duration");
        this.potency = tag.getInt("Potency");
        this.setRadius(tag.getFloat("Radius"));
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", this.tickCount);
        tag.putInt("Duration", this.duration);
        tag.putInt("Potency", this.potency);
        tag.putFloat("Radius", this.getRadius());
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.5F);
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
